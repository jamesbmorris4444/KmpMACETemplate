
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.MaceTemplateTheme
import ui.DrawerAppComponent

@Composable
fun StartApplication(viewModel: BloodViewModel, repository: Repository) {
    MaceTemplateTheme {
        DrawerAppComponent(viewModel, repository.screenWidth.dp)
    }
}