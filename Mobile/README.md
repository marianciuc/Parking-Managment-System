# Mobile Parking Application Overview

This mobile application helps users find parking lots, manage their active parking sessions, and handle payments through
Stripe integration. Here's a breakdown of the core features:

## Main Functionality

1. **Parking Lot Finder**
    - Users can search for and locate available parking lots
    - Likely uses map integration for visual representation of parking options

2. **Parking Session Management**
    - Users can view their active parking sessions
    - Check status of current and past parking sessions
    - Monitor time remaining and other session details

3. **Payment Processing**
    - Integrated with Stripe for secure payment handling
    - Users can make payments for parking sessions
    - Likely includes payment history and receipt functionality

There are two ways to build and run this application.

1. **Using Expo Go**: This is a quick and straightforward method. However, this approach does not support native
   functionalities like **maps**, **Stripe integration**, and other custom native modules, which may lead to bugs or
   limitations.

2. **Native Build**: Fully utilizing the application's features (e.g., maps, Stripe) requires a native build. Below are
   the detailed instructions for building the application on **Apple devices**.

---

## Prerequisites

Before proceeding, make sure the following tools are installed and available on your system:

1. **Node.js** (Version 22 or later)
    - Download and install from [Node.js official website](https://nodejs.org).

2. **Xcode** (Minimum version 16.4)
    - Download and install via the Mac App Store.
    - Ensure you have at least one virtual device configured in **Xcode Simulator**.

3. **Expo CLI**
    - If not installed globally, install Expo CLI using the command below:
      ```bash
      npm install -g expo-cli
      ```

4. **Apple Developer Account (Optional, Recommended)**
    - Having an Apple Developer account will allow configuring signing certificates and deploying to physical devices.

---

## Environment Variables Configuration

This application uses environment variables to manage sensitive information like API keys. This approach enhances security by keeping sensitive data out of the codebase and allows for different configurations across environments.

### Required Environment Variables

The following environment variables are required for the application to function properly:

| Variable | Description | Example |
|----------|-------------|---------|
| `GOOGLE_MAPS_API_KEY` | API key for Google Maps integration, used for parking lot location services | `AIzaSyB...` |
| `STRIPE_PUBLISHABLE_KEY` | Publishable key from Stripe, used for payment processing | `pk_test_...` |
| `MAPBOX_DOWNLOAD_TOKEN` | MapBox token for downloading map resources | `sk.eyJ1...` |

### Setting Up Environment Variables

1. **Create an `.env` file** in the root directory of the project.
2. **Add your environment variables** using the format `VARIABLE_NAME=value`:

   ```
   GOOGLE_MAPS_API_KEY=your_google_maps_api_key
   STRIPE_PUBLISHABLE_KEY=your_stripe_publishable_key
   MAPBOX_DOWNLOAD_TOKEN=your_mapbox_download_token
   ```


## Steps to Build the Application (iOS)

### 1. Navigate to the Project Directory

Start by opening the terminal and navigating to the root folder of the project:

```bash
cd driver_mobile
```

---

### 2. Install Dependencies

Install all required libraries and dependencies for the project by running:

```bash
npm install
```

> **Tip:** If you encounter dependency or version issues:
> - Run `npm audit` to check for vulnerabilities.
> - Use `npm install --legacy-peer-deps` to resolve peer dependency conflicts.

---

### 3. Create a Prebuild

The prebuild process generates all necessary native configurations (e.g., **Pods**, Xcode project files).

```bash
npx expo prebuild --platform ios
```

> **Note:** During the prebuild process:
> - Double-check the log for warnings and errors about your native dependencies.
> - Ensure you have a stable internet connection for resolving dependencies.

---

### 4. Open the Project in Xcode

After the prebuild process completes, open the generated Xcode workspace:

```bash
open ios/parkingapp.xcworkspace
```

### Xcode Adjustments:

1. In Xcode, go to **Target Settings**.
2. Under **Signing & Capabilities**, select your **Team** (developer account).
3. Disable unnecessary capabilities like **Push Notifications** if they are not needed.
4. Review build errors, if any, and check for missing signing certificates.

---

### 5. Run the App Locally

Once the configuration in Xcode is complete, you can run the app in development mode using:

```bash
npx expo run:ios
```

This command launches the app on the chosen iOS simulator (or a connected physical iOS device).

---

## Troubleshooting Tips

1. **CocoaPods Issues**:
    - If you face Pod-related errors, try running:
      ```bash
      cd ios
      pod install
      cd ..
      ```
    - Ensure you have CocoaPods installed (`sudo gem install cocoapods`).

2. **Simulator Not Launching**:
    - Open Xcode and ensure a virtual device is configured under **Devices and Simulators**.

3. **Dependencies Conflicts**:
    - Clean the npm cache using `npm cache clean --force` and reinstall dependencies.

4. **Native Build Errors**:
    - Verify that native modules like Stripe, Maps, etc., are properly installed. Run `expo doctor` to check for common
      issues.

---

## Additional Notes

- If you intend to publish the app to the **Apple App Store**, make sure to:
    1. Configure the **App Bundle Identifier** in Xcode.
    2. Use the `expo build:ios` or `eas build` commands for production-ready builds.
- Always test thoroughly on both emulators and physical devices to ensure optimal performance.

---

With these steps, your mobile application should now be running on iOS devices. If you have any further difficulties,
consult the official [Expo documentation](https://docs.expo.dev/), or feel free to ask for help!