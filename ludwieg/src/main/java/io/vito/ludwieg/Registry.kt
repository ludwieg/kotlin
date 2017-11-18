package io.vito.ludwieg

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation


class Registry private constructor() {
    private object Holder { val INSTANCE = Registry() }
    companion object {
        val instance: Registry by lazy { Holder.INSTANCE }
    }

    private val map: HashMap<Int, KClass<out Any>> = HashMap()

    fun register(type: Class<*>) =
            map.put((type.kotlin.findAnnotation<LudwiegPackage>() ?: throw InvalidTypeException()).id, type.kotlin)
    @Suppress("unused")
    fun register(vararg types: Class<*>) = types.forEach { register(it) }
    fun query(id: Int) : KClass<out Any>? = map[id]
}
