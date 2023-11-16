
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import kotlinx.serialization.json.JsonNull.content

@get:Composable
expect val Colors.extraRed: Color
@get:Composable
expect val Colors.extraGreen: Color
@get:Composable
expect val Colors.extraBlue: Color
@get:Composable
expect val Colors.extraMagenta: Color
@get:Composable
expect val Colors.extraCyan: Color
@get:Composable
expect val Colors.extraYellow: Color
@get:Composable
expect val Colors.extraBlack: Color
@get:Composable
expect val Colors.extraWhite: Color

expect val avenirFontFamilyRegular: FontFamily
expect val avenirFontFamilyBold: FontFamily
expect val avenirFontFamilyMedium: FontFamily

expect val shapes: Shapes

expect val typography: Typography

expect val darkColorPalette: Colors

expect val lightColorPalette: Colors

@Composable
expect fun MaceTemplateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
)