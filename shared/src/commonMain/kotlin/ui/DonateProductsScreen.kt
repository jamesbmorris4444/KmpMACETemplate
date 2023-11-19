package ui

import BloodViewModel
import Repository
import Strings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.cache.Donor
import kotlinx.coroutines.delay

@Composable
fun DonateProductsScreen(
    repository: Repository,
    configAppBar: (AppBarState) -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openDrawer: () -> Unit,
    onItemButtonClicked: (donor: Donor) -> Unit,
    viewModel: BloodViewModel,
    title: String
) {
    val completed by viewModel.refreshCompletedState.collectAsState()
    val isInvalid by viewModel.databaseInvalidState.collectAsState()
    val showStandardModalState by viewModel.showStandardModalState.collectAsState()
    val failure by viewModel.refreshFailureState.collectAsState()

    when {
        isInvalid -> {
            repository.refreshDonors()
            viewModel.updateRefreshCompletedState(true)
            viewModel.updateDatabaseInvalidState(false)
            viewModel.updateRefreshFailureState("")
        }
        failure.isNotEmpty() -> {
            if (showStandardModalState.topIconId.isNotEmpty()) {
                StandardModal(
                    showStandardModalState.topIconId,
                    showStandardModalState.titleText,
                    showStandardModalState.bodyText,
                    showStandardModalState.positiveText,
                    showStandardModalState.negativeText,
                    showStandardModalState.neutralText,
                    showStandardModalState.onDismiss
                )
            } else {
                viewModel.changeShowStandardModalState(
                    StandardModalArgs(
                        topIconId = "drawable/notification.xml",
                        titleText = Strings.get("failure_db_entries_title_text"),
                        bodyText = Strings.format("failure_db_entries_body_text", failure),
                        positiveText = Strings.get("positive_button_text_ok")
                    ) {
                        navigateUp()
                        viewModel.changeShowStandardModalState(StandardModalArgs())
                        viewModel.updateRefreshFailureState("")
                    }
                )
            }
        }
        completed -> {
            DonateProductsHandler(
                repository,
                configAppBar = configAppBar,
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp,
                openDrawer = openDrawer,
                viewModel = viewModel,
                title = title,
                onItemButtonClicked = onItemButtonClicked)
        }
        else -> {
            viewModel.updateRefreshCompletedState(false)
            viewModel.updateDatabaseInvalidState(true)
            viewModel.updateRefreshFailureState("")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DonateProductsHandler(
    repository: Repository,
    configAppBar: (AppBarState) -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openDrawer: () -> Unit,
    viewModel: BloodViewModel,
    title: String,
    onItemButtonClicked: (donor: Donor) -> Unit
) {
    val foundDonors = viewModel.donorsAvailableState.collectAsState().value
    fun handleSearchClick(searchKey: String) {
        viewModel.updateDonorsAvailableState(repository.handleSearchClick(searchKey))
    }

    @Composable
    fun DonorList(donors: List<Donor>, onItemButtonClicked: (donor: Donor) -> Unit) {
        LazyColumn(
            modifier = Modifier.testTag("LazyColumn")
        ) {
            items(items = donors) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemButtonClicked(it) }
                ) {
                    DonorElementText(
                        it.firstName,
                        it.middleName,
                        it.lastName,
                        it.dob,
                        it.aboRh,
                        it.branch,
                        it.gender
                    )
                }
                Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
            }
        }
    }

    Logger.d("launch DonateProductsScreen=$title    donors=$foundDonors")

    LaunchedEffect(key1 = true) {
        configAppBar(
            AppBarState(
                title = title,
                actions = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = Strings.get("menu_content_description")
                        )
                    }
                },
                navigationIcon = {
                    if (canNavigateBack) {
                        IconButton(onClick = navigateUp.also {  }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = Strings.get("back_button_content_description")
                            )
                        }
                    }
                }
            )
        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(start = 24.dp, end = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val focusRequester = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current
            val text by viewModel.refreshEditTextState.collectAsState()
            Spacer(modifier = Modifier.height(36.dp))
            Row {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(0.7f)
                        .height(60.dp)
                        .focusRequester(focusRequester)
                        .onGloballyPositioned {
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }
                        .testTag("OutlinedTextField"),
                    value = text,
                    onValueChange = {
                        viewModel.refreshEditTextState(it)
                    },
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(Strings.get("initial_letters_of_last_name_text")) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            handleSearchClick(text)
                        })
                )
            }
            WidgetButton(
                padding = PaddingValues(top = 16.dp),
                onClick = {
                    keyboardController?.hide()
                    handleSearchClick(text)
                },
                buttonText = Strings.get("search_button_text")
            )
            WidgetButton(
                padding = PaddingValues(top = 16.dp),
                onClick = {
                    onItemButtonClicked(viewModel.emptyDonor)
                },
                buttonText = Strings.get("new_donor_button_text")
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (foundDonors.isNotEmpty()) {
                Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
            }
            DonorList(foundDonors, onItemButtonClicked)
        }
    }
}