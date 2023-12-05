
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

private val purple000 = RGB("#eea6fc").toComposeColor()
private val purple200 = RGB("#bb86fc").toComposeColor()
private val purple500 = RGB("#6200ee").toComposeColor()
private val purple700 = RGB("#3700b3").toComposeColor()
private val black = RGB("#000000").toComposeColor()
private val white = RGB("#ffffff").toComposeColor()
private val red = RGB("#ff0000").toComposeColor()

@get:Composable
actual val Colors.extraRed: Color
    get() = RGB("#ff0000").toComposeColor()
@get:Composable
actual val Colors.extraGreen: Color
    get() = RGB("#00ff00").toComposeColor()
@get:Composable
actual val Colors.extraBlue: Color
    get() = RGB("#0000ff").toComposeColor()
@get:Composable
actual val Colors.extraMagenta: Color
    get() = RGB("#ff00ff").toComposeColor()
@get:Composable
actual val Colors.extraCyan: Color
    get() = RGB("#00ffff").toComposeColor()
@get:Composable
actual val Colors.extraYellow: Color
    get() = RGB("#ffff00").toComposeColor()
@get:Composable
actual val Colors.extraBlack: Color
    get() = RGB("#000000").toComposeColor()
@get:Composable
actual val Colors.extraWhite: Color
    get() = RGB("#ffffff").toComposeColor()


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
    h3 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 21.sp),
    h4 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 16.sp),
    h5 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 13.sp),
    h6 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 11.sp),
    body1 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 20.sp),
    body2 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 18.sp),
    subtitle1 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 16.sp),
    subtitle2 = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 14.sp),
    button = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 20.sp),
    caption = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 24.sp),
    overline = TextStyle(fontFamily = avenirFontFamilyRegular, fontSize = 14.sp)
)

actual val darkColorPalette = darkColors(
    primary = purple700,
    primaryVariant = purple500,
    secondary = purple500,
    secondaryVariant = purple200,
    background = white,
    surface = white,
    error = red,
    onPrimary = white,
    onSecondary = white,
    onBackground = black,
    onSurface = black,
    onError = white
)

actual val lightColorPalette = lightColors(
    primary = purple700,
    primaryVariant = purple500,
    secondary = purple500,
    secondaryVariant = purple200,
    background = white,
    surface = white,
    error = red,
    onPrimary = white,
    onSecondary = white,
    onBackground = black,
    onSurface = black,
    onError = white
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