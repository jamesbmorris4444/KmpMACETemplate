package com.mace.corelib

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import avenirFontFamilyBold
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

data class StandardModalArgs(
    val topIconId: String = "",
    val titleText: String = "",
    val bodyText: String = "",
    val positiveText: String = "",
    val negativeText: String = "",
    val neutralText: String = "",
    val onDismiss: (DismissSelector) -> Unit = { }
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StandardModal(
    topIconId: String,
    titleText: String = "",
    bodyText: String = "",
    positiveText: String = "",
    negativeText: String = "",
    neutralText: String = "",
    onDismiss: (DismissSelector) -> Unit
) {

    @Composable
    fun TextForButton(text: String, isBackgrounded: Boolean) {
        Text(
            text = text,
            color = if (isBackgrounded) MaterialTheme.colors.surface else  MaterialTheme.colors.primary,
            style = TextStyle(
                fontFamily = avenirFontFamilyBold,
                color = MaterialTheme.colors.onPrimary,
                fontSize = MaterialTheme.typography.body2.fontSize
            )
        )
    }

    var shouldShowDialog by remember { mutableStateOf(true) }
    val numberOfButtons = when {
        negativeText.isEmpty() && neutralText.isEmpty() -> 1
        neutralText.isEmpty() -> 2
        else -> 3
    }
    if (shouldShowDialog) {
        Dialog(
            onDismissRequest = { shouldShowDialog = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp).testTag("StandardModal"),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (topIconId.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(color = MaterialTheme.colors.background),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(top = 22.dp)
                                    .height(160.dp)
                                    .width(120.dp),
                                painter = painterResource(topIconId),
                                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                                contentDescription = "Dialog Alert"
                            )
                        }
                    }

                    if (titleText.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = if (bodyText.isEmpty()) 8.dp else 16.dp),
                            text = titleText,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.secondaryVariant,
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (bodyText.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                            text = bodyText,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.secondaryVariant,
                            style = MaterialTheme.typography.body2
                        )
                    }

                    val positiveButtonTopSpace = 12.dp
                    val positiveTextButtonTopSpace = 8.dp
                    val otherButtonTopSpace = 16.dp
                    val otherTextButtonTopSpace = 8.dp
                    val buttonBottomSpace = 20.dp
                    val textButtonBottomSpace = 24.dp
                    var index = positiveText.indexOf(':')
                    when  {
                        index == 3 && positiveText.substring(0,3) == "BKG" -> {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = positiveButtonTopSpace, start = 36.dp, end = 36.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                                onClick = {
                                    shouldShowDialog = false
                                    onDismiss(DismissSelector.POSITIVE)
                                }
                            ) {
                                TextForButton(positiveText.substring(index + 1), true)
                            }
                            if (numberOfButtons == 1) {
                                Spacer(modifier = Modifier.padding(bottom = buttonBottomSpace))
                            }
                        }
                        else -> {
                            TextButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = positiveTextButtonTopSpace, start = 36.dp, end = 36.dp),
                                onClick = {
                                    shouldShowDialog = false
                                    onDismiss(DismissSelector.POSITIVE)
                                }
                            ) {
                                TextForButton(positiveText, false)
                            }
                            if (numberOfButtons == 1) {
                                Spacer(modifier = Modifier.padding(bottom = textButtonBottomSpace))
                            }
                        }
                    }

                    if (numberOfButtons > 1) {
                        index = negativeText.indexOf(':')
                        when  {
                            index == 3 && negativeText.substring(0,3) == "BKG" -> {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = otherButtonTopSpace, start = 36.dp, end = 36.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                                    onClick = {
                                        shouldShowDialog = false
                                        onDismiss(DismissSelector.NEGATIVE)
                                    }
                                ) {
                                    TextForButton(negativeText.substring(index + 1), true)
                                }
                                if (numberOfButtons == 2) {
                                    Spacer(modifier = Modifier.padding(bottom = buttonBottomSpace))
                                }
                            }
                            else -> {
                                TextButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = otherTextButtonTopSpace, start = 36.dp, end = 36.dp),
                                    onClick = {
                                        shouldShowDialog = false
                                        onDismiss(DismissSelector.NEGATIVE)
                                    }
                                ) {
                                    TextForButton(negativeText, false)
                                }
                                if (numberOfButtons == 2) {
                                    Spacer(modifier = Modifier.padding(bottom = textButtonBottomSpace))
                                }
                            }
                        }
                    }

                    if (numberOfButtons > 2) {
                        index = neutralText.indexOf(':')
                        when  {
                            index == 3 && neutralText.substring(0,3) == "BKG" -> {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = otherButtonTopSpace, start = 36.dp, end = 36.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                                    onClick = {
                                        shouldShowDialog = false
                                        onDismiss(DismissSelector.NEUTRAL)
                                    }
                                ) {
                                    TextForButton(neutralText.substring(index + 1), true)
                                }
                                Spacer(modifier = Modifier.padding(bottom = buttonBottomSpace))
                            }
                            else -> {
                                TextButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = otherTextButtonTopSpace, start = 36.dp, end = 36.dp),
                                    onClick = {
                                        shouldShowDialog = false
                                        onDismiss(DismissSelector.NEUTRAL)
                                    }
                                ) {
                                    TextForButton(neutralText, false)
                                }
                                Spacer(modifier = Modifier.padding(bottom = textButtonBottomSpace))
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class DismissSelector {
    POSITIVE,
    NEGATIVE,
    NEUTRAL
}