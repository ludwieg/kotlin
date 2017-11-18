package io.vito.ludwieg

import io.vito.ludwieg.types.ProtocolType
import kotlin.reflect.KClass

/**
 * LudwiegField identifies a field as the source and destination of a transferred package field data
 */
annotation class LudwiegField(val index: Int,
                              val protocolType: ProtocolType,
                              val arrayType: ProtocolType = ProtocolType.UNKNOWN,
                              val arraySize: String = "*",
                              val structType: KClass<*> = Nothing::class)
