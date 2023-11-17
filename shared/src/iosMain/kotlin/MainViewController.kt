import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController(viewModel: BloodViewModel, repository: Repository) = ComposeUIViewController { StartApplication(viewModel, repository) }
