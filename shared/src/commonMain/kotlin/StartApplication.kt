
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.MaceTemplateTheme
import moe.tlaster.precompose.navigation.rememberNavigator
import ui.DrawerAppComponent
import ui.ScreenNames
import ui.ScreenNavigator

@Composable
fun StartApplication(viewModel: BloodViewModel, repository: Repository) {
    MaceTemplateTheme {
        ScreenNavigator(
            viewModel = viewModel,
            navigator = rememberNavigator(),
            initialRoute = ScreenNames.RocketLaunch.name
        )
    }
}