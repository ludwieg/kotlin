package io.vito.ludwieg.models

import io.vito.ludwieg.Consts

internal class MetaProtocolByte(input: Int) {

    var type: Int = (input and Consts.isEmptyBit().inv())

    var isEmpty: Boolean = (input and Consts.isEmptyBit()) == Consts.isEmptyBit()

    private val isLengthPrefixed: Boolean = (input and Consts.hasPrefixedLengthBit()) == Consts.hasPrefixedLengthBit()

    fun byte() : Int {
        var result : Int = type

        result = if(isEmpty) {
            result or Consts.isEmptyBit()
        } else {
            result and Consts.isEmptyBit().inv()
        }

        result = if(isLengthPrefixed) {
            result or Consts.hasPrefixedLengthBit()
        } else {
            result and Consts.hasPrefixedLengthBit().inv()
        }

        return result
    }
}
