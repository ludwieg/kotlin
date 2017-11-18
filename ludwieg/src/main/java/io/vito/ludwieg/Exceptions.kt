package io.vito.ludwieg

class InvalidLengthIdentifier : Exception()
class InvalidLengthValue : Exception()
class InvalidUUIDValue : Exception()
class CannotEncodeUnknownType : Exception()
class InvalidSerializationCandidate(msg : String? = null) : Exception(msg)
class InvalidArrayType : Exception()
class InvalidArraySize(msg : String? = null) : Exception(msg)
class InvalidType(msg : String? = null) : Exception(msg)
class IllegalInvocation(msg: String) : Exception(msg)
class InternalInconsistency(msg: String) : Exception(msg)
