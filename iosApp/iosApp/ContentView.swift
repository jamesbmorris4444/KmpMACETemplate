import UIKit
import SwiftUI
import shared

struct ComposeView: UIViewControllerRepresentable {
    var repository: Repository

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(repository: repository)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var repository: Repository
    var body: some View {
        ComposeView(repository: repository)
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}
