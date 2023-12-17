package ui

import BloodViewModel
import MaceEditText
import Strings
import MaceButton
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.cache.Donor

@Composable
fun DonateProductsScreen(
    configAppBar: (AppBarState) -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openDrawer: () -> Unit,
    onItemButtonClicked: (donor: Donor) -> Unit,
    viewModel: BloodViewModel,
    title: String
) {
    viewModel.initializeDatabase()
    DonateProductsHandler(
        configAppBar = configAppBar,
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp,
        openDrawer = openDrawer,
        viewModel = viewModel,
        title = title,
        onItemButtonClicked = onItemButtonClicked
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DonateProductsHandler(
    configAppBar: (AppBarState) -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openDrawer: () -> Unit,
    viewModel: BloodViewModel,
    title: String,
    onItemButtonClicked: (donor: Donor) -> Unit
) {
    // state variables
    val foundDonors by viewModel.donorsAvailableState.collectAsState()

    fun handleSearchClick(searchKey: String) {
        viewModel.donorsAvailableState.value = viewModel.handleSearchClick(searchKey)
    }

    @Composable
    fun DonorList(donors: List<Donor>, onItemButtonClicked: (donor: Donor) -> Unit) {
        Spacer(modifier = Modifier.height(4.dp))
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
                Divider(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp), color = colors.onBackground, thickness = 2.dp)
            }
        }
    }

    Logger.i("MACELOG: launch DonateProductsScreen=$title")

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
            val keyboardController = LocalSoftwareKeyboardController.current
            var text by remember { mutableStateOf("") }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                MaceEditText(
                    testTag = "otf_last_name",
                    value = text,
                    onValueChange = { text = it },
                    label = Strings.get("initial_letters_of_last_name_text"),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            handleSearchClick(text)
                        }
                    )
                )
            }
            MaceButton(
                padding = PaddingValues(top = 16.dp),
                onClick = {
                    keyboardController?.hide()
                    handleSearchClick(text)
                },
                enabled = text.isNotEmpty(),
                buttonText = Strings.get("search_button_text")
            )
            MaceButton(
                padding = PaddingValues(top = 16.dp),
                onClick = {
                    onItemButtonClicked(viewModel.emptyDonor)
                },
                buttonText = Strings.get("new_donor_button_text")
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (foundDonors.isNotEmpty()) {
                Divider(color = colors.onBackground, thickness = 2.dp)
            }
            DonorList(foundDonors, onItemButtonClicked)
        }
    }
}

@Composable
fun DonorElementText(
    donorFirstName: String,
    donorMiddleName: String,
    donorLastName: String,
    dob: String,
    aboRh: String,
    branch: String,
    gender: Boolean
) {
    ListDisplayText("item_lastname", Strings.get("last_name"), donorLastName)
    ListDisplayText("item_middleName", Strings.get("middle_name"), donorMiddleName)
    ListDisplayText("item_firstName", Strings.get("first_name"), donorFirstName)
    ListDisplayText("item_gender", Strings.get("gender"), if (gender) "Male" else "Female")
    ListDisplayText("item_dob", Strings.get("date_of_birth"), dob)
    ListDisplayText("item_abo_rh", Strings.get("abo_rh"), aboRh)
    ListDisplayText("item_branch", Strings.get("branch"), branch)
}
