package io.vito.ludwieg.types

import io.vito.ludwieg.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.UINT64)
class TypeUint64 : Type<Long>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        buf.writeLong(value!!)
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        value = buf.readLong()
    }
}
