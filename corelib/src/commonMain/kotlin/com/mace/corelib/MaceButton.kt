import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun MaceButton(padding: PaddingValues, buttonText: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(padding).testTag("WidgetButton"),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            disabledBackgroundColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.disabled),
            disabledContentColor =  MaterialTheme.colors.onSecondary,
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        MaceText(
            text = buttonText,
            style = MaterialTheme.typography.body1
        )
    }
}