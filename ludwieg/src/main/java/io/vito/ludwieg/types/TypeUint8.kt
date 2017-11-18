package io.vito.ludwieg.types

import io.vito.ludwieg.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.UINT8)
class TypeUint8 : Type<Int>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        buf.writeByte(value!!)
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        value = buf.read()
    }
}
