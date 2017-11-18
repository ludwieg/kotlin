package io.vito.ludwieg.types

import io.vito.ludwieg.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.ARRAY)
class TypeArrayBuffer : Type<ByteArray>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        throw IllegalInvocation("TypeArrayBuffer is a transport type and must not be used to encoding operations")
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        val tmp = ByteArrayOutputStream()
        // Payload Size
        val payloadSize = buf.readSize()
        tmp.writeSize(payloadSize)
        // Meta Byte
        tmp.writeByte(buf.readByte())
        // Array Size
        tmp.writeSize(buf.readSize())

        val payload = ByteArray(payloadSize.toInt())
        buf.read(payload)
        tmp.write(payload)

        value = tmp.toByteArray()
    }
}
