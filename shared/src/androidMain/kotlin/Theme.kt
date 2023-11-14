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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.ajalt.colormath.extensions.android.composecolor.toComposeColor
import com.github.ajalt.colormath.model.RGB
import com.mace.kmpmacetemplate.R

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

actual val avenirFontFamilyRegular: FontFamily = FontFamily(Font(R.font.avenir_regular))
actual val avenirFontFamilyBold: FontFamily = FontFamily(Font(R.font.avenir_bold, FontWeight.Bold))
actual val avenirFontFamilyMedium: FontFamily = FontFamily(Font(R.font.avenir_book))

actual val shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp)
)

actual val typography = Typography(
    h1 = TextStyle(fontFamily = avenirFontFamilyRegular),
    h2 = TextStyle(fontFamily = avenirFontFamilyRegular),
    h3 = TextStyle(fontFamily = avenirFontFamilyRegular),
    h4 = TextStyle(fontFamily = avenirFontFamilyRegular),
    h5 = TextStyle(fontFamily = avenirFontFamilyRegular),
    h6 = TextStyle(fontFamily = avenirFontFamilyRegular),
    body1 = TextStyle(fontFamily = avenirFontFamilyRegular),
    body2 = TextStyle(fontFamily = avenirFontFamilyRegular),
    subtitle1 = TextStyle(fontFamily = avenirFontFamilyRegular),
    subtitle2 = TextStyle(fontFamily = avenirFontFamilyRegular),
    button = TextStyle(fontFamily = avenirFontFamilyRegular),
    caption = TextStyle(fontFamily = avenirFontFamilyRegular),
    overline = TextStyle(fontFamily = avenirFontFamilyRegular)
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