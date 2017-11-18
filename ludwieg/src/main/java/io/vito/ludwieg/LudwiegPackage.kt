package io.vito.ludwieg

/**
 * LudwiegPackage defines a class as the receiver of a given package identification
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LudwiegPackage(val id: Int)
