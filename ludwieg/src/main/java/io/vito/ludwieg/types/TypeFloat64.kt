package io.vito.ludwieg.types

import io.vito.ludwieg.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.FLOAT64)
class TypeFloat64 : Type<Double>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        buf.writeDouble(value!!)
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        value = buf.readDouble()
    }
}
