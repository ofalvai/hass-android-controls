package com.example.hasscontrolsprovider.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.HorizontalGradient

// https://github.com/android/compose-samples/blob/7da64ff119990df23bd5a026be8c2757ebbd2a82/Jetsnack/app/src/main/java/com/example/jetsnack/ui/components/Gradient.kt#L42
fun Modifier.horizontalGradientBackground(
    colors: List<Color>
) = drawWithCache {
    // Use drawWithCache modifier to create and cache the gradient once size is known or changes.
    val gradient = HorizontalGradient(
        startX = 0.0f,
        endX = size.width,
        colors = colors
    )
    onDraw {
        drawRect(brush = gradient)
    }
}