package de.brueggenthies.leinwand.ui.core

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset

public enum class ResizeMode {
    Fit,
    FillWidth,
    FillHeight,
    Fill,
    Zoom,
}

internal fun Modifier.resize(
    aspectRatio: Float,
    resizeMode: ResizeMode
) = when (resizeMode) {
    ResizeMode.Fit -> aspectRatio(aspectRatio)
    ResizeMode.Fill -> fillMaxSize()
    ResizeMode.FillWidth -> fixedWidth(aspectRatio)
    ResizeMode.FillHeight -> fixedHeight(aspectRatio)
    ResizeMode.Zoom -> zoom(aspectRatio)
}

private fun Modifier.fixedWidth(
    aspectRatio: Float
) = fillMaxWidth()
    .wrapContentHeight(unbounded = true)
    .aspectRatio(aspectRatio)

private fun Modifier.fixedHeight(
    aspectRatio: Float
) = fillMaxHeight()
    .wrapContentWidth(unbounded = true)
    .aspectRatio(aspectRatio)

private fun Modifier.zoom(
    aspectRatio: Float
) = layout { measurable, constraints ->
        val maxWidth = constraints.maxWidth
        val maxHeight = constraints.maxHeight
        if (aspectRatio > maxWidth.toFloat() / maxHeight) {
            // wrap width unbounded
            val modifiedConstraints = constraints.copy(maxWidth = Constraints.Infinity)
            val placeable = measurable.measure(modifiedConstraints)
            layout(constraints.maxWidth, placeable.height) {
                val offsetX = Alignment.CenterHorizontally
                    .align(0, constraints.maxWidth - placeable.width, layoutDirection)
                placeable.place(IntOffset(offsetX, 0))
            }
        } else {
            // wrap height unbounded
            val modifiedConstraints = constraints.copy(maxHeight = Constraints.Infinity)
            val placeable = measurable.measure(modifiedConstraints)
            layout(placeable.width, constraints.maxHeight) {
                val offsetY = Alignment.CenterVertically
                    .align(0, constraints.maxHeight - placeable.height)
                placeable.place(IntOffset(0, offsetY))
            }
        }
    }
    .aspectRatio(aspectRatio)
