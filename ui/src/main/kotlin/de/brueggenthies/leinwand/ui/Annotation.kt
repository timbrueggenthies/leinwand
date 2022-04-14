package de.brueggenthies.leinwand.ui

@RequiresOptIn(message = "This API is subject to change")
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY_GETTER,
)
public annotation class ExperimentalVideoPlayerUi