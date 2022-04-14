package de.brueggenthies.leinwand.core.internal

import androidx.media3.common.C
import de.brueggenthies.leinwand.core.VideoSize
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal typealias ExoVideoSize = androidx.media3.common.VideoSize

internal fun Long.durationOrNull(): Duration? = if (this == C.TIME_UNSET) null else this.milliseconds

internal fun ExoVideoSize.asSize(): VideoSize? = if (height == 0 && width == 0) null else VideoSize(width, height, unappliedRotationDegrees)
