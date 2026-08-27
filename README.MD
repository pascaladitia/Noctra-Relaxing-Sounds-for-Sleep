# Noctra

### Android
To run the application on android device/emulator:  
 - open project in Android Studio and run imported android run configuration  

To build the application bundle:  
 - run `./gradlew :androidApp:assembleDevDebug`  
 - find `.apk` file in `androidApp/build/outputs/apk/dev/debug/androidApp-dev-debug.apk`  

### iOS
To run the application on iPhone device/simulator:  
 - Open `iosApp/iosApp.xcworkspace` in Xcode and run standard configuration  
 - Or use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) for Android Studio  

### Firebase Apple Login
Apple Sign-In uses Firebase provider `apple.com`.

Firebase Console setup:
 - Enable Authentication provider `Apple`.
 - Use this callback URL in Apple Developer Service ID / Firebase Apple provider setup: `https://Noctra-73bf3.firebaseapp.com/__/auth/handler`.
 - Make sure the iOS app identifier `com.pascal.noctra` has the `Sign in with Apple` capability enabled in Apple Developer.
 - Keep `GoogleService-Info.plist` in the iOS target and `google-services.json` in `androidApp`.

### CI/CD
GitHub Actions workflows are in `.github/workflows`.

Runner requirement:
 - Workflows use a self-hosted macOS runner: `runs-on: [self-hosted, macOS]`.
 - Install Xcode, Android SDK, CocoaPods, Ruby, Java 17, Node/npm, and Android command-line tools on that runner.
 - Make sure the runner can execute `./gradlew`, `pod install`, `xcodebuild`, and upload commands.

Branch behavior:
 - Push/merge to `develop`: build Android dev APK and iOS ad-hoc IPA, then upload both to Firebase App Distribution.
 - Push/merge to `main`: build Android prod AAB and upload to Play Store, then build iOS App Store IPA and upload to App Store Connect.

GitHub Secrets to add:
 - `FIREBASE_SERVICE_ACCOUNT_JSON`: Firebase service account JSON with App Distribution access.
 - `FIREBASE_ANDROID_DEV_APP_ID`: Firebase App Distribution Android app id.
 - `FIREBASE_IOS_DEV_APP_ID`: Firebase App Distribution iOS app id.
 - `FIREBASE_DISTRIBUTION_GROUPS`: tester groups, for example `qa,internal`.
 - `ANDROID_KEYSTORE_BASE64`: base64 of the Android upload keystore.
 - `ANDROID_KEYSTORE_PASSWORD`: Android upload keystore password.
 - `ANDROID_KEY_ALIAS`: Android upload key alias.
 - `ANDROID_KEY_PASSWORD`: Android upload key password.
 - `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON`: Google Play Console service account JSON.
 - `APPLE_TEAM_ID`: Apple Developer Team ID.
 - `IOS_CERTIFICATE_P12_BASE64`: base64 of Apple signing certificate `.p12`.
 - `IOS_CERTIFICATE_PASSWORD`: password for the `.p12` certificate.
 - `IOS_KEYCHAIN_PASSWORD`: temporary CI keychain password.
 - `IOS_PROVISIONING_PROFILE_BASE64_DEV`: base64 of iOS ad-hoc provisioning profile for Firebase distribution.
 - `IOS_PROVISIONING_PROFILE_NAME_DEV`: provisioning profile name for dev/ad-hoc export.
 - `IOS_PROVISIONING_PROFILE_BASE64_PROD`: base64 of iOS App Store provisioning profile.
 - `IOS_PROVISIONING_PROFILE_NAME_PROD`: provisioning profile name for App Store export.
 - `APP_STORE_CONNECT_API_KEY_ID`: App Store Connect API key id.
 - `APP_STORE_CONNECT_API_ISSUER_ID`: App Store Connect issuer id.
 - `APP_STORE_CONNECT_API_PRIVATE_KEY`: contents of the `AuthKey_XXXX.p8` private key.

Useful base64 commands:
 - Android keystore: `base64 -i upload-keystore.jks | pbcopy`
 - iOS certificate: `base64 -i certificate.p12 | pbcopy`
 - iOS provisioning profile: `base64 -i profile.mobileprovision | pbcopy`
