package io.vito.ludwieg

/**
 * Serializable identifies a non-package class as a serialization target
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Serializable
