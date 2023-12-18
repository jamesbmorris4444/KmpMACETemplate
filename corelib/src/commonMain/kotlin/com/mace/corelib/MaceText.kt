
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MaceText(
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    text: String = "",
    style: TextStyle = MaterialTheme.typography.body2,
    color: Color = Color.Unspecified,
    fontFamily: FontFamily = avenirFontFamilyRegular
) {
    Text(
        modifier = modifier,
        textAlign = textAlign,
        text = text,
        style = style,
        color = color,
        fontFamily = fontFamily
    )
}

@Composable
fun MaceAnnotatedText(
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    text: AnnotatedString= AnnotatedString(""),
    style: TextStyle = MaterialTheme.typography.body2,
    color: Color = Color.Unspecified,
    fontFamily: FontFamily = avenirFontFamilyRegular
) {
    Text(
        modifier = modifier,
        textAlign = textAlign,
        text = text,
        style = style,
        color = color,
        fontFamily = fontFamily
    )
}