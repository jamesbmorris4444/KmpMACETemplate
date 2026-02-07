package ui
import BloodViewModel
import MaceText
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import co.touchlab.kermit.Logger
import com.Strings
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.transition.NavTransition

data class AppBarState(
    val title: String = Strings.get("rocket_launch_pending_title"),
    val actions: (@Composable RowScope.() -> Unit)? = null,
    val navigationIcon: (@Composable () -> Unit)? = null
)

// Called one time at app startup
@Composable
fun ScreenNavigator(
    navigator: Navigator,
    initialRoute: String,
    viewModel: BloodViewModel
) {
    var appBarState by remember { mutableStateOf(AppBarState()) }
    Scaffold(
        topBar = {
            StartScreenAppBar(appBarState = appBarState)
        }
    ) { internalPadding ->
        Box(modifier = Modifier.padding(internalPadding)) {
            NavHost(
                navigator = navigator,
                navTransition = NavTransition(),
                initialRoute = initialRoute,
            ) {
                scene(
                    route = ScreenNames.RocketLaunch.name,
                    navTransition = NavTransition(),
                ) {
                    Logger.i("MACELOG: ScreenNavigator: launch screen=${ScreenNames.RocketLaunch.name}")
                    RocketLaunchScreen(
                        navigator = navigator,
                        configAppBar = {
                            appBarState = it
                        },
                        viewModel = viewModel,
                        title = ScreenNames.RocketLaunch.string
                    )
                }
                scene(
                    route = ScreenNames.Movies.name,
                    navTransition = NavTransition(),
                ) {
                    Logger.i("MACELOG: ScreenNavigator: launch screen=${ScreenNames.Movies.name}")
                    MoviesScreen(
                        navigator = navigator,
                        configAppBar = {
                            appBarState = it
                        },
                        viewModel = viewModel,
                        title = ScreenNames.Movies.string
                    )
                }
                scene(
                    route = ScreenNames.TravelDestinations.name,
                    navTransition = NavTransition(),
                ) {
                    Logger.i("MACELOG: ScreenNavigator: launch screen=${ScreenNames.TravelDestinations.name}")
                    TravelDestinationsScreen(
                        navigator = navigator,
                        viewModel = viewModel,
                        title = ScreenNames.TravelDestinations.string,
                        configAppBar = {
                            appBarState = it
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun StartScreenAppBar(
    appBarState: AppBarState
) {
    TopAppBar(
        title = { MaceText(
            modifier = Modifier.testTag("item"),
            text = appBarState.title,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.body1
        ) },
        backgroundColor = MaterialTheme.colors.primary,
        actions = { appBarState.actions?.invoke(this) },
        navigationIcon = { appBarState.navigationIcon?.invoke() }
    )
}

enum class ScreenNames(val inDrawer: Boolean, val string: String) {
    RocketLaunch(false, Strings.get("rocket_launch_screen_name")),
    Movies(false, Strings.get("movies_screen_name")),
    TravelDestinations(false, Strings.get("travel_destinations_screen_name")),
    DonateProductsSearch(false, Strings.get("donate_products_search_screen_name")),
    CreateProducts(false, Strings.get("create_blood_product_title")),
    ManageDonorAfterSearch(false, Strings.get("manage_donor_after_search_title")),
    ManageDonorFromDrawer(true, Strings.get("manage_donor_from_drawer_title")),
    ReassociateDonation(true, Strings.get("reassociate_donation_title")),
    ViewDonorList(true, Strings.get("view_donor_list_title"))
}