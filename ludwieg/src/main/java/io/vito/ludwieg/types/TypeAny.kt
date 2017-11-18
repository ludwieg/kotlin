package io.vito.ludwieg.types

import io.vito.ludwieg.*
import io.vito.ludwieg.models.MetaProtocolByte
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.ANY)
class TypeAny : Type<Type<*>>() {

    override var value: Type<*>?
        get() = super.value
        set(value) {
            when(value) {
                is TypeAny -> throw InvalidAnyValueException("TypeAny cannot retain value of type TypeAny")
                is TypeStruct<*> -> throw InvalidAnyValueException("TypeAny cannot retain value of type TypeStruct")
                is TypeArray<*> -> {
                    if(value.isEmpty) {
                        throw InvalidAnyValueException("TypeAny cannot retain empty TypeArray object")
                    }
                    if(value.value!!.first() is TypeStruct<*>) {
                        throw InvalidAnyValueException("TypeAny cannot retain TypeArray retaining TypeStruct")
                    }
                }
            }
            super.value = value
        }

    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        val internalBuffer = ByteArrayOutputStream(0)
        val t = Type.protocolByteFromType(value!!::class)
        when(value) {
            is TypeArray<*> -> {
                val arrayType = typeFromArray(value!!.value as Collection<*>)
                if(arrayType == TypeUnknown::class) {
                    throw InvalidArrayTypeException("illegal attempt to encode array lacking type annotation")
                }
                Type.encodeTo(internalBuffer, SerializationCandidate(
                        value=value!!,
                        writeType = true,
                        meta = MetaProtocolByte(ProtocolType.ARRAY.value),
                        annotation = FieldMetadata(
                                index = 0,
                                arrayType = protocolByteFromType(arrayType),
                                arraySize = "*",
                                type = ProtocolType.ARRAY
                        )
                ))
            }
            else -> {
                Type.encodeTo(internalBuffer, SerializationCandidate(
                        value = value as Type<*>,
                        meta = MetaProtocolByte(t.value),
                        annotation = FieldMetadata(0, t),
                        writeType = true
                ))
            }
        }

        buf.writeSize(internalBuffer.size())
        internalBuffer.writeTo(buf)
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        val payloadSize = buf.readSize()
        val payload = ByteArray(payloadSize.toInt())
        buf.read(payload)
        val data = ByteArrayInputStream(payload)

        val meta = MetaProtocolByte(data.readByte())
        value = Type.decodeWith(data, meta)
    }
}
