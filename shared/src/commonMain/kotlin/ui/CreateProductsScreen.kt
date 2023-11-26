
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jetbrains.handson.kmm.shared.cache.Donor
import com.jetbrains.handson.kmm.shared.cache.Product
import ui.AppBarState
import ui.DismissSelector
import ui.ProductListScreen
import ui.StandardModal
import ui.StandardModalArgs
import ui.WidgetButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateProductsScreen(
    repository: Repository,
    configAppBar: (AppBarState) -> Unit,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    openDrawer: () -> Unit,
    donor: Donor,
    viewModel: BloodViewModel,
    title: String,
    onCompleteButtonClicked: () -> Unit,
) {
    val screenWidth = repository.screenWidth.dp
    val leftGridPadding = 20.dp
    val rightGridPadding = 20.dp
    val horizontalGridWidth = screenWidth - leftGridPadding - rightGridPadding
    val horizontalGridHeight = horizontalGridWidth * 2 / 3
    val gridCellWidth = horizontalGridWidth / 2
    val gridCellHeight = horizontalGridHeight / 2
    val enterDinText = Strings.get("enter_din_text")
    val dinTitle = Strings.get("din_title")
    val enterProductCodeText = Strings.get("enter_product_code")
    val productCodeTitle = Strings.get("product_code_title")
    val enterExpirationText = Strings.get("enter_expiration_text")
    val expirationTitle = Strings.get("expiration_title")
    val aboRhTitle = Strings.get("abo_rh_title")

    // state variables
    val products by viewModel.productsListState.collectAsState()
    val dinText by viewModel.dinTextState.collectAsState()
    val expirationText by viewModel.expirationTextState.collectAsState()
    val productCodeText by viewModel.productCodeTextState.collectAsState()
    val showStandardModalState by viewModel.showStandardModalState.collectAsState()
    val screenIsReadOnly by viewModel.screenIsReadOnlyState.collectAsState()
    val clearButtonVisible by viewModel.clearButtonVisibleState.collectAsState()
    val confirmButtonVisible by viewModel.confirmButtonVisibleState.collectAsState()
    val completeButtonVisible by viewModel.completeButtonVisibleState.collectAsState()

    fun setButtonState(clearVisible: Boolean, confirmVisible: Boolean, completeVisible: Boolean) {
        viewModel.changeClearButtonVisibleState(clearVisible)
        if (screenIsReadOnly) {
            viewModel.changeConfirmButtonVisibleState(false)
            viewModel.changeCompleteButtonVisibleState(completeVisible)
        } else {
            viewModel.changeConfirmButtonVisibleState(confirmVisible)
            viewModel.changeCompleteButtonVisibleState(completeVisible)
        }
    }

    fun clearTextState(dinText: String = "", productCode: String = "", expiration: String = "") {
        viewModel.changeDinTextState(dinText)
        viewModel.changeProductCodeTextState(productCode)
        viewModel.changeExpirationTextState(expiration)
    }

    fun processNewProduct() {
        val product = Product(id = 0, donorId = donor.id, din = dinText, aboRh = donor.aboRh, productCode = productCodeText, expirationDate = expirationText, removedForReassociation = false)
        val productList: MutableList<Product> = products.toMutableList()
        productList.add(product)
        viewModel.changeProductsListState(productList)
    }

    fun addDonorWithProductsToDatabase() {
        repository.insertProductsIntoDatabase(products)
        viewModel.changeShowStandardModalState(
            StandardModalArgs(
                topIconId = "drawable/notification.xml",
                titleText = Strings.get("made_db_entries_title_text"),
                bodyText = Strings.get("made_db_entries_body_text"),
                positiveText = Strings.get("positive_button_text_ok")
            ) {
                viewModel.changeShowStandardModalState(StandardModalArgs())
                viewModel.changeProductsListState(listOf())
                viewModel.changeScreenIsReadOnlyState(false)
                clearTextState()
                setButtonState(clearVisible = false, confirmVisible = true, completeVisible = true)
                onCompleteButtonClicked()
            }
        )
    }

    fun onClearClicked() {
        clearTextState()
        setButtonState(clearVisible = false, confirmVisible = true, completeVisible = true)
    }

    fun onConfirmClicked() {
        if (products.isEmpty() && dinText.isEmpty() && productCodeText.isEmpty() && expirationText.isEmpty()) {
            // all are empty, display donor product list
            repository.donorsFromFullNameWithProducts(donor.lastName, donor.dob)?.let {
                viewModel.changeProductsListState(it.products)
                viewModel.changeScreenIsReadOnlyState(true)
                setButtonState(clearVisible = false, confirmVisible = false, completeVisible = true)
            } ?: {
                viewModel.changeShowStandardModalState(
                    StandardModalArgs(
                        topIconId = "drawable/notification.xml",
                        titleText = Strings.get("donor_fetch_problem_title_text"),
                        bodyText = Strings.get("donor_fetch_problem_body_text"),
                        positiveText = Strings.get("positive_button_text_ok")
                    ) {
                        viewModel.changeShowStandardModalState(StandardModalArgs())
                    }
                )
            }
        } else {
            processNewProduct()
            clearTextState()
            setButtonState(clearVisible = false, confirmVisible = false, completeVisible = true)
        }
    }

    fun onCompleteClicked() {
        when {
            screenIsReadOnly || (products.isEmpty() && (dinText.isEmpty() || productCodeText.isEmpty() || expirationText.isEmpty())) -> {
                // Nothing to store in DB, exit
                viewModel.changeScreenIsReadOnlyState(false)
                clearTextState()
                setButtonState(clearVisible = false, confirmVisible = true, completeVisible = true)
                onCompleteButtonClicked()
            }
            dinText.isNotEmpty() && productCodeText.isNotEmpty() && expirationText.isNotEmpty() -> {
                // all fields filled, DB entry possibly needed
                viewModel.changeShowStandardModalState(
                    StandardModalArgs(
                        topIconId = "drawable/notification.xml",
                        titleText = Strings.get("std_modal_noconfirm_title"),
                        bodyText = Strings.get("std_modal_noconfirm_body"),
                        positiveText = Strings.get("positive_button_text_yes"),
                        negativeText = Strings.get("negative_button_text_no")
                    ) { dismissSelector ->
                        when (dismissSelector) {
                            DismissSelector.POSITIVE -> {
                                processNewProduct()
                                clearTextState()
                                setButtonState(clearVisible = false, confirmVisible = false, completeVisible = true)
                            }
                            else -> {
                                setButtonState(clearVisible = true, confirmVisible = true, completeVisible = true)
                            }
                        }
                        viewModel.changeShowStandardModalState(StandardModalArgs())
                    }
                )
            }
            else -> {
                // At least one field is empty, add products to DB if any are present
                if (products.isNotEmpty()) {
                    addDonorWithProductsToDatabase()
                } else {
                    viewModel.changeScreenIsReadOnlyState(false)
                    clearTextState()
                    setButtonState(clearVisible = false, confirmVisible = true, completeVisible = true)
                    onCompleteButtonClicked()
                }
            }
        }
    }

    fun handleTextEntry(dinText: String, productCodeText: String, expirationText: String) {
        val allPresent = dinText.isNotEmpty() && productCodeText.isNotEmpty() && expirationText.isNotEmpty()
        val nonePresent = dinText.isEmpty() && productCodeText.isEmpty() && expirationText.isEmpty()
        if (allPresent || nonePresent) {
            viewModel.changeConfirmButtonVisibleState(true)
        } else {
            viewModel.changeConfirmButtonVisibleState(false)
        }
        if (nonePresent) {
            viewModel.changeClearButtonVisibleState(false)
        } else {
            viewModel.changeClearButtonVisibleState(true)
        }
    }

    fun goBack() {
        viewModel.changeShowStandardModalState(StandardModalArgs())
        viewModel.changeProductsListState(listOf())
        viewModel.changeScreenIsReadOnlyState(false)
        clearTextState()
        setButtonState(clearVisible = false, confirmVisible = true, completeVisible = true)
        navigateUp()
    }

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

    when {
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.height(40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text (
                        modifier = Modifier
                            .padding(PaddingValues(start = leftGridPadding)),
                        text = Strings.format("create_products_header_text", donor.lastName, donor.firstName),
                        style = MaterialTheme.typography.body1,
                        fontFamily = avenirFontFamilyBold
                    )
                }
                var currentDinText by remember { mutableStateOf(dinText) }
                var currentExpirationText by remember { mutableStateOf(expirationText) }
                var currentProductCodeText by remember { mutableStateOf(productCodeText) }
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(PaddingValues(start = leftGridPadding, end = rightGridPadding)),
                    columns = GridCells.Fixed(2)
                ) {
                    item {
                        LazyHorizontalGrid(
                            modifier = Modifier
                                .height(horizontalGridHeight),
                            rows = GridCells.Fixed(2)
                        ) {
                            item { // upper left
                                Box(
                                    modifier = Modifier
                                        .size(gridCellWidth, gridCellHeight)
                                        .borders(2.dp, DarkGray, left = true, top = true, bottom = true)
                                ) {
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .padding(PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp))
                                            .align(Alignment.BottomStart),
                                        value = currentDinText,
                                        readOnly = screenIsReadOnly,
                                        onValueChange = {
                                            currentDinText = it
                                            viewModel.changeDinTextState(currentDinText)
                                            handleTextEntry(currentDinText, productCodeText, expirationText)
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        label = { Text(enterDinText) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                    )
                                    Text(
                                        modifier = Modifier
                                            .padding(PaddingValues(start = 8.dp))
                                            .align(Alignment.TopStart),
                                        text = dinTitle,
                                        style = MaterialTheme.typography.body1,
                                        fontFamily = avenirFontFamilyBold

                                    )
                                }
                            }
                            item { // lower left
                                Box(
                                    modifier = Modifier
                                        .size(gridCellWidth, gridCellHeight)
                                        .borders(2.dp, DarkGray, left = true, bottom = true)
                                ) {
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .padding(PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp))
                                            .align(Alignment.BottomStart),
                                        value = currentProductCodeText,
                                        readOnly = screenIsReadOnly,
                                        onValueChange = {
                                            currentProductCodeText = it
                                            viewModel.changeProductCodeTextState(currentProductCodeText)
                                            handleTextEntry(dinText, it, expirationText)
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        label = { Text(enterProductCodeText) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                    )
                                    Text(
                                        modifier = Modifier
                                            .padding(PaddingValues(start = 8.dp))
                                            .align(Alignment.TopStart),
                                        text = productCodeTitle,
                                        style = MaterialTheme.typography.body1,
                                        fontFamily = avenirFontFamilyBold
                                    )
                                }
                            }
                        }
                    }
                    item {
                        LazyHorizontalGrid(
                            modifier = Modifier
                                .height(horizontalGridHeight),
                            rows = GridCells.Fixed(2)
                        ) {
                            item { // upper right
                                Box(
                                    modifier = Modifier
                                        .size(gridCellWidth, gridCellHeight)
                                        .borders(
                                            2.dp,
                                            DarkGray,
                                            left = true,
                                            top = true,
                                            right = true,
                                            bottom = true
                                        )
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(PaddingValues(start = 8.dp))
                                            .align(Alignment.TopStart),
                                        text = aboRhTitle,
                                        style = MaterialTheme.typography.body1,
                                        fontFamily = avenirFontFamilyBold
                                    )
                                    Text(
                                        modifier = Modifier
                                            .padding(PaddingValues(bottom = 32.dp))
                                            .align(Alignment.BottomCenter),
                                        text = donor.aboRh,
                                        style = MaterialTheme.typography.body1,
                                        fontFamily = avenirFontFamilyBold
                                    )
                                }
                            }
                            item { // lower right
                                Box(
                                    modifier = Modifier
                                        .size(gridCellWidth, gridCellHeight)
                                        .borders(2.dp, DarkGray, left = true, right = true, bottom = true)
                                ) {
                                    OutlinedTextField(
                                        modifier = Modifier
                                            .height(80.dp)
                                            .padding(PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp))
                                            .align(Alignment.BottomStart),
                                        value = currentExpirationText,
                                        readOnly = screenIsReadOnly,
                                        onValueChange = {
                                            currentExpirationText = it
                                            viewModel.changeExpirationTextState(currentExpirationText)
                                            handleTextEntry(dinText, productCodeText, currentExpirationText)
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        label = { Text(enterExpirationText) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                    )
                                    Text(
                                        modifier = Modifier
                                            .padding(PaddingValues(start = 8.dp))
                                            .align(Alignment.TopStart),
                                        text = expirationTitle,
                                        style = MaterialTheme.typography.body1,
                                        fontFamily = avenirFontFamilyBold
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val keyboardController = LocalSoftwareKeyboardController.current
                    if (clearButtonVisible) {
                        WidgetButton(
                            padding = PaddingValues(start = 8.dp, end = 8.dp),
                            onClick = {
                                onClearClicked()
                                keyboardController?.hide()
                            },
                            buttonText = Strings.get("clear_button_text")
                        )
                    }
                    if (confirmButtonVisible) {
                        WidgetButton(
                            padding = PaddingValues(start = 8.dp, end = 8.dp),
                            onClick = {
                                onConfirmClicked()
                                keyboardController?.hide()
                            },
                            buttonText = Strings.get("confirm_button_text")
                        )
                    }
                    if (completeButtonVisible) {
                        WidgetButton(
                            padding = PaddingValues(start = 8.dp, end = 8.dp),
                            onClick = {
                                onCompleteClicked()
                            },
                            buttonText = Strings.get("complete_button_text")
                        )
                    }
                }
                Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
                ProductListScreen(
                    repository = repository,
                    canScrollVertically = true,
                    productList = products,
                    useOnProductsChange = true,
                    onProductsChange = { viewModel.changeProductsListState(it) },
                    onDinTextChange = { viewModel.changeDinTextState(it) },
                    onProductCodeTextChange = { viewModel.changeProductCodeTextState(it) },
                    onExpirationTextChange = { viewModel.changeDinTextState(it) },
                    enablerForProducts = { true }
                )
            }
        }
    }
}

private fun Modifier.borders(strokeWidth: Dp, color: Color, left: Boolean = false, top: Boolean = false, right: Boolean = false, bottom: Boolean = false) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2
            if (left) {
                drawLine(
                    color = color,
                    start = Offset(x = 0f, y = height),
                    end = Offset(x = 0f , y = 0f),
                    strokeWidth = strokeWidthPx
                )
            }
            if (top) {
                drawLine(
                    color = color,
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = width , y = 0f),
                    strokeWidth = strokeWidthPx
                )
            }
            if (right) {
                drawLine(
                    color = color,
                    start = Offset(x = width, y = 0f),
                    end = Offset(x = width , y = height),
                    strokeWidth = strokeWidthPx
                )
            }
            if (bottom) {
                drawLine(
                    color = color,
                    start = Offset(x = width, y = height),
                    end = Offset(x = 0f , y = height),
                    strokeWidth = strokeWidthPx
                )
            }
        }
    }
)
