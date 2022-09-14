package de.brueggenthies.leinwand.ui

import androidx.compose.ui.Alignment

internal fun lerp(alignment1: Alignment, alignment2: Alignment, fraction: Float): Alignment {
    return Alignment { size, space, layoutDirection ->
        alignment1.align(size, space, layoutDirection) * (1f - fraction) + alignment2.align(size, space, layoutDirection) * fraction
    }
}
