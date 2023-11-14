import UIKit
import SwiftUI

struct ComposeView: UIViewControllerRepresentable {
    var viewModel: BloodViewModel
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(viewModel: viewModel, repository: Repository())
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var viewModel = BloodViewModel()
    var body: some View {
        ComposeView(viewModel)
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}



