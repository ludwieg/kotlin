package io.vito.ludwieg.types

import io.vito.ludwieg.LudwiegInternalType
import io.vito.ludwieg.SerializationCandidate
import io.vito.ludwieg.readInt
import io.vito.ludwieg.writeInt
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.UINT32)
class TypeUint32 : Type<Int>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        buf.writeInt(value!!)
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        value = buf.readInt()
    }
}
