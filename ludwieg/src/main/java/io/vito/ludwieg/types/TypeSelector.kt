package io.vito.ludwieg.types

import io.vito.ludwieg.InvalidType
import io.vito.ludwieg.LudwiegInternalType
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

internal class TypeSelector private constructor() {
    private object Holder { val INSTANCE = TypeSelector() }
    companion object {
        val instance: TypeSelector by lazy { Holder.INSTANCE }
    }

    private val map: HashMap<Int, KClass<out Type<*>>> = hashMapOf(pairs = *arrayOf(
            TypeAny::class, TypeArrayBuffer::class, TypeBlob::class, TypeBool::class,
            TypeFloat64::class, TypeString::class, TypeStructBuffer::class, TypeUint8::class,
            TypeUint32::class, TypeUint64::class, TypeUnknown::class, TypeUUID::class
    ).map { (it.findAnnotation<LudwiegInternalType>()?.protocolType?.value ?: throw InvalidType()) to it }.toTypedArray())

    fun classForType(b: Int) : KClass<out Type<*>> = map[b] ?: TypeUnknown::class
}
