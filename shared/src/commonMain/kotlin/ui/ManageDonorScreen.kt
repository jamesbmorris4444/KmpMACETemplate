package ui

import BloodViewModel
import Strings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.cache.Donor
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ManageDonorScreen(
    navigator: Navigator,
    configAppBar: (AppBarState) -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openDrawer: () -> Unit,
    donor: Donor? = null,
    title: String,
    viewModel: BloodViewModel,
    transitionToCreateProductsScreen: Boolean,
    donateProductsSearchStringName: String,
    createProductsStringName: String
) {
    // state variables
    val showStandardModalState by viewModel.showStandardModalState.collectAsState()

    val stateVertical = rememberScrollState(0)
    Logger.i("MACELOG: launch ManageDonorScreen=${ScreenNames.ManageDonorAfterSearch.name}")
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
                        IconButton(onClick = navigateUp) {
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(state = stateVertical),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val enterFirstNameText = Strings.get("enter_first_name_text")
        val enterMiddleNameText = Strings.get("enter_middle_name_text")
        val enterLastNameText = Strings.get("enter_last_name_text")
        val enterDobText = Strings.get("enter_dob_text")
        val enterBloodTypeText = Strings.get("enter_blood_type_text")
        val enterBranchText = Strings.get("enter_branch_text")
        var currentFirstNameText by remember { mutableStateOf(donor?.firstName ?: "") }
        var currentMiddleNameText by remember { mutableStateOf(donor?.middleName ?: "") }
        var currentLastNameText by remember { mutableStateOf(donor?.lastName ?: "") }
        var currentDobText by remember { mutableStateOf(donor?.dob ?: "") }
        var currentAboRhText by remember { mutableStateOf(donor?.aboRh ?: "") }
        var currentBranchText by remember { mutableStateOf(donor?.branch ?: "") }
        var currentGender by remember { mutableStateOf(true) }
        var databaseModified by remember { mutableStateOf(false) }
        var radioButtonChanged by remember { mutableStateOf(false) }
        var aboRhExpanded by remember { mutableStateOf(false) }
        var branchExpanded  by remember { mutableStateOf(false) }

        fun databaseIsReadyToUpdate(donor: Donor?= null): Boolean {
            return donor?.let {
                it.firstName.isNotEmpty() && it.middleName.isNotEmpty() && it.lastName.isNotEmpty() && it.dob.isNotEmpty() && it.aboRh.isNotEmpty() && it.branch.isNotEmpty()
            } ?: run {
                currentFirstNameText.isNotEmpty() && currentMiddleNameText.isNotEmpty() && currentLastNameText.isNotEmpty() &&
                        currentDobText.isNotEmpty() && currentAboRhText.isNotEmpty() && currentBranchText.isNotEmpty()
            }
        }
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
            if (transitionToCreateProductsScreen.not()) {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Row {
                    StandardEditText(testTag = "otf_last_name", value = currentLastNameText, onValueChange = { currentLastNameText = it ; databaseModified = true }, label = enterLastNameText)
                }
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Row {
                StandardEditText(testTag = "otf_first_name", value = currentFirstNameText, onValueChange = { currentFirstNameText = it ; databaseModified = true }, label = enterFirstNameText)
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Row {
                StandardEditText(testTag = "otf_middle_name", value = currentMiddleNameText, onValueChange = { currentMiddleNameText = it ; databaseModified = true }, label = enterMiddleNameText)
            }
            if (transitionToCreateProductsScreen.not()) {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Row {
                    StandardEditText(testTag = "otf_dob", value = currentDobText, onValueChange = { currentDobText = it ; databaseModified = true }, label = enterDobText)
                }
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Row {
                HorizontalRadioButtons(donor?.gender ?: true) { text ->
                    currentGender = text == "Male"
                    radioButtonChanged = true
                }
            }
            ExposedDropdownMenuBox(
                expanded = aboRhExpanded,
                onExpandedChange = {
                    aboRhExpanded = !aboRhExpanded
                }
            ) {
                StandardEditText(
                    testTag = "otf_aborh",
                    value = currentAboRhText,
                    onValueChange = { currentAboRhText = it ; databaseModified = true },
                    label = enterBloodTypeText,
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = branchExpanded) }
                )
                ExposedDropdownMenu(
                    expanded = aboRhExpanded,
                    onDismissRequest = { aboRhExpanded = false }
                ) {
                    val aboRhArray = listOf(
                        "O-Negative",
                        "O-Positive",
                        "A-Negative",
                        "A-Positive",
                        "B-Negative",
                        "B-Positive",
                        "AB-Negative",
                        "AB-Positive"
                    )
                    aboRhArray.forEach { label ->
                        DropdownMenuItem(
                            modifier = Modifier.background(MaterialTheme.colors.primary),
                            onClick = {
                                aboRhExpanded = false
                                currentAboRhText = label
                                databaseModified = true
                            }
                        ) {
                            Text(
                                text = label,
                                color = MaterialTheme.colors.onPrimary,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            ExposedDropdownMenuBox(
                expanded = branchExpanded,
                onExpandedChange = {
                    branchExpanded = !branchExpanded
                }
            ) {
                StandardEditText(
                    testTag = "otf_branch",
                    value = currentBranchText,
                    onValueChange = { currentBranchText = it ; databaseModified = true },
                    label = enterBranchText,
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = branchExpanded) }
                )
                ExposedDropdownMenu(
                    expanded = branchExpanded,
                    onDismissRequest = { branchExpanded = false }
                ) {
                    val branchArray = listOf(
                        "The Army",
                        "The Marines",
                        "The Navy",
                        "The Coast Guard",
                        "The Air Force",
                        "The JCS"
                    )
                    branchArray.forEach { label ->
                        DropdownMenuItem(
                            modifier = Modifier.background(MaterialTheme.colors.primary),
                            onClick = {
                                branchExpanded = false
                                currentBranchText = label
                                databaseModified = true
                            }
                        ) {
                            Text(
                                text = label,
                                color = MaterialTheme.colors.onPrimary,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            }
            WidgetButton(
                padding = PaddingValues(top = 16.dp, bottom = 24.dp),
                enabled = databaseIsReadyToUpdate(),
                onClick = {
                    if ((databaseModified || radioButtonChanged)) {
                        if (databaseIsReadyToUpdate(donor)) {
                            if (transitionToCreateProductsScreen) {
                                viewModel.updateDonor(
                                    firstName = currentFirstNameText,
                                    middleName = currentMiddleNameText,
                                    lastName = currentLastNameText,
                                    dob = currentDobText,
                                    aboRh = currentAboRhText,
                                    branch = currentBranchText,
                                    gender = currentGender,
                                    id = donor?.id ?: 1
                                )
                            } else {
                                viewModel.insertDonorIntoDatabase(Donor(
                                    id = 0,
                                    firstName = currentFirstNameText,
                                    middleName = currentMiddleNameText,
                                    lastName = currentLastNameText,
                                    dob = currentDobText,
                                    aboRh = currentAboRhText,
                                    branch = currentBranchText,
                                    gender = currentGender
                                ))
                            }
                            viewModel.showStandardModalState.value =  StandardModalArgs(
                                topIconId = "drawable/notification.xml",
                                titleText = Strings.get("made_db_entries_title_text"),
                                bodyText = Strings.get("made_db_entries_body_text"),
                                positiveText = Strings.get("positive_button_text_ok")
                            ) {
                                if (transitionToCreateProductsScreen) {
                                    navigator.navigate(createProductsStringName, NavOptions(popUpTo = PopUpTo(donateProductsSearchStringName, inclusive = false)))
                                } else {
                                    navigator.popBackStack()
                                }
                                viewModel.showStandardModalState.value = StandardModalArgs()
                            }
                        } else {
                            viewModel.showStandardModalState.value = StandardModalArgs(
                                topIconId = "drawable/notification.xml",
                                titleText = Strings.get("made_db_entries_title_text"),
                                bodyText = Strings.get("not_made_db_entries_body_text"),
                                positiveText = Strings.get("positive_button_text_ok")
                            ) {
                                if (transitionToCreateProductsScreen) {
                                    navigator.navigate(createProductsStringName, NavOptions(popUpTo = PopUpTo(donateProductsSearchStringName, inclusive = false)))
                                } else {
                                    navigator.popBackStack()
                                }
                                viewModel.showStandardModalState.value = StandardModalArgs()
                            }
                        }
                    } else {
                        viewModel.showStandardModalState.value = StandardModalArgs(
                            topIconId = "drawable/notification.xml",
                            titleText = Strings.get("no_db_entries_title_text"),
                            positiveText = Strings.get("positive_button_text_ok")
                        ) {
                            if (transitionToCreateProductsScreen) {
                                navigator.navigate(createProductsStringName, NavOptions(popUpTo = PopUpTo(donateProductsSearchStringName, inclusive = false)))
                            } else {
                                navigator.popBackStack()
                            }
                            viewModel.showStandardModalState.value = StandardModalArgs()
                        }
                    }
                },
                buttonText = Strings.get("update_button_text")
            )
        }
    }
}

@Composable
fun HorizontalRadioButtons(isMale: Boolean, setRadioButton: (text: String) -> Unit) {
    val radioOptions = listOf("Male", "Female")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[if (isMale) 0 else 1]) }
    Row {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .height(60.dp)
                    .testTag("RadioButton")
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colors.primary,
                        unselectedColor = MaterialTheme.colors.onBackground
                    ),
                    onClick = {
                        onOptionSelected(text)
                        setRadioButton(text)
                    }
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = text,
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}