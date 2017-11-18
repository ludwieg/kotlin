package io.vito.ludwieg.types

import io.vito.ludwieg.InvalidUUIDValue
import io.vito.ludwieg.LudwiegInternalType
import io.vito.ludwieg.SerializationCandidate
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.UUID)
class TypeUUID : Type<String>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        val reg = Regex("^[0-9a-f]{32}$")
        val v = value!!.toLowerCase().replace("-", "")
        if(!reg.containsMatchIn(v)) {
            throw InvalidUUIDValue()
        }
        val intBuf = ByteArray(16)
        var i = 0
        var pos = 0
        while(i < 32) {
            intBuf[pos++] = ((Character.digit(v[i], 16) shl 4) + Character.digit(v[i+1], 16)).toByte()
            i += 2
        }
        buf.write(intBuf)
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        val sb = StringBuilder(32)
        val buffer = ByteArray(16)
        buf.read(buffer)
        for (b : Byte in buffer) {
            sb.append(String.format("%02x", b))
        }
        value = sb.toString()
    }
}
