package com.boltic28.coroutinescompose.elements.buttons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.boltic28.coroutinescompose.ui.theme.*

sealed class ButtonStyle(
    val height: Dp = 40.dp,
    val width: Dp = 80.dp,
    val defaultBackground: Color = Gray,
    val pressedBackground: Color = Black40,
    val disabledBackground: Color = Black20,
    val defaultText: Color = Black100,
    val disabledText: Color = Black20,
    val cornerRadius: Dp = 0.dp,
    val horizontalPadding: Dp = 12.dp,
    val verticalPadding: Dp = 6.dp
) {
    object Blue : ButtonStyle(
        defaultText = LightWhite100,
        defaultBackground = com.boltic28.coroutinescompose.ui.theme.Blue,
        height = 60.dp
    )

    object MaxWidthBlue : ButtonStyle(
        defaultText = LightWhite100,
        defaultBackground = com.boltic28.coroutinescompose.ui.theme.Blue,
        height = 60.dp,
        width = 420.dp
    )
    object MaxWidthRed : ButtonStyle(
        defaultText = LightWhite100,
        defaultBackground = com.boltic28.coroutinescompose.ui.theme.Red,
        height = 60.dp,
        width = 420.dp
    )

    object Green : ButtonStyle(
        defaultBackground = com.boltic28.coroutinescompose.ui.theme.Green
    )
    object Red : ButtonStyle(
        defaultBackground = com.boltic28.coroutinescompose.ui.theme.Red
    )
    object Yellow : ButtonStyle(
        defaultBackground = com.boltic28.coroutinescompose.ui.theme.Yellow
    )
    object Remove : ButtonStyle(
        defaultBackground = com.boltic28.coroutinescompose.ui.theme.Red,
        width = 40.dp
    )
}