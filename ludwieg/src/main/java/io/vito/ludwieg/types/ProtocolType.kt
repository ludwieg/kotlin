package io.vito.ludwieg.types


enum class ProtocolType(val value: Int) {
    UNKNOWN(0x00),
    UINT8(0x01 shl 2),
    UINT32(0x02 shl 2),
    UINT64(0x03 shl 2),
    DOUBLE(0x04 shl 2),
    STRING((0x05 shl 2) or 0x1),
    BLOB((0x06 shl 2) or 0x1),
    BOOL(0x07 shl 2),
    ARRAY((0x08 shl 2) or 0x1),
    UUID(0x09 shl 2),
    ANY((0x0A shl 2) or 0x1),
    STRUCT((0x0B shl 2) or 0x1),
}
