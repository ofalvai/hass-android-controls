package com.example.hasscontrolsprovider.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val primaryColor = Color(0xFF03A9F4)
val primaryVariantColor = Color(0xFF0288D1)
val onBackgroundTextColor = Color(0xFF212121)
val iconDefaultColor = Color(0xFF44739E)
val iconEnabledColor = Color(0xFFFDD835)
val secondaryTextColor = Color(0xFF727272)

@Composable
fun HassTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorPalette = if (darkTheme) {
        darkColors(
            primary = primaryColor,
            primaryVariant = primaryVariantColor
        )
    } else {
        lightColors(
            primary = primaryColor,
            primaryVariant = primaryVariantColor,
            onBackground = onBackgroundTextColor
        )
    }

    MaterialTheme(colors = colorPalette) {
        content()
    }
}