// ktlint-disable filename
package de.brueggenthies.leinwand.ui

import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER

@RequiresOptIn(message = "This API is subject to change")
@Target(CLASS, FUNCTION, PROPERTY, FIELD, PROPERTY_GETTER)
@Retention(BINARY)
public annotation class ExperimentalVideoPlayerUi
