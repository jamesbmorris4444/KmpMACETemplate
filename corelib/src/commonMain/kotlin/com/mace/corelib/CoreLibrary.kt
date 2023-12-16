import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun StandardEditText(
    modifier: Modifier = Modifier,
    testTag: String, value: String,
    onValueChange: (String) -> Unit,
    label: String,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,

    ) {
    OutlinedTextField(
        modifier = modifier
            .height(72.dp)
            .testTag(testTag),
        value = value,
        readOnly = readOnly,
        textStyle = TextStyle(
            color = MaterialTheme.colors.primary,
            fontSize = MaterialTheme.typography.body2.fontSize
        ),
        onValueChange = onValueChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.primary
        ),
        shape = MaterialTheme.shapes.medium,
        label = { Text(label, color = MaterialTheme.colors.primary, style = MaterialTheme.typography.body2) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = trailingIcon
    )
}

@Composable
fun progressBar() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = MaterialTheme.colors.primary,
            strokeWidth = 6.dp
        )
    }
}

@Composable
fun WidgetButton(padding: PaddingValues, buttonText: String, enabled: Boolean = true, onClick: () -> Unit) {
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
        Text(
            text = buttonText,
            style = MaterialTheme.typography.body1
        )
    }
}