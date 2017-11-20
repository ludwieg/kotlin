package io.vito.ludwieg.types

import io.vito.ludwieg.*
import io.vito.ludwieg.models.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf

abstract class Type<T> {
    internal abstract fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate)
    internal abstract fun decodeValue(buf: ByteArrayInputStream)

    open var value: T? = null
    open val isEmpty: Boolean
        get() = value == null

    internal fun forceSet(i: Any) : Type<T> {
        @Suppress("UNCHECKED_CAST")
        value = i as T
        return this
    }

    companion object {
        internal fun encodeTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
            val type = candidate.value
            val empty = type == null || type.isEmpty

            if(candidate.writeType) {
                candidate.meta!!.isEmpty = empty
                buf.writeByte(candidate.meta!!.byte())
            }

            if(empty) {
                return
            }

            type!!.encodeValueTo(buf, candidate)
        }

        internal fun decodeWith(buf: ByteArrayInputStream, meta: MetaProtocolByte) : Type<*> {
            val t = TypeSelector.instance.classForType(meta.type)
            val inst = t.java.newInstance()
            if(meta.isEmpty) {
                inst.value = null
                return inst
            }
            inst.decodeValue(buf)
            return inst
        }

        internal fun protocolByteFromType(t: KClass<out Any>) : ProtocolType =
                t.findAnnotation<LudwiegInternalType>()?.protocolType ?: throw InvalidTypeException()

        fun <T: Any?> coerce(v: T) : Type<*>? {
            // coerce() won't ever correctly coerce UUIDs, as UUIDs are represented using String.
            if(v == null) return null

            return when(v) {
                is String -> TypeString().forceSet(v)
                is ByteArray -> TypeBlob().forceSet(v)
                is Boolean -> TypeBool().forceSet(v)
                is Byte -> TypeUint8().forceSet(v)
                is Int -> TypeUint32().forceSet(v)
                is Long -> TypeUint64().forceSet(v)
                is Double -> TypeDouble().forceSet(v)
                is Collection<*> -> {
                    val t = typeFromArray(v)

                    @Suppress("UNCHECKED_CAST")
                    val arr = ArrayList::class.java.newInstance() as ArrayList<Type<*>>

                    for(obj: Any? in v) {
                        val inst = t.java.newInstance() as Type<*>
                        inst.forceSet(obj!!)
                        arr.add(inst)
                    }
                    TypeArray(t.java).forceSet(arr)
                }
                else -> throw IllegalArgumentException("cannot coerce type $v")
            }
        }

        internal fun <E> typeFromArray(col: Collection<E>) : KClass<*> {
            // At this point, we have a problem, since it is not possible to infer type from an
            // empty array, and Java's type erasure makes it unable to collect generic information.

            val filtered = col.filter { it != null }
            if(filtered.isEmpty()) {
                return TypeUnknown::class
            }

            val obj = filtered.first() as Any?

            if(obj!!::class.isSubclassOf(Type::class)) {
                return obj::class
            }

            val coerced = when(obj) {
                is Byte -> TypeUint8::class
                is Int -> TypeUint32::class
                is Long -> TypeUint64::class
                is Double -> TypeDouble::class
                is ByteArray -> TypeBlob::class
                is Boolean -> TypeBool::class
                is String -> TypeString::class
                is Array<*> -> TypeArray::class
                else -> if(obj::class.findAnnotation<Serializable>() != null) TypeString::class else null
            }

            return coerced ?: TypeUnknown::class
        }
    }
}
