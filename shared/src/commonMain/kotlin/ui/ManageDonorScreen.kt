package ui

import BloodViewModel
import Repository
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.cache.Donor
import extraWhite
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManageDonorScreen(
    repository: Repository,
    navigator: Navigator,
    configAppBar: (AppBarState) -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openDrawer: () -> Unit,
    donor: Donor,
    title: String,
    viewModel: BloodViewModel,
    transitionToCreateProductsScreen: Boolean,
    donateProductsSearchStringName: String,
    createProductsStringName: String
) {
    // state variables
    val showStandardModalState by viewModel.showStandardModalState.collectAsState()

    val stateVertical = rememberScrollState(0)
    Logger.d("launch ManageDonorScreen=${ScreenNames.ManageDonorAfterSearch.name}")
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
        val databaseModified by viewModel.databaseModifiedState.collectAsState()
        val radioButtonChanged by viewModel.radioButtonState.collectAsState()
        val aboRhExpanded by viewModel.aboRhExpandedState.collectAsState()
        val branchExpanded by viewModel.branchExpandedState.collectAsState()
        val firstNameText by viewModel.firstNameState.collectAsState()
        val middleNameText by viewModel.middleNameState.collectAsState()
        val lastNameText by viewModel.lastNameState.collectAsState()
        val dobText by viewModel.dobState.collectAsState()
        val aboRhText by viewModel.aboRhState.collectAsState()
        val branchText by viewModel.branchState.collectAsState()
        val gender by viewModel.genderState.collectAsState()
        val enterFirstNameText = Strings.get("enter_first_name_text")
        val enterMiddleNameText = Strings.get("enter_middle_name_text")
        val enterLastNameText = Strings.get("enter_last_name_text")
        val enterDobText = Strings.get("enter_dob_text")
        val enterBloodTypeText = Strings.get("enter_blood_type_text")
        val enterBranchText = Strings.get("enter_branch_text")
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
                    OutlinedTextField(
                        modifier = Modifier
                            .height(60.dp)
                            .testTag("OutlinedTextField"),
                        value = donor.lastName,
                        onValueChange = {
                            viewModel.changeLastNameState(it)
                            viewModel.changeDatabaseModifiedState(true)
                        },
                        shape = RoundedCornerShape(10.dp),
                        label = { Text(enterLastNameText) },
                        singleLine = true
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Row {
                OutlinedTextField(
                    modifier = Modifier
                        .height(60.dp)
                        .testTag("OutlinedTextField"),
                    value = donor.firstName,
                    onValueChange = {
                        viewModel.changeFirstNameState(it)
                        viewModel.changeDatabaseModifiedState(true)
                    },
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(enterFirstNameText) },
                    singleLine = true
                )
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Row {
                OutlinedTextField(
                    modifier = Modifier
                        .height(60.dp)
                        .testTag("OutlinedTextField"),
                    value = donor.middleName,
                    onValueChange = {
                        viewModel.changeMiddleNameState(it)
                        viewModel.changeDatabaseModifiedState(true)
                    },
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(enterMiddleNameText) },
                    singleLine = true
                )
            }
            if (transitionToCreateProductsScreen.not()) {
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Row {
                    OutlinedTextField(
                        modifier = Modifier
                            .height(60.dp)
                            .testTag("OutlinedTextField"),
                        value = donor.dob,
                        onValueChange = {
                            viewModel.changeDobState(it)
                            viewModel.changeDatabaseModifiedState(true)
                        },
                        shape = RoundedCornerShape(10.dp),
                        label = { Text(enterDobText) },
                        singleLine = true
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Row {
                HorizontalRadioButtons(donor.gender) { text ->
                    viewModel.changeGenderState(text == "Male")
                    viewModel.changeRadioButtonState(true)
                }
            }
            ExposedDropdownMenuBox(
                expanded = aboRhExpanded,
                onExpandedChange = {
                    viewModel.changeAboRhExpandedState(!aboRhExpanded)
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .height(60.dp)
                        .testTag("OutlinedTextField"),
                    value = donor.aboRh,
                    readOnly = true,
                    onValueChange = {
                        viewModel.changeAboRhState(it)
                    },
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(enterBloodTypeText) },
                    singleLine = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = aboRhExpanded
                        )
                    }
                )
                ExposedDropdownMenu(
                    expanded = aboRhExpanded,
                    onDismissRequest = { viewModel.changeAboRhExpandedState(false) }
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
                            modifier = Modifier.background(MaterialTheme.colors.secondary),
                            onClick = {
                                viewModel.changeAboRhExpandedState(false)
                                viewModel.changeAboRhState(label)
                                viewModel.changeDatabaseModifiedState(true)
                            }
                        ) {
                            Text(
                                text = label,
                                color = MaterialTheme.colors.extraWhite,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(top = 16.dp))
            ExposedDropdownMenuBox(
                expanded = branchExpanded,
                onExpandedChange = {
                    viewModel.changeBranchExpandedState(!branchExpanded)
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .height(60.dp)
                        .testTag("OutlinedTextField"),
                    value = donor.branch,
                    readOnly = true,
                    onValueChange = {
                        viewModel.changeBranchState(it)
                    },
                    shape = RoundedCornerShape(10.dp),
                    label = { Text(enterBranchText) },
                    singleLine = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = branchExpanded
                        )
                    }
                )
                ExposedDropdownMenu(
                    expanded = branchExpanded,
                    onDismissRequest = { viewModel.changeBranchExpandedState(false) }
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
                            modifier = Modifier.background(MaterialTheme.colors.secondary),
                            onClick = {
                                Logger.d("JIMX    label=$label")
                                viewModel.changeBranchExpandedState(false)
                                viewModel.changeBranchState(label)
                                viewModel.changeDatabaseModifiedState(true)
                            }
                        ) {
                            Text(
                                text = label,
                                color = MaterialTheme.colors.extraWhite,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            }
            Logger.d("JIMX    label2=$branchText")
            WidgetButton(
                padding = PaddingValues(top = 16.dp, bottom = 24.dp),
                onClick = {
                    val legalEntry = donor.lastName.isNotEmpty() && donor.dob.isNotEmpty()
                    if ((databaseModified || radioButtonChanged)) {
                        if (legalEntry) {
                            repository.insertDonorIntoDatabase(donor)
                            viewModel.changeShowStandardModalState(
                                StandardModalArgs(
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
                                    viewModel.changeShowStandardModalState(StandardModalArgs())
                                }
                            )
                        } else {
                            viewModel.changeShowStandardModalState(
                                StandardModalArgs(
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
                                    viewModel.changeShowStandardModalState(StandardModalArgs())
                                }
                            )
                        }
                    } else {
                        viewModel.changeShowStandardModalState(
                            StandardModalArgs(
                                topIconId = "drawable/notification.xml",
                                titleText = Strings.get("no_db_entries_title_text"),
                                positiveText = Strings.get("positive_button_text_ok")
                            ) {
                                if (transitionToCreateProductsScreen) {
                                    navigator.navigate(createProductsStringName, NavOptions(popUpTo = PopUpTo(donateProductsSearchStringName, inclusive = false)))
                                } else {
                                    navigator.popBackStack()
                                }
                                viewModel.changeShowStandardModalState(StandardModalArgs())
                            }
                        )
                    }
                    viewModel.changeRadioButtonState(false)
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
                    onClick = {
                        onOptionSelected(text)
                        setRadioButton(text)
                    }
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = text
                )
            }
        }
    }
}