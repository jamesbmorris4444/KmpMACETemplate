import SwiftUI
import shared

@main
struct iOSApp: App {

    init() {
        Helper.initKoin()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}