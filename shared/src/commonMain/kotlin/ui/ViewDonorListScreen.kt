package ui
import BloodViewModel
import Strings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts
import extraBlack
import extraWhite
import utils.Utils

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun ViewDonorListScreen(
    viewModel: BloodViewModel,
    title: String,
    configAppBar: (AppBarState) -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openDrawer: () -> Unit
) {
    val noValue = viewModel.noValue
    val aboRhArray = listOf(
        noValue,
        "O-Negative",
        "O-Positive",
        "A-Negative",
        "A-Positive",
        "B-Negative",
        "B-Positive",
        "AB-Negative",
        "AB-Positive"
    )

    // state variables
    var donorsAndProducts: List<DonorWithProducts> by remember { mutableStateOf(listOf()) }
    var lastNameTextEntered by remember { mutableStateOf("") }
    var aboRhTextState by remember { mutableStateOf(noValue) }

    @Composable
    fun DonorsAndProductsList(donorsAndProducts: List<DonorWithProducts>) {
        LazyColumn {
            items(items = donorsAndProducts, itemContent = { donorWithProductsLocal ->
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(PaddingValues(start = 24.dp, end = 24.dp))
                ) {
                    ListDisplayText("item_lastname", Strings.get("last_name"), donorWithProductsLocal.donor.lastName)
                    ListDisplayText("item_middleName", Strings.get("middle_name"), donorWithProductsLocal.donor.middleName)
                    ListDisplayText("item_firstName", Strings.get("first_name"), donorWithProductsLocal.donor.firstName)
                    ListDisplayText("item_gender", Strings.get("gender"), if (donorWithProductsLocal.donor.gender) "Male" else "Female")
                    ListDisplayText("item_dob", Strings.get("dob"), donorWithProductsLocal.donor.dob)
                    ListDisplayText("item_abo_rh", Strings.get("abo_rh"), donorWithProductsLocal.donor.aboRh)
                    ListDisplayText("item_branch", Strings.get("branch"), donorWithProductsLocal.donor.branch)
                    if (donorWithProductsLocal.products.isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(top = 4.dp))
                        Divider(color = MaterialTheme.colors.error, thickness = 2.dp)
                    }
                    donorWithProductsLocal.products.forEachIndexed { index, product ->
                        Column(modifier = Modifier
                            .height(IntrinsicSize.Min)
                        ) {
                            if (index > 0) {
                                Spacer(modifier = Modifier.padding(top = 16.dp))
                            }
                            ListDisplayText("item_din", Strings.get("din"), product.din)
                            ListDisplayText("item_abo_rh", Strings.get("abo_rh"), product.aboRh)
                            ListDisplayText("item_product_code", Strings.get("product_code"), product.productCode)
                            ListDisplayText("item_expiration", Strings.get("expiration"), product.expirationDate)
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
            })
        }
    }

    fun handleNameOrAboRhTextEntry(lastNameSearchKey: String, aboRhSearchKey: String) {
        val donorAndProductsEntries = viewModel.donorAndProductsList(lastNameSearchKey)
        val finalResultList = if (aboRhSearchKey == aboRhArray[0]) {
            donorAndProductsEntries
        } else {
            donorAndProductsEntries.filter { finalDonorWithProducts -> Utils.donorBloodTypeComparisonByString(finalDonorWithProducts.donor) == aboRhSearchKey }
        }
        donorsAndProducts = finalResultList.sortedBy { it.donor.lastName }
    }

    LaunchedEffect(key1 = true) {
        Logger.i("MACELOG: launch ViewDonorList Screen=${ScreenNames.ViewDonorList.name}")
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

    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        var aboRhExpanded by remember { mutableStateOf(false) }
        Row {
            StandardEditText(
                testTag = "otf_last_name",
                value = lastNameTextEntered,
                onValueChange = { lastNameTextEntered = it },
                label = Strings.get("donor_search_view_donor_list_text"),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        handleNameOrAboRhTextEntry(lastNameTextEntered, aboRhTextState)
                    }
                )
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        ExposedDropdownMenuBox(
            expanded = aboRhExpanded,
            onExpandedChange = {
                aboRhExpanded = !aboRhExpanded
            }
        ) {
            StandardEditText(testTag = "otf_abo_rh", value = aboRhTextState, onValueChange = { }, label = Strings.get("enter_blood_type_text"))
            ExposedDropdownMenu(
                expanded = aboRhExpanded,
                onDismissRequest = { aboRhExpanded = false }
            ) {
                aboRhArray.forEach { label ->
                    DropdownMenuItem(
                        modifier = Modifier.background(MaterialTheme.colors.secondary),
                        onClick = {
                            aboRhExpanded = false
                            aboRhTextState = label
                            handleNameOrAboRhTextEntry(lastNameTextEntered, aboRhTextState)
                        }
                    ) {
                        Text(
                            text = label,
                            color = MaterialTheme.colors.extraWhite,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        if (donorsAndProducts.isNotEmpty()) {
            Divider(color = MaterialTheme.colors.extraBlack, thickness = 2.dp)
        }
        DonorsAndProductsList(donorsAndProducts)
    }
}