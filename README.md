# Home Assistant + Android Device Controls API

Super-WIP app for controlling Home Assistant using the upcoming [Android 11 Quick Access Device Controls](https://developer.android.com/preview/features/device-control) feature.

![](demo.gif)

### What's working:

- Fetch devices using Home Assistant's REST API
- Display current state and get updates via the WebSocket API
- Supported device types:
    - Light
    - Switch
    - Vacuum
    - Camera
- Control devices via single tap and slider

### TO-DO:

- Support more device types (Alarm, Climate, etc.)
- Configuration UI (connection params are hardcoded for now)
- Custom UI and controls when long pressing a device

### How to use

First, you need a device/emulator running Android 11 beta. Then clone the repo, set your URL and access token in `Config.kt` and build the app.