package ui

import BloodViewModel
import Strings
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.jetbrains.handson.kmm.shared.cache.Product
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProductListContent(
    canScrollVertically: Boolean,
    products: List<Product>,
    useOnProductsChange: Boolean,
    onProductsChange: (List<Product>) -> Unit,
    onProductSelected: (List<Product>) -> Unit,
    onDinTextChange: (String) -> Unit,
    onProductCodeTextChange: (String) -> Unit,
    onExpirationTextChange: (String) -> Unit,
    enablerForProducts: (Product) -> Boolean
) {
    Column(
        modifier = if (canScrollVertically) Modifier.verticalScroll(rememberScrollState()) else Modifier
    ) {
        products.forEachIndexed { index, item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .height(40.dp)
                        .width(30.dp)
                        .clickable(
                            enabled = enablerForProducts(item)
                        ) {
                            if (useOnProductsChange) {
                                onProductsChange(products.filterIndexed { filterIndex, _ -> filterIndex != index })
                            } else {
                                val productSelectedAsList = products.filterIndexed { filterIndex, _ -> filterIndex == index }
                                onProductSelected(productSelectedAsList)
                            }
                        },
                    painter = painterResource("drawable/delete_icon.png"),
                    contentDescription = "Dialog Alert"
                )
                Image(
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .height(40.dp)
                        .width(40.dp)
                        .clickable(
                            enabled = enablerForProducts(item)
                        ) {
                            if (useOnProductsChange) {
                                onDinTextChange(products[index].din)
                                onProductCodeTextChange(products[index].productCode)
                                onExpirationTextChange(products[index].expirationDate)
                                onProductsChange(products.filterIndexed { filterIndex, _ -> filterIndex != index })
                            } else {
                                val productSelectedAsList = products.filterIndexed { filterIndex, _ -> filterIndex == index }
                                onProductSelected(productSelectedAsList)
                            }
                        },
                    painter = painterResource("drawable/edit_icon.png"),
                    contentDescription = "Dialog Alert"
                )
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp)
                ) {
                    ListDisplayText("item_din", Strings.get("din"), item.din)
                    ListDisplayText("item_abo_rh", Strings.get("abo_rh"), item.aboRh)
                    ListDisplayText("item_product_code", Strings.get("product_code"), item.productCode)
                    ListDisplayText("item_expiration", Strings.get("expiration"), item.expirationDate)
                }
            }
            Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
        }
    }
}

@Composable
fun ProductListScreen(
    canScrollVertically: Boolean,
    productList: List<Product>,
    useOnProductsChange: Boolean,
    onProductsChange: (List<Product>) -> Unit = { },
    onProductSelected: (List<Product>) -> Unit = { },
    onDinTextChange: (String) -> Unit = { },
    onProductCodeTextChange: (String) -> Unit = { },
    onExpirationTextChange: (String) -> Unit = { },
    enablerForProducts: (Product) -> Boolean
) {
    ProductListContent(
        canScrollVertically = canScrollVertically,
        products = productList,
        useOnProductsChange = useOnProductsChange,
        onProductsChange = onProductsChange,
        onProductSelected = onProductSelected,
        onDinTextChange = onDinTextChange,
        onProductCodeTextChange = onProductCodeTextChange,
        onExpirationTextChange = onExpirationTextChange,
        enablerForProducts = enablerForProducts
    )
}

@Composable
fun AnnotatedLabelledStringBuilder(label: String, body: String): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("$label: ") }
        append(body)
    }
}

@Composable
fun ListDisplayText(testTag: String, label: String, body: String, color: Color = MaterialTheme.colors.onBackground) {
    Text(
        modifier = Modifier
            .padding(top = 1.dp)
            .testTag(testTag),
        text = AnnotatedLabelledStringBuilder(label, body),
        color = color,
        style = MaterialTheme.typography.body1
    )
}

@Composable
fun StandardEditText(
    modifier: Modifier = Modifier,
    testTag: String, value: String,
    onValueChange: (String) -> Unit,
    label: String,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,

) {
    OutlinedTextField(
        modifier = modifier
            .height(72.dp)
            .testTag(testTag),
        value = value,
        readOnly = readOnly,
        textStyle = TextStyle(
            color = MaterialTheme.colors.primary,
            fontSize = MaterialTheme.typography.body2.fontSize
        ),
        onValueChange = onValueChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.primary
        ),
        shape = MaterialTheme.shapes.medium,
        label = { Text(label, color = MaterialTheme.colors.primary, style = MaterialTheme.typography.body2) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = trailingIcon
    )
}

@Composable
fun progressBar() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = MaterialTheme.colors.primary,
            strokeWidth = 6.dp
        )
    }
}

fun genericApiCall(
    searchKey: String = "",
    searchType: String = "",
    apiType: ApiCalls,
    viewModel: BloodViewModel
) {
    val composableScope = viewModel.viewModelScope.coroutineScope
    when (apiType) {
        ApiCalls.SpaceX -> {
            composableScope.launch {
                val pair = viewModel.getSpaceXLaunches(composableScope)
                if (pair.second.isEmpty()) {
                    // success
                    viewModel.launchesAvailable.value = pair.first
                } else {
                    // failure
                    viewModel.launchesFailure.value = pair.second
                }
            }
        }
        ApiCalls.TravelDestinations -> {
            composableScope.launch {
                val pair = viewModel.getHotelDestinationIds(searchKey, composableScope)
                if (pair.second.isEmpty()) {
                    // success
                    viewModel.destinationIdsAvailable.value = pair.first
                } else {
                    // failure
                    viewModel.destinationIdsFailure.value = pair.second
                }
            }
        }

        ApiCalls.TravelRegions -> {
            composableScope.launch {
                val pair = viewModel.getHotels(searchKey, searchType, composableScope)
                if (pair.second.isEmpty()) {
                    // success
                    viewModel.hotelsAvailable.value = pair.first
                } else {
                    // failure
                    viewModel.regionsFailure.value = pair.second
                }
            }
        }
    }
}

enum class ApiCalls(val string: String) {
    SpaceX(Strings.get("rocket_launch_type")),
    TravelRegions(Strings.get("region_type")),
    TravelDestinations(Strings.get("destination_id_type"))

}