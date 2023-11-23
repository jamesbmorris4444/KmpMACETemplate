package ui

import BloodViewModel
import Repository
import Strings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.cache.Product
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts
import extraBlack
import extraRed

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReassociateDonationScreen(
    onComposing: (AppBarState) -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openDrawer: () -> Unit,
    viewModel: BloodViewModel,
    repository: Repository,
    title: String
) {
    // state variables
    val correctDonorsWithProducts by viewModel.correctDonorsWithProductsState.collectAsState()
    val incorrectDonorsWithProducts by viewModel.incorrectDonorsWithProductsState.collectAsState()
    val correctDonorWithProducts by viewModel.correctDonorWithProductsState.collectAsState()
    val incorrectDonorWithProducts by viewModel.incorrectDonorWithProductsState.collectAsState()
    val singleSelectedProductList by viewModel.singleSelectedProductListState.collectAsState()
    val incorrectDonorSelected  by viewModel.incorrectDonorSelectedState.collectAsState()
    val isProductSelected by viewModel.isProductSelectedState.collectAsState()
    val isReassociateCompleted by viewModel.isReassociateCompletedState.collectAsState()
    val showStandardModalState by viewModel.showStandardModalState.collectAsState()

    val reassociateDonationSearchStringName = ScreenNames.ReassociateDonation.name
    val keyboardController = LocalSoftwareKeyboardController.current

    fun handleSearchClickWithProducts(isCorrectDonorProcessing: Boolean, searchKey: String) {
        if (isCorrectDonorProcessing) {
            viewModel.handleSearchClickWithProductsCorrectDonor(searchKey = searchKey)
        } else {
            viewModel.handleSearchClickWithProductsIncorrectDonor(searchKey = searchKey)
        }
    }

    fun moveProductsToCorrectDonor(correctDonor: Donor) {
        incorrectDonorsWithProducts.map { donorWithProducts ->
            donorWithProducts.products.map { product ->
                if (product.removedForReassociation) {
                    repository.updateDonorIdInProduct(correctDonor.id, product.donorId)
                }
            }
//            repository.insertReassociatedProductsIntoDatabase(correctDonor, donorWithProducts.products)
            viewModel.changeShowStandardModalState(
                StandardModalArgs(
                    topIconId = "drawable/notification.xml",
                    titleText = Strings.get("made_reassociate_entries_body_text"),
                    positiveText = Strings.get("positive_button_text_ok")
                ) {
                    viewModel.changeCorrectDonorWithProductsState(correctDonor)
                    viewModel.changeIsReassociateCompletedState(true)
                    viewModel.changeShowStandardModalState(StandardModalArgs())
                }
            )
        }
    }

    @Composable
    fun DonorListWithProducts(
        isCorrectDonorProcessing: Boolean,
        donorsWithProducts: List<DonorWithProducts>,
        displayForDonor: (DonorWithProducts) -> Boolean,
        enablerForDonor: (DonorWithProducts) -> Boolean,
        enablerForProducts: (Product) -> Boolean
    ) {
        LazyColumn {
            items(items = donorsWithProducts) {
                if (displayForDonor(it)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            enabled = enablerForDonor(it)
                        ) {
                            if (incorrectDonorSelected) {
                                moveProductsToCorrectDonor(it.donor)
                            } else {
                                if (isCorrectDonorProcessing) {
                                    viewModel.changeCorrectDonorsWithProductsState(listOf(it))
                                    viewModel.changeCorrectDonorWithProductsState(it.donor)
                                } else {
                                    viewModel.changeIncorrectDonorsWithProductsState(listOf(it))
                                    viewModel.changeIncorrectDonorWithProductsState(it.donor)
                                    viewModel.changeIncorrectDonorSelectedState(true)
                                }
                            }
                        }
                    ) {
                        DonorElementText(
                            it.donor.firstName,
                            it.donor.middleName,
                            it.donor.lastName,
                            it.donor.dob,
                            it.donor.aboRh,
                            it.donor.branch,
                            it.donor.gender
                        )
                    }
                    Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
                    ProductListScreen(
                        repository = repository,
                        canScrollVertically = false,
                        productList = it.products,
                        useOnProductsChange = false,
                        onProductSelected = { productList ->
                            viewModel.changeSingleSelectedProductListState(productList)
                            viewModel.changeIsProductSelectedState(true)
                        },
                        enablerForProducts = enablerForProducts
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        Logger.d("launch ReassociateDonationScreen=$reassociateDonationSearchStringName")
        onComposing(
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
                        IconButton(onClick = navigateUp.also { viewModel.resetReassociateCompletedScreen() }) {
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
        Logger.d("Compose: ${ScreenNames.ReassociateDonation.name}")
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                isReassociateCompleted -> {
                    // Fourth (Last) run
                    Text(
                        modifier = Modifier.align(Alignment.Start),
                        text = Strings.get("reassociate_complete_title"),
                        color = MaterialTheme.colors.extraBlack,
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = MaterialTheme.colors.extraBlack, thickness = 2.dp)
                    DonorListWithProducts(
                        true,
                        listOf(correctDonorWithProducts),
                        displayForDonor = { true },
                        enablerForDonor = { false },
                        enablerForProducts = { false }
                    )

                }
                showStandardModalState.topIconId.isNotEmpty() -> {
                    StandardModal(
                        showStandardModalState.topIconId,
                        showStandardModalState.titleText,
                        showStandardModalState.bodyText,
                        showStandardModalState.positiveText,
                        showStandardModalState.negativeText,
                        showStandardModalState.neutralText,
                        showStandardModalState.onDismiss
                    )
                }
                else -> {
                    if (incorrectDonorSelected) {
                        // Second run
                        if (isProductSelected) {
                            // Third run
                            Text(
                                modifier = Modifier.align(Alignment.Start),
                                text = Strings.get("incorrect_donor_and_product_title"),
                                color = MaterialTheme.colors.extraBlack,
                                style = MaterialTheme.typography.body1
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = MaterialTheme.colors.extraBlack, thickness = 2.dp)
                            DonorListWithProducts(
                                true,
                                listOf(DonorWithProducts(donor = incorrectDonorWithProducts.donor, products = singleSelectedProductList)),
                                displayForDonor = { true },
                                enablerForDonor = { false },
                                enablerForProducts = { false }
                            )
                        } else {
                            // Second run
                            Text(
                                modifier = Modifier.align(Alignment.Start),
                                text = Strings.get("incorrect_donor_title"),
                                color = MaterialTheme.colors.extraBlack,
                                style = MaterialTheme.typography.body1
                            )
                            Text(
                                modifier = Modifier.align(Alignment.Start),
                                text = Strings.get("choose_product_for_reassociation_title"),
                                color = MaterialTheme.colors.extraRed,
                                style = MaterialTheme.typography.body2
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = MaterialTheme.colors.extraBlack, thickness = 2.dp)
                            DonorListWithProducts(
                                true,
                                incorrectDonorsWithProducts,
                                displayForDonor = { true },
                                enablerForDonor = { false },
                                enablerForProducts = { true }
                            )
                        }
                        if (isProductSelected) {
                            // Third run
                            Spacer(modifier = Modifier.height(4.dp))
                            var text by rememberSaveable { mutableStateOf("") }
                            OutlinedTextField(
                                modifier = Modifier
                                    .height(60.dp),
                                value = text,
                                onValueChange = {
                                    text = it
                                },
                                shape = RoundedCornerShape(10.dp),
                                label = { Text(Strings.get("initial_letters_of_correct_donor_last_name_text")) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                        handleSearchClickWithProducts(true, text)
                                    })
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            if (correctDonorsWithProducts.isNotEmpty()) {
                                Text(
                                    modifier = Modifier.align(Alignment.Start),
                                    text = Strings.get("choose_correct_donor_title"),
                                    color = MaterialTheme.colors.extraRed,
                                    style = MaterialTheme.typography.body2
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            Divider(color = MaterialTheme.colors.extraBlack, thickness = 2.dp)
                            DonorListWithProducts(
                                true,
                                correctDonorsWithProducts,
                                displayForDonor = { true },
                                enablerForDonor = { true },
                                enablerForProducts = { false }
                            )

                        }
                    } else {
                        // First run
                        var text by remember { mutableStateOf("") }
                        OutlinedTextField(
                            modifier = Modifier
                                .height(60.dp),
                            value = text,
                            onValueChange = {
                                text = it
                            },

                            shape = RoundedCornerShape(10.dp),
                            label = { Text(Strings.get("initial_letters_of_incorrect_donor_last_name_text")) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    handleSearchClickWithProducts(false, text)
                                })
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        if (incorrectDonorsWithProducts.isNotEmpty()) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Start),
                                text = Strings.get("choose_incorrect_donor_title"),
                                color = MaterialTheme.colors.extraRed,
                                style = MaterialTheme.typography.body2
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = MaterialTheme.colors.extraBlack, thickness = 2.dp)
                        }
                        DonorListWithProducts(
                            false,
                            incorrectDonorsWithProducts,
                            displayForDonor = { donorWithProducts -> donorWithProducts.products.isNotEmpty() },
                            enablerForDonor = { true },
                            enablerForProducts = { false }
                        )
                    }
                }
            }
        }
    }
}