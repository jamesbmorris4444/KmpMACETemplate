package ui
import Repository
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
    repository: Repository,
    title: String,
    configAppBar: (AppBarState) -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openDrawer: () -> Unit
) {
    val donorsAndProducts: MutableState<List<DonorWithProducts>> = remember { mutableStateOf(listOf()) }
    var nameConstraint by remember { mutableStateOf("") }
    val bloodTypeList = listOf(
        "NO VALUE",
        "O-Negative",
        "O-Positive",
        "A-Negative",
        "A-Positive",
        "B-Negative",
        "B-Positive",
        "AB-Negative",
        "AB-Positive"
    )
    val aboRhArray: MutableState<List<String>> = remember { mutableStateOf(bloodTypeList) }
    var aboRhConstraint by remember { mutableStateOf(aboRhArray.value[0]) }

    @Composable
    fun DonorsAndProductsList(donorsAndProducts: MutableState<List<DonorWithProducts>>) {
        LazyColumn {
            items(items = donorsAndProducts.value, itemContent = { donorWithProductsLocal ->
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

    fun handleNameOrAboRhTextEntry() {
        val donorAndProductsEntries = repository.donorAndProductsList()
        val resultList = donorAndProductsEntries.map { mainDonorWithProducts ->
            donorAndProductsEntries.firstOrNull {
                stagingDonorWithProducts -> Utils.donorComparisonByString(mainDonorWithProducts.donor) == Utils.donorComparisonByString(stagingDonorWithProducts.donor)
            } ?: mainDonorWithProducts
        }

        val nameResultList = resultList.filter { finalDonorWithProducts -> Utils.donorLastNameComparisonByString(finalDonorWithProducts.donor).lowercase().startsWith(nameConstraint) }
        val finalResultList = if (aboRhConstraint == aboRhArray.value[0]) {
            nameResultList
        } else {
            nameResultList.filter { finalDonorWithProducts -> Utils.donorBloodTypeComparisonByString(finalDonorWithProducts.donor) == aboRhConstraint }
        }
        donorsAndProducts.value = finalResultList.sortedBy { it.donor.lastName }
    }

    LaunchedEffect(key1 = true) {
        Logger.d("MACELOG: launch ViewDonorList Screen=${ScreenNames.ViewDonorList.name}")
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
        var lastNameTextEntered by rememberSaveable { mutableStateOf("") }
        var aboRhText by rememberSaveable { mutableStateOf(aboRhArray.value[0]) }
        var aboRhExpanded by remember { mutableStateOf(false) }
        val donorSearchStringText = Strings.get("donor_search_view_donor_list_text")
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            OutlinedTextField(
                modifier = Modifier
                    .height(60.dp),
                value = lastNameTextEntered,
                onValueChange = {
                    lastNameTextEntered = it
                },
                shape = RoundedCornerShape(10.dp),
                label = { Text(donorSearchStringText) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        nameConstraint = lastNameTextEntered
                        handleNameOrAboRhTextEntry()
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
                value = aboRhText,
                readOnly = true,
                onValueChange = {
                    aboRhText = it
                },
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
                aboRhArray.value.forEach { label ->
                    DropdownMenuItem(
                        modifier = Modifier.background( MaterialTheme.colors.extraWhite),
                        onClick = {
                            aboRhExpanded = false
                            aboRhText = label
                            aboRhConstraint = aboRhText
                            handleNameOrAboRhTextEntry()
                        }
                    ) {
                        Text(
                            text = label
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        if (donorsAndProducts.value.isNotEmpty()) {
            Divider(color = MaterialTheme.colors.extraBlack, thickness = 2.dp)
        }
        DonorsAndProductsList(donorsAndProducts)
    }
}