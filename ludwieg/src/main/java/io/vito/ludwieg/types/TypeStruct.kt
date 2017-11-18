package io.vito.ludwieg.types

import io.vito.ludwieg.*
import io.vito.ludwieg.models.MetaProtocolByte
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.isAccessible

private typealias KProp = KProperty1<out Any, Any?>

class TypeStruct<T : Any>(val type: java.lang.Class<T>) : Type<Any>() {
    var nativeValue: T? = null
    override val isEmpty : Boolean = nativeValue == null

    fun forceSetNativeValue(v: Any?) {
        @Suppress("UNCHECKED_CAST")
        nativeValue = v as T?
    }

    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        val target = nativeValue!!
        val reflect = type.kotlin

        if(!isValidSerializationCandidate(reflect)) {
            throw InvalidSerializationCandidate("input value is not a valid serialization candidate")
        }
        val fields = reflect.extractLudwiegFields()

        if(!validateFields(fields.keys, target)) {
            throw InvalidSerializationCandidate("input value contains invalid fields")
        }

        val internalBuf = ByteArrayOutputStream(0)
        fields.entries
                .sortedBy { it.value.index }
                .forEach {
                    it.key.isAccessible = true
                    val v = it.key.getter.call(target) as Type<*>
                    Type.encodeTo(internalBuf, SerializationCandidate(
                            annotation = it.value,
                            meta = MetaProtocolByte(it.value.type.value),
                            value = v as? Type<*>,
                            isRoot = false,
                            writeType = true
                    ))
                }
        buf.writeSize(internalBuf.size())
        buf.write(internalBuf.toByteArray())
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        val objects = ArrayList<Type<*>>()
        while(buf.available() > 0) {
            val meta = MetaProtocolByte(buf.readByte())
            val v = Type.decodeWith(buf, meta)
            objects.add(v)
        }
        nativeValue = createObject(type, objects)
    }

    private fun isValidSerializationCandidate(reflect: KClass<*>) : Boolean =
            reflect.annotations.any { it is LudwiegPackage || it is Serializable }

    private fun validateFields(fields: Set<KProp>, instance: Any) : Boolean =
            fields
                .map { it.isAccessible = true; it }
                .all { it.getter.call(instance) is Type<*> }

}
