package ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp

@Composable
fun WidgetButton(padding: PaddingValues, buttonText: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(padding).testTag("WidgetButton"),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            disabledBackgroundColor = MaterialTheme.colors.secondary,
            disabledContentColor =  MaterialTheme.colors.onSecondary,
        ),
        onClick = onClick
    ) {
        Text(
            text = buttonText,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onPrimary
        )
    }
}