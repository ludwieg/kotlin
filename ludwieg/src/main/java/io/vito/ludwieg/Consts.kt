package io.vito.ludwieg

class Consts {
    companion object {
        fun magicBytes() : IntArray = intArrayOf(0x27, 0x24, 0x50)
        fun hasPrefixedLengthBit() : Int = 0x01
        fun isEmptyBit() : Int = 0x02
    }
}
