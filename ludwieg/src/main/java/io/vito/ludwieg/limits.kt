package io.vito.ludwieg

@Suppress("INTEGER_OVERFLOW")
internal class Limits {
    companion object {
        const val MaxInt8: Byte = (1 shl 7) - 1
        const val MinInt8: Byte  = -1 shl 7
        const val MaxInt16: Short = (1 shl 15) - 1
        const val MinInt16: Short = -1  shl  15
        const val MaxInt32: Long = (-1L and 0xFFFFFFFFL)
        const val MinInt32: Long = -((-1L shl 31) -1)
        const val MaxInt64: Long = 0x7FFFFFFFFFFFFFFF
        const val MinInt64: Long = (1L shl 63) - 1
        const val MaxUint8: Int = 255 and 0xFF
        const val MaxUint16: Long = ((1 shl 16) - 1)
        const val MaxUint32: Long = (1L shl 32) - 1

        // FIXME: This by far isn't MaxUint64.
        const val MaxUint64: Long = 0x7FFFFFFFFFFFFFFF
        const val MaxFloat32: Float = 3.40282346638528859811704183484516925440e+38.toFloat()
        const val MaxFloat64: Double = 1.797693134862315708145274237317043567981e+308
    }
}
