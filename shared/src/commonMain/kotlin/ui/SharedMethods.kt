package ui

import ViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.jetbrains.handson.kmm.shared.cache.Product
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProductListContent(
    viewModel: ViewModel,
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
                        .padding(start = 40.dp)
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
                                viewModel.updateProductRemovedForReassociation(true, productSelectedAsList[0].id)
                            }
                        },
                    painter = painterResource("drawable/delete_icon.png"),
                    contentDescription = "Dialog Alert"
                )
                Image(
                    modifier = Modifier
                        .padding(start = 40.dp)
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
                                viewModel.updateProductRemovedForReassociation(true, productSelectedAsList[0].id)
                            }
                        },
                    painter = painterResource("drawable/edit_icon.png"),
                    contentDescription = "Dialog Alert"
                )
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp)
                ) {
                    Text(
                        text = item.din,
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = item.aboRh,
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = item.productCode,
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = item.expirationDate,
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            Divider(color = MaterialTheme.colors.onBackground, thickness = 2.dp)
        }
    }
}

@Composable
fun ProductListScreen(
    viewModel: ViewModel,
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
        viewModel = viewModel,
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