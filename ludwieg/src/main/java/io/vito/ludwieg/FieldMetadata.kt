package io.vito.ludwieg

import io.vito.ludwieg.models.MetaProtocolByte
import io.vito.ludwieg.types.ProtocolType
import kotlin.reflect.KClass


class FieldMetadata(val index: Int,
                    val type: ProtocolType,
                    val arrayType: ProtocolType = ProtocolType.UNKNOWN,
                    val arraySize: String = "*",
                    val structUserType : KClass<*> = Nothing::class) {

    constructor(ludwiegField: LudwiegField) : this(ludwiegField.index, ludwiegField.protocolType, ludwiegField.arrayType, ludwiegField.arraySize, ludwiegField.structType)
    fun metaProtocolByte() : MetaProtocolByte = MetaProtocolByte(type.value)
}
