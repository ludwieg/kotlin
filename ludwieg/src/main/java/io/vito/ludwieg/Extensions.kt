@file:Suppress("unused")

package io.vito.ludwieg

import io.vito.ludwieg.types.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.and
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.javaField

internal fun ByteArrayOutputStream.writeByte(b : Int) = this.write(b)

internal fun ByteArrayOutputStream.writeShort(i : Short) =
    this.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(i).array())

internal fun ByteArrayOutputStream.writeInt(i : Int) =
    this.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(i).array())


internal fun ByteArrayOutputStream.writeLong(i : Long) =
    this.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(i).array())

internal fun ByteArrayOutputStream.writeDouble(i : Double) =
    this.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(i).array())


internal fun ByteArrayOutputStream.writeSize(i : Byte) = writeSize(i.toLong())
internal fun ByteArrayOutputStream.writeSize(i : Short) = writeSize(i.toLong())
internal fun ByteArrayOutputStream.writeSize(i : Int) = writeSize(i.toLong())
internal fun ByteArrayOutputStream.writeSize(i : Long) = when {
    i == 0L -> this.writeByte(LengthEncoding.BITS0.value)
    i <= 255 -> {
        this.writeByte(LengthEncoding.BITS8.value)
        this.writeByte(i.toInt())
    }
    i <= 65535 -> {
        this.writeByte(LengthEncoding.BITS16.value)
        this.writeShort(i.toShort())
    }
    i <= 4294967295 -> {
        this.writeByte(LengthEncoding.BITS32.value)
        this.writeInt(i.toInt())
    }
    i <= Long.MAX_VALUE -> {
        this.writeByte(LengthEncoding.BITS64.value)
        this.writeLong(i)
    }
    else -> throw InvalidLengthValueException()
}

internal fun ByteArrayInputStream.readByte() : Int = this.read() and 0xFF

internal fun ByteArrayInputStream.readShort() : Short {
    val arr = ByteArray(2)
    this.read(arr)
    return ByteBuffer.wrap(arr).order(ByteOrder.LITTLE_ENDIAN).short

}

internal fun ByteArrayInputStream.readInt() : Int {
    val arr = ByteArray(4)
    this.read(arr)
    return ByteBuffer.wrap(arr).order(ByteOrder.LITTLE_ENDIAN).int
}

internal fun ByteArrayInputStream.readLong() : Long {
    val arr = ByteArray(8)
    this.read(arr)
    return ByteBuffer.wrap(arr).order(ByteOrder.LITTLE_ENDIAN).long
}

internal fun ByteArrayInputStream.readDouble() : Double {
    val arr = ByteArray(8)
    this.read(arr)
    return ByteBuffer.wrap(arr).order(ByteOrder.LITTLE_ENDIAN).double
}

internal fun ByteArrayInputStream.peek() : Byte {
    this.mark(0)
    val ret = this.read()
    this.reset()
    return ret.toByte()
}

internal fun ByteArrayInputStream.readSize() : Long {
    val len = LengthEncoding.fromByte(this.readByte())
    return when(len) {
        LengthEncoding.BITS0 -> 0L
        LengthEncoding.BITS8 -> (this.readByte() and 0xFF).toLong()
        LengthEncoding.BITS16 -> (this.readShort() and 0xFF).toLong()
        LengthEncoding.BITS32 -> (this.readInt() and 0xFFFF).toLong()
        LengthEncoding.BITS64 -> (this.readLong() and 0xFFFFFFFF)
    }
}

internal fun KClass<*>.extractLudwiegFields() : HashMap<KProperty1<out Any, Any?>, FieldMetadata> =
        hashMapOf(pairs = *this.memberProperties
                .filter { it.annotations.any { it is LudwiegField } }
                .filter { it.returnType.isSubtypeOf(Type::class.starProjectedType) }
                .map { it to FieldMetadata(it.findAnnotation()!!) }.toTypedArray())

internal fun <T : Any> createObject(type: java.lang.Class<T>, objects: ArrayList<Type<*>>) : T {
    val reflect = type.kotlin
    val instance = type.newInstance()
    val annotations = reflect.extractLudwiegFields().entries
    annotations.forEach {
        val prop = it.key
        val annotation = it.value
        if(annotation.index < objects.size) {
            val field = prop.javaField!!
            val obj = objects[annotation.index]

            when(obj) {
                is TypeArrayBuffer -> {
                    if(annotation.arrayType == ProtocolType.STRUCT && annotation.structUserType == Nothing::class) {
                        throw InternalInconsistencyException("Detected array type annotation lacking generic type information. This is an inconsistency bug caused by the 'ludco' tool. Please file a new issue.")
                    }
                    val value = TypeArray(when(annotation.type) {
                        ProtocolType.STRUCT -> annotation.structUserType
                        else -> Type::class
                    }.java)
                    value.decodeValue(ByteArrayInputStream(obj.value))
                    if(annotation.arrayType == ProtocolType.STRUCT) {
                        // At this point, coercion is required to ensure array values are
                        // TypeStruct<> instead of TypeStructBuffer, used as the internal transport
                        // type before deserialisation information is available.
                        value.coerceFromStructTypeTo(annotation.structUserType)
                    }
                    field.set(instance, value)
                }
                is TypeStructBuffer -> {
                    if(annotation.structUserType == Nothing::class) {
                        throw InternalInconsistencyException("Detected struct type annotation lacking generic type information. This is an inconsistency bug caused by the 'ludco' tool. Please file a new issue.")
                    }
                    val value = TypeStruct(annotation.structUserType.java)
                    value.decodeValue(ByteArrayInputStream(obj.value))
                    field.set(instance, value)
                }
                else -> field.set(instance, obj)
            }
        }
    }
    return instance
}
