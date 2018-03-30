package io.vito.ludwieg

data class DeserializationStats(val receivedFields: Int,
                                val appliedFields: Int) {
    fun knowsAllFields(): Boolean = receivedFields == appliedFields
}
