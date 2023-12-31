package ui

import BloodViewModel
import MaceEditText
import MaceText
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.Strings
import com.avenirFontFamilyBold
import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.cache.Product
import com.jetbrains.handson.kmm.shared.entity.DonorWithProducts
import com.mace.corelib.StandardModal
import com.mace.corelib.StandardModalArgs

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReassociateDonationScreen(
    onComposing: (AppBarState) -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openDrawer: () -> Unit,
    viewModel: BloodViewModel,
    title: String
) {
    // state variables
    var correctDonorsWithProducts: List<DonorWithProducts> by remember { mutableStateOf(listOf()) }
    var incorrectDonorsWithProducts: List<DonorWithProducts> by remember { mutableStateOf(listOf()) }
    var correctDonorWithProducts: DonorWithProducts by remember { mutableStateOf(DonorWithProducts(viewModel.emptyDonor)) }
    var incorrectDonorWithProducts: DonorWithProducts by remember { mutableStateOf(DonorWithProducts(viewModel.emptyDonor)) }
    var singleSelectedProductList: List<Product> by remember { mutableStateOf(listOf()) }
    var incorrectDonorSelected by remember { mutableStateOf(false) }
    var isProductSelected by remember { mutableStateOf(false) }
    var isReassociateCompleted by remember { mutableStateOf(false) }
    var showStandardModalState by remember { mutableStateOf(StandardModalArgs()) }

    val reassociateDonationSearchStringName = ScreenNames.ReassociateDonation.name
    val keyboardController = LocalSoftwareKeyboardController.current

    fun handleSearchClickWithProducts(isCorrectDonorProcessing: Boolean, searchKey: String) {
        if (isCorrectDonorProcessing) {
            correctDonorsWithProducts = viewModel.handleSearchClickWithProductsCorrectDonor(searchKey = searchKey)
        } else {
            incorrectDonorsWithProducts = viewModel.handleSearchClickWithProductsIncorrectDonor(searchKey = searchKey)
        }
    }

    fun moveProductsToCorrectDonor(correctDonor: Donor) {
        val product = singleSelectedProductList[0]
        viewModel.updateDonorIdInProduct(correctDonor.id, product.id)
        showStandardModalState = StandardModalArgs(
            topIconId = "drawable/notification.xml",
            titleText = Strings.get("made_reassociate_entries_body_text"),
            positiveText = Strings.get("positive_button_text_ok")
        ) {
            correctDonorWithProducts = viewModel.donorFromNameAndDateWithProducts(correctDonor) ?: DonorWithProducts(viewModel.emptyDonor)
            isReassociateCompleted = true
            showStandardModalState = StandardModalArgs()
        }
    }

    fun resetReassociatedScreen() {
        correctDonorsWithProducts = listOf()
        incorrectDonorsWithProducts = listOf()
        correctDonorWithProducts = DonorWithProducts(viewModel.emptyDonor)
        incorrectDonorWithProducts = DonorWithProducts(viewModel.emptyDonor)
        singleSelectedProductList = listOf()
        incorrectDonorSelected = false
        isProductSelected = false
        isReassociateCompleted = false
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
                                    correctDonorsWithProducts = listOf(it)
                                    correctDonorWithProducts = viewModel.donorFromNameAndDateWithProducts(it.donor) ?: DonorWithProducts(viewModel.emptyDonor)
                                } else {
                                    incorrectDonorsWithProducts = listOf(it)
                                    incorrectDonorWithProducts = viewModel.donorFromNameAndDateWithProducts(it.donor) ?: DonorWithProducts(viewModel.emptyDonor)
                                    incorrectDonorSelected = true
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
                        canScrollVertically = false,
                        productList = it.products,
                        useOnProductsChange = false,
                        onProductSelected = { productList ->
                            singleSelectedProductList = productList
                            isProductSelected = true
                        },
                        enablerForProducts = enablerForProducts
                    )
                }
            }
        }
    }

    fun goBack() {
        resetReassociatedScreen()
        navigateUp()
    }

    LaunchedEffect(key1 = true) {
        Logger.i("MACELOG: launch ReassociateDonationScreen=$reassociateDonationSearchStringName")
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
                        IconButton(onClick = { goBack() }) {
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
        Logger.i("MACELOG: Compose: ${ScreenNames.ReassociateDonation.name}")
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                isReassociateCompleted -> {
                    // Fourth (Last) run (Move product to correct donor)
                    Spacer(modifier = Modifier.height(12.dp))
                    MaceText(
                        modifier = Modifier.align(Alignment.Start),
                        text = Strings.get("reassociate_complete_title"),
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.body1,
                        fontFamily = avenirFontFamilyBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    MaceText(
                        modifier = Modifier.align(Alignment.Start),
                        text = Strings.get("reassociate_complete_body"),
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.body1,
                        fontFamily = avenirFontFamilyBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
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
                        // Second or third run
                        if (isProductSelected) {
                            // Third run (Display Incorrect Donor and Product)
                            Spacer(modifier = Modifier.height(12.dp))
                            MaceText(
                                modifier = Modifier.align(Alignment.Start),
                                text = Strings.get("incorrect_donor_and_product_title"),
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body1,
                                fontFamily = avenirFontFamilyBold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
                            DonorListWithProducts(
                                true,
                                listOf(DonorWithProducts(donor = incorrectDonorWithProducts.donor, products = singleSelectedProductList)),
                                displayForDonor = { true },
                                enablerForDonor = { false },
                                enablerForProducts = { false }
                            )
                        } else {
                            // Second run (Choose incorrect donor)
                            Spacer(modifier = Modifier.height(12.dp))
                            MaceText(
                                modifier = Modifier.align(Alignment.Start),
                                text = Strings.get("incorrect_donor_title"),
                                color = MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body1,
                                fontFamily = avenirFontFamilyBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            MaceText(
                                modifier = Modifier.align(Alignment.Start),
                                text = Strings.get("choose_product_for_reassociation_title"),
                                color = MaterialTheme.colors.secondary,
                                style = MaterialTheme.typography.body1,
                                fontFamily = avenirFontFamilyBold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
                            DonorListWithProducts(
                                true,
                                incorrectDonorsWithProducts,
                                displayForDonor = { true },
                                enablerForDonor = { false },
                                enablerForProducts = { true }
                            )
                        }
                        if (isProductSelected) {
                            // Third run (Choose correct donor)
                            Spacer(modifier = Modifier.height(8.dp))
                            var text by rememberSaveable { mutableStateOf("") }
                            MaceEditText(
                                testTag = "otf_correct_donor",
                                value = text,
                                onValueChange = { text = it },
                                label = Strings.get("initial_letters_of_correct_donor_last_name_text"),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                        handleSearchClickWithProducts(true, text)
                                    }
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            if (correctDonorsWithProducts.isNotEmpty()) {
                                MaceText(
                                    modifier = Modifier.align(Alignment.Start),
                                    text = Strings.get("choose_correct_donor_title"),
                                    color = MaterialTheme.colors.secondary,
                                    style = MaterialTheme.typography.body1,
                                    fontFamily = avenirFontFamilyBold
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
                            DonorListWithProducts(
                                true,
                                correctDonorsWithProducts,
                                displayForDonor = { true },
                                enablerForDonor = { true },
                                enablerForProducts = { false }
                            )

                        }
                    } else {
                        // First run (Enter incorrect donor name and choose the incorrect donor)
                        var text by remember { mutableStateOf("") }
                        Spacer(modifier = Modifier.height(12.dp))
                        MaceEditText(
                            testTag = "otf_correct_donor",
                            value = text,
                            onValueChange = { text = it },
                            label = Strings.get("initial_letters_of_incorrect_donor_last_name_text"),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    handleSearchClickWithProducts(false, text)
                                }
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        if (incorrectDonorsWithProducts.isNotEmpty()) {
                            MaceText(
                                modifier = Modifier
                                    .align(Alignment.Start),
                                text = Strings.get("choose_incorrect_donor_title"),
                                color = MaterialTheme.colors.secondary,
                                style = MaterialTheme.typography.body1,
                                fontFamily = avenirFontFamilyBold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
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