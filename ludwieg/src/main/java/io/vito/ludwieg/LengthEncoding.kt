package io.vito.ludwieg

enum class LengthEncoding(val value: Int) {
    BITS8(0x01),
    BITS16(0x02),
    BITS32(0x03),
    BITS64(0x04);

    companion object {
        fun fromByte(b : Int) : LengthEncoding =
                values().find { it.value == b } ?: throw InvalidLengthIdentifier()
    }
}
