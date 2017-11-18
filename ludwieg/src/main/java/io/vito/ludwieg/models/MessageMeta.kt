package io.vito.ludwieg.models

import io.vito.ludwieg.writeByte
import java.io.ByteArrayOutputStream


class MessageMeta(val protocolVersion: Int, val messageID: Int, val packageType: Int) {
    fun writeTo(buf: ByteArrayOutputStream) {
        buf.writeByte(protocolVersion)
        buf.writeByte(messageID)
        buf.writeByte(packageType)
    }
}
