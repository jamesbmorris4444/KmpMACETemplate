import SwiftUI
import shared

@main
struct iOSApp: App {

    init() {
        HelperKt.doInitKoin()
    }

    var bloodViewModel = BloodViewModel()
    var repository: RepositoryImpl {
        let repo = RepositoryImpl()
        var width: Int32 { get { return Int32(UIScreen.main.bounds.width) }}
        var height: Int32 { get { return Int32(UIScreen.main.bounds.height) }}
        repo.screenWidth = width
        repo.screenHeight = height
        return repo
    }

	var body: some Scene {
		WindowGroup {
			ContentView(viewModel: bloodViewModel, repository: repository)
		}
	}
}