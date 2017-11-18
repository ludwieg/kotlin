package io.vito.ludwieg.types

import io.vito.ludwieg.*
import io.vito.ludwieg.models.MetaProtocolByte
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.reflect.KClass

class TypeArray<T : Type<*>>(val type: java.lang.Class<in T>) : Type<ArrayList<T>>() {

    override val isEmpty: Boolean
        get() = value == null || value?.size == 0

    override var value: ArrayList<T>?
        get() = super.value
        set(value) {
            if(value?.any { it is TypeAny } == true) {
                throw InvalidArrayTypeException("TypeArray<T> cannot retain values of TypeAny")
            }
            super.value = value
        }

    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        val annotation = candidate.annotation!!

        val rawArraySize = annotation.arraySize
        val arrayType = annotation.arrayType
        if(arrayType == ProtocolType.UNKNOWN) {
            throw InvalidArrayTypeException("illegal attempt to encode array lacking type annotation")
        }

        if(rawArraySize != "*") {
            try {
                rawArraySize.toInt(10)
            } catch(_: NumberFormatException) {
                throw InvalidArraySizeException("Array size should be * or a number value. Found $rawArraySize")
            }
        }

        val arrayLogicalSize = value?.size ?: 0
        val internalBuffer = ByteArrayOutputStream()
        val arrayAnnotation = FieldMetadata(0, arrayType)
        val arrayMetaByte = arrayAnnotation.metaProtocolByte()

        (0 until arrayLogicalSize)
                .map { value!![it] }
                .map { SerializationCandidate(value = it, annotation = arrayAnnotation, meta = arrayMetaByte, writeType = false, isRoot = false) }
                .forEach { Type.encodeTo(internalBuffer, it) }

        buf.writeSize(internalBuffer.size())
        buf.writeByte(arrayMetaByte.byte())
        buf.writeSize(arrayLogicalSize)
        buf.write(internalBuffer.toByteArray())
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        val payloadSize = buf.readSize()
        val metaByte = MetaProtocolByte(buf.readByte())
        val arraySize = buf.readSize()
        val arr = ArrayList<T>(arraySize.toInt())


        val payload = ByteArray(payloadSize.toInt())
        buf.read(payload)
        val data = ByteArrayInputStream(payload)

        while(data.available() > 0) {
            val t = Type.decodeWith(data, metaByte)
            @Suppress("UNCHECKED_CAST")
            arr.add(t as T)
        }
        value = arr
    }

    internal fun coerceFromStructTypeTo(type: KClass<*>) {
        if(value?.any { it !is TypeStructBuffer } != false) {
            throw IllegalInvocationException("attempt to coerce from struct type a non-struct array")
        }
        val v = ArrayList<T>()
        value!!
                .filterIsInstance<TypeStructBuffer>()
                .forEach {
                    val tmpVal = TypeStruct(type.java)
                    tmpVal.decodeValue(ByteArrayInputStream(it.value))
                    @Suppress("UNCHECKED_CAST") // At this point, T is TypeStruct<>, and so is the
                                                // value being set.
                    v.add(tmpVal as T)
                }
        value = v
    }

    fun <Z : Any> getNativeArray(targetClass: java.lang.Class<out Z>) : List<Z?>? {
        return value?.map {
            @Suppress("UNCHECKED_CAST") // it as Z is safe because of targetClass.isInstance
            if(targetClass.isInstance(it.value)) {
                it.value as Z
            } else {
                null
            }
        }
    }

    fun <Z : Any> setNativeArray(arr: Collection<Z>?) {
        if(arr == null) {
            value = null
            return
        }

        if(value == null) {
            value = ArrayList()
        }
        value?.clear()
        @Suppress("UNCHECKED_CAST") // type.newInstance() as T is safe.
        for(it: Z in arr) {
            val inst = type.newInstance() as T
            inst.forceSet(it)
            value?.add(inst)
        }
    }
}
