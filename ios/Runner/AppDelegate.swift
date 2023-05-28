import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    let delegate = BoostDelegate()
    FlutterBoost.instance().setup(application, delegate: delegate) { engine in
      GeneratedPluginRegistrant.register(with: self)
    }
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
}
