package io.vito.ludwieg

import io.vito.ludwieg.types.ProtocolType
import kotlin.reflect.KClass

annotation class LudwiegField(val index: Int,
                              val protocolType: ProtocolType,
                              val arrayType: ProtocolType = ProtocolType.UNKNOWN,
                              val arraySize: String = "*",
                              val structType: KClass<*> = Nothing::class)
