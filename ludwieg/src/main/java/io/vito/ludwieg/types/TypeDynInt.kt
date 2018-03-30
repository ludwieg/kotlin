package io.vito.ludwieg.types

import io.vito.ludwieg.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@LudwiegInternalType(ProtocolType.DYNINT)
class TypeDynInt : Type<DynInt>() {
    override fun encodeValueTo(buf: ByteArrayOutputStream, candidate: SerializationCandidate) {
        buf.writeByte(value!!.underlyingType.value)
        when(value!!.underlyingType) {
            DynIntValueKind.INVALID -> {
                return
            }
            DynIntValueKind.INT8, DynIntValueKind.UINT8 -> {
                buf.writeByte(value!!.value.toInt())
            }
            DynIntValueKind.INT16, DynIntValueKind.UINT16 -> {
                buf.writeShort(value!!.value.toShort())
            }
            DynIntValueKind.INT32, DynIntValueKind.UINT32 -> {
                buf.writeInt(value!!.value.toInt())
            }
            DynIntValueKind.INT64, DynIntValueKind.UINT64 -> {
                buf.writeLong(value!!.value.toLong())
            }
            DynIntValueKind.FLOAT32, DynIntValueKind.FLOAT64 -> {
                buf.writeDouble(value!!.value)
            }
        }
    }

    override fun decodeValue(buf: ByteArrayInputStream) {
        val dynKind = DynIntValueKind.kindFromByte(buf.readByte())
        val readValue: Double = when (dynKind) {
            DynIntValueKind.INVALID -> 0.0
            DynIntValueKind.UINT8, DynIntValueKind.INT8 -> buf.readByte().toDouble()
            DynIntValueKind.UINT16, DynIntValueKind.INT16 -> buf.readShort().toDouble()
            DynIntValueKind.UINT32, DynIntValueKind.INT32 -> buf.readInt().toDouble()
            DynIntValueKind.UINT64, DynIntValueKind.INT64 -> buf.readLong().toDouble()
            DynIntValueKind.FLOAT32, DynIntValueKind.FLOAT64 -> buf.readDouble()
        }
        value = DynInt(Triple(dynKind != DynIntValueKind.INVALID, dynKind, readValue))
    }
}
