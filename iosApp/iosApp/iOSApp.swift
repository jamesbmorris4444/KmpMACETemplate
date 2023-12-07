import SwiftUI
import Sentry

import shared

@main
struct iOSApp: App {
    init() {
        HelperKt.doInitKoin()
        SentrySDK.start { options in
            options.dsn = "https://cf41e9b6a3a72a9aa0517d51e5a2f89e@o4506251182604288.ingest.sentry.io/4506251420237824"
            options.debug = false
            options.enableTracing = true 

            // Uncomment the following lines to add more data to your events
            // options.attachScreenshot = true // This adds a screenshot to the error events
            // options.attachViewHierarchy = true // This adds the view hierarchy to the error events
        }
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