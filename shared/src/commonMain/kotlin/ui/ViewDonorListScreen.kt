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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
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
import extraRed
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
                Spacer(modifier = Modifier.padding(top = 8.dp))
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(PaddingValues(start = 24.dp, end = 24.dp))
                ) {
                    Row {
                        Text(
                            text = "${donorWithProductsLocal.donor.lastName}, ${donorWithProductsLocal.donor.firstName} ${donorWithProductsLocal.donor.middleName} (${if (donorWithProductsLocal.donor.gender) "Male" else "Female"})",
                            color = MaterialTheme.colors.extraBlack,
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Row {
                        Text(
                            text = "DOB:${donorWithProductsLocal.donor.dob}  AboRh:${donorWithProductsLocal.donor.aboRh}  Branch:${donorWithProductsLocal.donor.branch}",
                            color = MaterialTheme.colors.extraBlack,
                            style = MaterialTheme.typography.body1
                        )
                    }
                    if (donorWithProductsLocal.products.isNotEmpty()) {
                        Spacer(modifier = Modifier.padding(top = 8.dp))
                        Divider(color = MaterialTheme.colors.extraRed, thickness = 2.dp)
                    }
                    donorWithProductsLocal.products.forEach { product ->
                        Column(modifier = Modifier
                            .height(IntrinsicSize.Min)
                        ) {
                            Spacer(modifier = Modifier.padding(top = 8.dp))
                            Row {
                                Text(
                                    text = "DIN: ${product.din}   Blood Type: ${product.aboRh}",
                                    color = MaterialTheme.colors.extraBlack,
                                    style = MaterialTheme.typography.body1
                                )
                            }
                            Row {
                                Text(
                                    text = "Product Code: ${product.productCode}    Expires: ${product.expirationDate}",
                                    color = MaterialTheme.colors.extraBlack,
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(top = 8.dp))
                Divider(color = MaterialTheme.colors.extraBlack, thickness = 2.dp)
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
            OutlinedTextField(
                modifier = Modifier
                    .height(60.dp),
                value = lastNameTextEntered,
                onValueChange = {
                    lastNameTextEntered = it
                },
                shape = RoundedCornerShape(10.dp),
                label = { Text(Strings.get("donor_search_view_donor_list_text")) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        handleNameOrAboRhTextEntry(lastNameTextEntered, aboRhTextState)
                    })
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        ExposedDropdownMenuBox(
            expanded = aboRhExpanded,
            onExpandedChange = {
                aboRhExpanded = !aboRhExpanded
            }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .height(60.dp),
                value = aboRhTextState,
                readOnly = true,
                onValueChange = { },
                shape = RoundedCornerShape(10.dp),
                label = { Text(Strings.get("enter_blood_type_text")) },
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = aboRhExpanded
                    )
                }
            )
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
                            style = MaterialTheme.typography.body2
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