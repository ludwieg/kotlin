package io.vito.ludwieg.types

import io.vito.ludwieg.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.UNKNOWN)
class TypeUnknown : Type<ByteArray>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        throw CannotEncodeUnknownTypeException()
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        val size = buf.readSize()
        val data = ByteArray(size.toInt())
        buf.read(data)
        value = data
    }
}
