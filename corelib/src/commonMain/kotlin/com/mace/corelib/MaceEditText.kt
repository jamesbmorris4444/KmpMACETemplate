import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun MaceEditText(
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
        label = { MaceText(text = label, color = MaterialTheme.colors.primary, style = MaterialTheme.typography.body2) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = trailingIcon
    )
}

