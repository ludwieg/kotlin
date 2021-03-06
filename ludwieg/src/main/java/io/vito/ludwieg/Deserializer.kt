package io.vito.ludwieg

import io.vito.ludwieg.models.MessageMeta
import io.vito.ludwieg.models.MetaProtocolByte
import io.vito.ludwieg.types.Type
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.reflect.KClass

class Deserializer {
    private enum class State {
        PRELUDE,
        VERSION,
        MESSAGE_ID,
        PACKAGE_TYPE,
        PACKAGE_SIZE_PRELUDE,
        PACKAGE_SIZE_VALUE,
        PAYLOAD
    }

    private var state = State.PRELUDE
    private var tmpBuffer = ByteArrayOutputStream()
    private var readBytes = 0
    private var packageSizeBytes = 0
    private var packageSize: Long = 0
    private var sizeEncoding = LengthEncoding.BITS8

    private var protocolVersion: Int = 0x00
    private var messageID: Int = 0x00
    private var packageType: Int = 0x00


    var messageMeta : MessageMeta? = null
        private set
    var result : Any? = null
        private set
    var stats : DeserializationStats? = null
        private set


    private fun reset() : Boolean {
        state = State.PRELUDE
        tmpBuffer.reset()
        readBytes = 0
        packageSize = 0
        sizeEncoding = LengthEncoding.BITS8
        messageMeta = null
        result = null
        return false
    }

    fun read(byte: Int) : Boolean {
        when(state) {
            State.PRELUDE -> {
                val expectation = Consts.magicBytes()[readBytes]
                if(byte == expectation) {
                    readBytes++
                    if(readBytes == Consts.magicBytes().count()) {
                        state = State.VERSION
                    }
                } else {
                    return reset()
                }
            }

            State.VERSION -> {
                protocolVersion = byte
                readBytes++
                state = State.MESSAGE_ID
            }

            State.MESSAGE_ID -> {
                messageID = byte
                readBytes++
                state = State.PACKAGE_TYPE
            }

            State.PACKAGE_TYPE -> {
                packageType = byte
                readBytes++
                state = State.PACKAGE_SIZE_PRELUDE
            }

            State.PACKAGE_SIZE_PRELUDE -> {
                if(byte < LengthEncoding.BITS0.value || byte > LengthEncoding.BITS64.value) {
                    return reset()
                }

                sizeEncoding = LengthEncoding.fromByte(byte)
                packageSizeBytes = when(sizeEncoding) {
                    LengthEncoding.BITS0 -> {
                        tmpBuffer.reset()
                        return handleDeserialization()
                    }
                    LengthEncoding.BITS8 -> 1
                    LengthEncoding.BITS16 -> 2
                    LengthEncoding.BITS32 -> 4
                    LengthEncoding.BITS64 -> 8
                }
                tmpBuffer.reset()
                readBytes++
                state = State.PACKAGE_SIZE_VALUE
            }

            State.PACKAGE_SIZE_VALUE -> {
                tmpBuffer.writeByte(byte)
                readBytes++
                if(tmpBuffer.size() == packageSizeBytes) {
                    val buf = ByteArrayInputStream(tmpBuffer.toByteArray())

                    packageSize = when(sizeEncoding) {
                        LengthEncoding.BITS0 -> throw InternalInconsistencyException("incorrect handling of sizing information")
                        LengthEncoding.BITS8 -> buf.readByte().toLong()
                        LengthEncoding.BITS16 -> buf.readShort().toLong()
                        LengthEncoding.BITS32 -> buf.readInt().toLong()
                        LengthEncoding.BITS64 -> buf.readLong()
                    }
                    tmpBuffer.reset()
                    state = State.PAYLOAD
                }
            }

            State.PAYLOAD -> {
                tmpBuffer.writeByte(byte)
                readBytes++
                if(tmpBuffer.size() == packageSize.toInt()) {
                    return handleDeserialization()
                }
            }
        }

        return false
    }

    private fun handleDeserialization() : Boolean {
        val pType = Registry.instance.query(packageType) ?: return reset()
        val softResult = deserializeIntoClass(pType)
        result = softResult.first
        stats = softResult.second
        messageMeta = MessageMeta(protocolVersion, messageID, packageType)
        return true
    }

    private fun deserializeIntoClass(type: KClass<out Any>) : Pair<Any?, DeserializationStats> {
        val objects = ArrayList<Type<*>>()
        val data = ByteArrayInputStream(tmpBuffer.toByteArray())
        var received = 0
        var applied = 0

        while(data.available() > 0) {
            val meta = MetaProtocolByte(data.readByte())
            val v = Type.decodeWith(data, meta)
            if(v.deserializationStats != null) {
                received += v.deserializationStats!!.receivedFields
                applied += v.deserializationStats!!.appliedFields
            }

            objects.add(v)
        }
        val r = createObject(type.java, objects)
        received += r.second.receivedFields
        applied += r.second.appliedFields

        return Pair(r.first, DeserializationStats(received, applied))
    }

}
