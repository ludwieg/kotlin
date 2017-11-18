package io.vito.ludwieg.types

import io.vito.ludwieg.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.BLOB)
class TypeBlob : Type<ByteArray>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        val v = value!!
        buf.writeSize(v.count())
        buf.write(v)
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        val len = buf.readSize()
        value = ByteArray(len.toInt())
        buf.read(value)
    }
}
