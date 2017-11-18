package io.vito.ludwieg

import io.vito.ludwieg.types.ProtocolType

/**
 * LudwiegInternalType identifies a type implementation as the handler of a given [ProtocolType]
 */
internal annotation class LudwiegInternalType(val protocolType: ProtocolType)
