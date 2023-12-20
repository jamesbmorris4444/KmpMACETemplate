package com
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Typeface
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.ajalt.colormath.extensions.android.composecolor.toComposeColor
import com.github.ajalt.colormath.model.RGB
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Typeface

@get:Composable
actual val Colors.extraBlack: Color
    get() = RGB("#000000").toComposeColor()
@get:Composable
actual val Colors.extraWhite: Color
    get() = RGB("#ffffff").toComposeColor()
@get:Composable
actual val Colors.extraPrimary: Color
    get() = RGB("#6200ee").toComposeColor()


private fun loadCustomFont(name: String): Typeface {
    return when (name) {
        "avenir_regular" -> Typeface.makeFromName(name, FontStyle.NORMAL)
        "avenir_bold" -> Typeface.makeFromName(name, FontStyle.BOLD)
        "avenir_book" -> Typeface.makeFromName(name, FontStyle.ITALIC)
        else ->  Typeface.makeFromName(name, FontStyle.NORMAL)
    }
}

actual val avenirFontFamilyRegular: FontFamily = FontFamily(Typeface(loadCustomFont("avenir_regular")))
actual val avenirFontFamilyBold: FontFamily = FontFamily(Typeface(loadCustomFont("avenir_bold")))
actual val avenirFontFamilyMedium: FontFamily = FontFamily(Typeface(loadCustomFont("avenir_medium")))

actual val shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

actual val typography = Typography(
    h1 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 32.sp),
    h2 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 24.sp),
    h3 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 20.sp),
    h4 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 16.sp),
    h5 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 12.sp),
    h6 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 10.sp),
    body1 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 18.sp),
    body2 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 16.sp),
    subtitle1 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 14.sp),
    subtitle2 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 12.sp),
    button = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 20.sp),
    caption = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 24.sp),
    overline = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 14.sp)
)

actual val darkColorPalette = darkColors(
//    primary = RGB("#6200ee").toComposeColor(),
//    primaryVariant = RGB("#3700b3").toComposeColor(),
//    secondary = RGB("#03dac6").toComposeColor(),
//    secondaryVariant = RGB("#018786").toComposeColor(),
//    background = RGB("#ffffff").toComposeColor(),
//    surface = RGB("#ffffff").toComposeColor(),
//    error = RGB("#b00020").toComposeColor(),
//    onPrimary = RGB("#ffffff").toComposeColor(),
//    onSecondary = RGB("#000000").toComposeColor(),
//    onBackground = RGB("#000000").toComposeColor(),
//    onSurface = RGB("#000000").toComposeColor(),
//    onError = RGB("#ffffff").toComposeColor(),
)

actual val lightColorPalette = lightColors(
//    primary = RGB("#6200ee").toComposeColor(),
//    primaryVariant = RGB("#3700b3").toComposeColor(),
//    secondary = RGB("#03dac6").toComposeColor(),
//    secondaryVariant = RGB("#018786").toComposeColor(),
//    background = RGB("#ffffff").toComposeColor(),
//    surface = RGB("#ffffff").toComposeColor(),
//    error = RGB("#b00020").toComposeColor(),
//    onPrimary = RGB("#ffffff").toComposeColor(),
//    onSecondary = RGB("#000000").toComposeColor(),
//    onBackground = RGB("#000000").toComposeColor(),
//    onSurface = RGB("#000000").toComposeColor(),
//    onError = RGB("#ffffff").toComposeColor(),
)

@Composable
actual fun MaceTemplateTheme(
    darkTheme: Boolean ,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorPalette
    } else {
        lightColorPalette
    }
    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}