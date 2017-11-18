package io.vito.ludwieg

import io.vito.ludwieg.models.MessageMeta
import io.vito.ludwieg.types.Type
import io.vito.ludwieg.types.TypeStruct
import java.io.ByteArrayOutputStream
import kotlin.reflect.full.findAnnotation

class Serializer {
    fun <T : Any> serialize(obj: T, messageID: Int) : ByteArray {
        val reflect = obj::class
        if(!reflect.annotations.any { it is LudwiegPackage }) {
            throw InvalidSerializationCandidate("input value is not a valid serialization candidate")
        }
        val data = ByteArrayOutputStream(0)
        Consts.magicBytes().forEach { data.write(it) }

        val meta = MessageMeta(0x01, messageID, reflect.findAnnotation<LudwiegPackage>()!!.id)
        meta.writeTo(data)

        val str = TypeStruct(reflect.java as Class<*>)
        str.forceSetNativeValue(obj)
        val candidate = SerializationCandidate(value = str, isRoot = true, writeType = false)
        Type.encodeTo(data, candidate)

        return data.toByteArray()
    }
}
