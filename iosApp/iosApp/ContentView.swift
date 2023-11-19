import UIKit
import SwiftUI
import shared

struct ComposeView: UIViewControllerRepresentable {
    var viewModel: BloodViewModel
    var repository: Repository

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(viewModel: viewModel, repository: repository)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var viewModel: BloodViewModel
    var repository: Repository
    var body: some View {
        ComposeView(viewModel: viewModel, repository: repository)
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}