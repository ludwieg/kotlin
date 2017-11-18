package io.vito.ludwieg

import io.vito.ludwieg.models.MetaProtocolByte
import io.vito.ludwieg.types.Type

data class SerializationCandidate (var meta: MetaProtocolByte? = null,
                                   var annotation: FieldMetadata? = null,
                                   var value: Type<*>? = null,
                                   var writeType: Boolean = true,
                                   var isRoot: Boolean = false)
