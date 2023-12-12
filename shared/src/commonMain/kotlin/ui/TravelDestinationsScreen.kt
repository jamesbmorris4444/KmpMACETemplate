package ui
import BloodViewModel
import Strings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.jetbrains.handson.kmm.shared.entity.HotelDestinationId
import com.jetbrains.handson.kmm.shared.entity.HotelRegion
import io.kamel.core.Resource
import io.kamel.core.utils.cacheControl
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.utils.CacheControl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

@Composable
fun TravelDestinationsScreen(
    navigator: Navigator,
    viewModel: BloodViewModel,
    title: String,
    configAppBar: (AppBarState) -> Unit
) {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun TravelStartHandler(
        navigator: Navigator,
        viewModel: BloodViewModel,
        title: String,
        progressBarState: Boolean,
        configAppBar: (AppBarState) -> Unit
    ) {
        // state variables
        var destinationTextEntered by remember { mutableStateOf("") }

        LaunchedEffect(key1 = true) {
            Logger.i("MACELOG: launch Travel Destination Screen=${ScreenNames.TravelDestinations.name}")
            configAppBar(
                AppBarState(
                    title = title,
                    navigationIcon = {
                        IconButton(onClick = {
                            navigator.navigate(
                                route = ScreenNames.DonateProductsSearch.name,
                                NavOptions(popUpTo = PopUpTo(ScreenNames.TravelDestinations.name, inclusive = true))
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = Strings.get("back_button_content_description")
                            )
                        }
                    }
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            var textEntered by remember { mutableStateOf("") }
            val keyboardController = LocalSoftwareKeyboardController.current

            when {
                progressBarState -> progressBar()
                else -> {
                    StandardEditText(
                        testTag = "otf_place_before",
                        value = textEntered,
                        onValueChange = { textEntered = it },
                        label = Strings.get("destination_search_view_text"),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                destinationTextEntered = textEntered
                                genericApiCall(searchKey = destinationTextEntered, apiType = ApiCalls.TravelDestinations, viewModel = viewModel)
                                viewModel.progressBarState.value = true
                            }
                        )
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun TravelDestinationsHandler(
        navigator: Navigator,
        viewModel: BloodViewModel,
        title: String,
        configAppBar: (AppBarState) -> Unit,
        progressBarState: Boolean,
        destinationIds: List<HotelDestinationId>? = null
    ) {
        // state variables
        val regionsSearchKey  by viewModel.regionsSearchKey.collectAsState()
        var regionTextEntered by remember { mutableStateOf("") }
        LaunchedEffect(key1 = true) {
            Logger.i("MACELOG: launch Travel Destinations Handler=${ScreenNames.TravelDestinations.name}")
            configAppBar(
                AppBarState(
                    title = title,
                    navigationIcon = {
                        IconButton(onClick = {
                            navigator.navigate(
                                route = ScreenNames.DonateProductsSearch.name,
                                NavOptions(popUpTo = PopUpTo(ScreenNames.TravelDestinations.name, inclusive = true))
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = Strings.get("back_button_content_description")
                            )
                        }
                    }
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 24.dp),
                text = Strings.format("travel_you_are_going", regionsSearchKey),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(18.dp))
            var regionExpanded by remember { mutableStateOf(false) }
            when {
                progressBarState -> progressBar()
                else -> {
                    ExposedDropdownMenuBox(
                        expanded = regionExpanded,
                        onExpandedChange = {
                            regionExpanded = !regionExpanded
                        }
                    ) {
                        StandardEditText(
                            testTag = "otf_region",
                            value = regionTextEntered,
                            onValueChange = { },
                            readOnly = true,
                            label = Strings.get("enter_region_text"),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) }
                        )
                        ExposedDropdownMenu(
                            expanded = regionExpanded,
                            onDismissRequest = { regionExpanded = false }
                        ) {
                            destinationIds?.forEach { label ->
                                DropdownMenuItem(
                                    modifier = Modifier.background(MaterialTheme.colors.primary),
                                    onClick = {
                                        regionExpanded = false
                                        regionTextEntered = label.name
                                        viewModel.destinationIdsAvailable.value = null
                                        Logger.d("JIMX  7777 ${label.searchType}")
                                        genericApiCall(searchKey = label.destId, searchType = label.searchType, apiType = ApiCalls.TravelRegions, viewModel = viewModel)
                                        viewModel.progressBarState.value = true
                                    }
                                ) {
                                    Text(
                                        text = label.name,
                                        color = MaterialTheme.colors.onPrimary,
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TravelRegionsHandler(
        navigator: Navigator,
        title: String,
        configAppBar: (AppBarState) -> Unit,
        hotelRegion: HotelRegion? = null
    ) {
        // state variables
        val coroutineScope = rememberCoroutineScope()

        @Composable
        fun HotelsDisplay(
            name: String,
            posterPath: String,
            coroutineScope: CoroutineScope
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ListDisplayText("item_title", Strings.get("title"), name)
                val painterResource: Resource<Painter> = asyncPainterResource(posterPath) {
                    coroutineContext = coroutineScope.coroutineContext
                    requestBuilder {
                        header("Key", "Value")
                        parameter("Key", "Value")
                        cacheControl(CacheControl.MAX_AGE)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    KamelImage(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 4.dp)
                            .size(100.dp),
                        resource = painterResource,
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null,
                        onFailure = { exception ->
                            coroutineScope.launch {
                                Logger.i("MACELOG: Kamel EXCEPTION=${exception.message.toString()}")
                            }
                        }
                    )
                }

                Divider(
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                    color = MaterialTheme.colors.onBackground,
                    thickness = 2.dp
                )
            }
        }

        @Composable
        fun HotelsList() {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = Strings.get("list_of_hotels"),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(18.dp))
            LazyColumn {
                items(count = hotelRegion?.hotelResult?.size ?: 0) { index ->
                    HotelsDisplay(
                        name = hotelRegion?.hotelResult?.get(index)?.hotelName ?: "",
                        posterPath = hotelRegion?.hotelResult?.get(index)?.photoUrl ?: "",
                        coroutineScope = coroutineScope
                    )
                }
            }
        }

        LaunchedEffect(key1 = true) {
            Logger.i("MACELOG: launch Travel Regions Handler=${ScreenNames.TravelDestinations.name}")
            configAppBar(
                AppBarState(
                    title = title,
                    navigationIcon = {
                        IconButton(onClick = {
                            navigator.navigate(
                                route = ScreenNames.DonateProductsSearch.name,
                                NavOptions(
                                    popUpTo = PopUpTo(
                                        ScreenNames.TravelDestinations.name,
                                        inclusive = true
                                    )
                                )
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = Strings.get("back_button_content_description")
                            )
                        }
                    }
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HotelsList()
        }

//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 24.dp, end = 24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(18.dp))
//            Text(
//                modifier = Modifier
//                    .align(Alignment.Start),
//                text = Strings.get("list_of_hotels"),
//                color = MaterialTheme.colors.primary,
//                style = MaterialTheme.typography.body1,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(18.dp))
//            LazyColumn {
//                items(count = hotelRegion?.hotelResult?.size ?: 0) { index ->
//                    HotelsDisplay(
//                        name = hotelRegion?.hotelResult?.get(index)?.hotelName ?: "",
//                        posterPath = hotelRegion?.hotelResult?.get(index)?.photoUrl ?: "",
//                        coroutineScope = coroutineScope
//                    )
//                }
//            }
//        }
    }

    @Composable
    fun handleFailure(viewModel: BloodViewModel, message: String, typeOfApi: ApiCalls, showStandardModalState: StandardModalArgs) {
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
            viewModel.showStandardModalState.value = StandardModalArgs(
                topIconId = "drawable/notification.xml",
                titleText = Strings.format("failure_api_title_text", typeOfApi.string),
                bodyText = message,
                positiveText = Strings.get("positive_button_text_ok"),
            ) {
                viewModel.destinationIdsFailure.value = ""
                viewModel.regionsFailure.value = ""
                viewModel.showStandardModalState.value = StandardModalArgs()
            }
        }
    }

    val destinationTitle = Strings.get("travel_destination_id")
    val hotelsTitle = Strings.get("travel_hotels")
    val destinationIdsAvailable: List<HotelDestinationId>? by viewModel.destinationIdsAvailable.collectAsState()
    val hotelsAvailable: HotelRegion? by viewModel.hotelsAvailable.collectAsState()
    val destinationIdsFailure by viewModel.destinationIdsFailure.collectAsState()
    val regionsFailure by viewModel.regionsFailure.collectAsState()
    val showStandardModalState by viewModel.showStandardModalState.collectAsState()
    val progressBarState by viewModel.progressBarState.collectAsState()

    when {
        destinationIdsFailure.isNotEmpty() -> {
            viewModel.progressBarState.value = false
            handleFailure(viewModel, destinationIdsFailure, ApiCalls.TravelDestinations, showStandardModalState)
        }
        regionsFailure.isNotEmpty() -> {
            viewModel.progressBarState.value = false
            handleFailure(viewModel, regionsFailure, ApiCalls.TravelRegions, showStandardModalState)
        }
        destinationIdsAvailable != null -> {
            viewModel.progressBarState.value = false
            TravelDestinationsHandler(navigator = navigator, configAppBar = configAppBar, viewModel = viewModel, title = destinationTitle, destinationIds = destinationIdsAvailable, progressBarState = progressBarState)
        }
        hotelsAvailable != null -> {
            viewModel.progressBarState.value = false
            TravelRegionsHandler(navigator = navigator, configAppBar = configAppBar, title = hotelsTitle, hotelRegion = hotelsAvailable)
        }
        else -> {
            TravelStartHandler(navigator = navigator, configAppBar = configAppBar, viewModel = viewModel, title = title, progressBarState = progressBarState)
        }
    }
}




