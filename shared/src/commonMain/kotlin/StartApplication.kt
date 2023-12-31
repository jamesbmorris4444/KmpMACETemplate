
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.MaceTemplateTheme
import ui.DrawerAppComponent

@Composable
fun StartApplication(repository: Repository) {
    MaceTemplateTheme {
        DrawerAppComponent(repository.screenWidth.dp, repository.screenHeight.dp)
    }
}