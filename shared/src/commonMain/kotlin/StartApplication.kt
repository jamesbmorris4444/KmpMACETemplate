
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import ui.DrawerAppComponent

@Composable
fun StartApplication(repository: Repository) {
    Logger.i("MACELOG: 1111")
    DrawerAppComponent(repository.screenWidth.dp, repository.screenHeight.dp)
}