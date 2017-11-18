package io.vito.ludwieg.types

import io.vito.ludwieg.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.BOOL)
class TypeBool : Type<Boolean>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        buf.writeByte(if (value!!) 0x01 else 0x02)
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        value = buf.read().toByte() == (0x01).toByte()
    }
}
