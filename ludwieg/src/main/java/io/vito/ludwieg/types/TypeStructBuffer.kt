package io.vito.ludwieg.types

import io.vito.ludwieg.IllegalInvocationException
import io.vito.ludwieg.LudwiegInternalType
import io.vito.ludwieg.SerializationCandidate
import io.vito.ludwieg.readSize
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.STRUCT)
class TypeStructBuffer : Type<ByteArray>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        throw IllegalInvocationException("TypeStructBuffer is a transport type and must not be used to encoding operations")
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        val size = buf.readSize()
        val internalBuffer = ByteArray(size.toInt())
        buf.read(internalBuffer)
        value = internalBuffer
    }
}
