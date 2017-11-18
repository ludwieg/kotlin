package io.vito.ludwieg.types

import io.vito.ludwieg.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


@LudwiegInternalType(ProtocolType.STRING)
class TypeString : Type<String>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        val arr = value!!.toByteArray(Charsets.UTF_8)
        buf.writeSize(arr.count())
        buf.write(arr)
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        val size = buf.readSize()
        val arr = ByteArray(size.toInt())
        buf.read(arr)
        value = String(arr, Charsets.UTF_8)
    }
}
