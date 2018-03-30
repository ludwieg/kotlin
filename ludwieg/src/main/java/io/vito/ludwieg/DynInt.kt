package io.vito.ludwieg

import kotlin.experimental.and

enum class DynIntValueKind(val value: Int) {
    INVALID(0x00),
    UINT8(0x01),
    UINT16(0x02),
    UINT32(0x03),
    UINT64(0x04),
    INT8(0x05),
    INT16(0x06),
    INT32(0x07),
    INT64(0x08),
    FLOAT32(0x09),
    FLOAT64(0x0A);

    companion object {
        internal fun kindFromByte(t: Int): DynIntValueKind =
                enumValues<DynIntValueKind>().find { it.value == t } ?: throw InvalidTypeException()
    }
}

class DynInt {
    private var hasValue: Boolean = false
    internal var value: Double = 0.0
    internal var underlyingType: DynIntValueKind = DynIntValueKind.INVALID

    constructor(value: Double) : this(DynInt.InferNumberType(value))

    constructor(value: Float) : this(DynInt.InferNumberType(value.toDouble()))

    constructor(value: Long) : this(DynInt.InferNumberType(value.toDouble()))

    constructor(value: Int) : this(DynInt.InferNumberType(value.toDouble()))

    constructor(value: Short) : this(DynInt.InferNumberType(value.toDouble()))

    constructor(value: Byte) : this(DynInt.InferNumberType(value.toDouble()))

    internal constructor(value: Pair<DynIntValueKind, Double>) : this(Triple(true, value.first, value.second))


    internal constructor(value: Triple<Boolean, DynIntValueKind, Double>) {
        this.hasValue = value.first
        this.underlyingType = value.second
        this.value = value.third
    }

    fun toDouble() : Double = value
    fun toFloat() : Float = value.toFloat()
    fun toLong() : Long = value.toLong()
    fun toInt() : Int = value.toInt()
    fun toShort() : Short = value.toShort()
    fun toByte() : Byte = value.toByte()
    fun getUnderlyingType() : DynIntValueKind = underlyingType


    companion object {
        private val Epsilon: Double = 1e-9

        private fun InferNumberType(value: Double): Pair<DynIntValueKind, Double> {
            val frac: Double = value % 1
            val integer: Double = value - frac
            if(frac > Epsilon && frac < 1.0 - Epsilon) {
                if(value >= -Limits.MaxFloat32 && value <= Limits.MaxFloat32) {
                    return Pair(DynIntValueKind.FLOAT32, value)
                }

                return Pair(DynIntValueKind.FLOAT64, value)
            }

            val maxMins = arrayOf(
                    arrayOf(0.0, Limits.MaxUint8.toDouble()),
                    arrayOf(0.0, Limits.MaxUint16.toDouble()),
                    arrayOf(0.0, Limits.MaxUint32.toDouble()),
                    arrayOf(0.0, Limits.MaxUint64.toDouble()),
                    arrayOf(Limits.MinInt8.toDouble(), Limits.MaxInt8.toDouble()),
                    arrayOf(Limits.MinInt16.toDouble(), Limits.MaxInt16.toDouble()),
                    arrayOf(Limits.MinInt32.toDouble(), Limits.MaxInt32.toDouble()),
                    arrayOf(Limits.MinInt64.toDouble(), Limits.MaxInt64.toDouble())
            )

            val types = arrayOf(
                    DynIntValueKind.UINT8,
                    DynIntValueKind.UINT16,
                    DynIntValueKind.UINT32,
                    DynIntValueKind.UINT64,
                    DynIntValueKind.INT8,
                    DynIntValueKind.INT16,
                    DynIntValueKind.INT32,
                    DynIntValueKind.INT64
            )

            for(i in maxMins.indices) {
                val v = maxMins[i]
                if(integer >= v[0] && integer <= v[1]) {
                    return Pair(types[i], integer)
                }
            }

            return Pair(DynIntValueKind.INVALID, 0.0)
        }
    }
}
