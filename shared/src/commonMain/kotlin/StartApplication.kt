
import androidx.compose.runtime.Composable
import ui.DrawerAppComponent

@Composable
fun StartApplication(viewModel: BloodViewModel, repository: Repository) {
    MaceTemplateTheme {
        DrawerAppComponent(viewModel, repository)
    }
}