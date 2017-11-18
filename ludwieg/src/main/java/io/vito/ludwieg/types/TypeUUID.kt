package io.vito.ludwieg.types

import io.vito.ludwieg.LudwiegInternalType
import io.vito.ludwieg.SerializationCandidate
import io.vito.ludwieg.UUID
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.UUID)
class TypeUUID : Type<UUID>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        buf.write(value!!.toByteArray())
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        val buffer = ByteArray(16)
        buf.read(buffer)
        value = UUID.from(buffer)
    }
}
