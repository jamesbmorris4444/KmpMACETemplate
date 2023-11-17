
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
import moe.tlaster.precompose.navigation.Navigator
import ui.AppBarState
import ui.DismissSelector
import ui.ProductListScreen
import ui.StandardModalArgs
import ui.WidgetButton

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateProductsScreen(
    repository: Repository,
    navigator: Navigator,
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
    val dinText = viewModel.dinTextState.collectAsState().value
    val expirationText  = viewModel.expirationTextState.collectAsState().value
    val productCodeText = viewModel.productCodeTextState.collectAsState().value
    val clearButtonVisible = viewModel.clearButtonVisibleState.collectAsState().value
    val confirmButtonVisible = viewModel.confirmButtonVisibleState.collectAsState().value
    val confirmNeeded  = viewModel.confirmNeededState.collectAsState().value
    val products = viewModel.productsListState.collectAsState().value
    val displayedProductList = viewModel.displayedProductListState.collectAsState().value

    fun processNewProduct() {
        val product = Product(id = 0, donorId = donor.id, din = dinText, aboRh = donor.aboRh, productCode = productCodeText, expirationDate = expirationText, inReassociate = false, removedForReassociation = false)
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
            }
        )
    }

    fun onClearClicked() {
        viewModel.changeDinTextState("")
        viewModel.changeProductCodeTextState("")
        viewModel.changeExpirationTextState("")
        viewModel.changeClearButtonVisibleState(false)
        viewModel.changeConfirmButtonVisibleState(false)
        viewModel.changeConfirmNeededState(false)
    }

    fun onConfirmClicked() {
        if (products.isEmpty() && dinText.isEmpty() && productCodeText.isEmpty() && expirationText.isEmpty()) {
            repository.donorsFromFullNameWithProducts(donor.lastName, donor.dob)?.let {
                viewModel.changeDisplayedProductListState(it.products)
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
            viewModel.changeClearButtonVisibleState(true)
            viewModel.changeConfirmButtonVisibleState(true)
            viewModel.changeConfirmNeededState(false)
            processNewProduct()
            if (displayedProductList.isNotEmpty()) {
                viewModel.changeDisplayedProductListState(listOf())
            }
        }
    }

    fun onCompleteClicked() {
        if (confirmNeeded) {
            viewModel.changeShowStandardModalState(
                StandardModalArgs(
                    topIconId = "drawable/notification.xml",
                    titleText = Strings.get("std_modal_noconfirm_title"),
                    bodyText = Strings.get("std_modal_noconfirm_body"),
                    positiveText = Strings.get("positive_button_text_yes"),
                    negativeText = Strings.get("positive_button_text_no")
                ) { dismissSelector ->
                    when (dismissSelector) {
                        DismissSelector.POSITIVE -> {
                            processNewProduct()
                            if (products.isNotEmpty()) {
                                addDonorWithProductsToDatabase()
                            }
                            onCompleteButtonClicked()
                        }
                        else -> { }
                    }
                    viewModel.changeShowStandardModalState(StandardModalArgs())
                }
            )
        } else {
            if (products.isNotEmpty()) {
                addDonorWithProductsToDatabase()
            }
            onCompleteButtonClicked()
        }
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
    ) {
        Column(
            modifier = Modifier.height(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text (
                modifier = Modifier
                    .padding(PaddingValues(start = leftGridPadding)),
                text = Strings.format(Strings.format("create_products_header_text", donor.lastName, donor.firstName)),
                style = MaterialTheme.typography.body1,
                fontFamily = avenirFontFamilyBold
            )
        }
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
                                value = dinText,
                                onValueChange = {
                                    viewModel.changeDinTextState(it)
                                    viewModel.changeConfirmNeededState(true)
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
                                value = productCodeText,
                                onValueChange = {
                                    viewModel.changeProductCodeTextState(it)
                                    viewModel.changeConfirmNeededState(true)
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
                                value = expirationText,
                                onValueChange = {
                                    viewModel.changeExpirationTextState(it)
                                    viewModel.changeConfirmNeededState(true)
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
            WidgetButton(
                padding = PaddingValues(start = 8.dp, end = 8.dp),
                onClick = {
                    onClearClicked()
                    keyboardController?.hide()
                },
                buttonText = Strings.get("clear_button_text")
            )
            WidgetButton(
                padding = PaddingValues(start = 8.dp, end = 8.dp),
                onClick = {
                    onConfirmClicked()
                    keyboardController?.hide()
                },
                buttonText = Strings.get("confirm_button_text")
            )
            WidgetButton(
                padding = PaddingValues(start = 8.dp, end = 8.dp),
                onClick = {
                    onCompleteClicked()
                },
                buttonText = Strings.get("complete_button_text")
            )
        }
        if (displayedProductList.isEmpty()) {
            if (products.isNotEmpty()) {
                Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
            }
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
        } else {
            if (displayedProductList.isNotEmpty()) {
                Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
            }
            ProductListScreen(
                repository = repository,
                canScrollVertically = true,
                productList = displayedProductList,
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
