# EventCheck 🎟️

EventCheck is an Android event registration and attendance management system built to simplify the complete event check-in process — from attendee registration and email verification to QR-based check-in and attendance statistics.

The project includes **two Android flavors in the same project**:

- **User** — for event attendees.
- **Scanner** — for event staff and organizers.

---

## ✨ Features

### 👤 User App

- Register using name and email.
- Verify email using a verification code.
- Resend the verification code.
- Generate a unique QR ticket after verification.
- Display the attendee's event ticket and QR code.
- Use the QR code for event check-in.

### 📷 Scanner App

- Scan attendee QR codes using the device camera.
- Check attendees in through the backend.
- Show the attendee's information and check-in result.
- Prevent duplicate check-ins.
- View event attendance statistics.

---

## 🔄 How It Works

```text
Attendee
   │
   ▼
Registration
   │
   ▼
Email Verification
   │
   ▼
QR Ticket
   │
   ▼
QR Scan at Event
   │
   ▼
Attendance Check-in
   │
   ▼
Event Statistics
```

---

# 📱 Screenshots

## 👤 User App

### 1. Registration

<table>
  <tr>
    <td align="center" width="33%">
      <img src="https://github.com/user-attachments/assets/de39c804-a9d0-4183-87ba-e1fff727898c" width="200" alt="User App - Registration 1" />
    </td>
    <td align="center" width="33%">
      <img src="https://github.com/user-attachments/assets/f7d8e8bb-eee4-48ca-8fbf-f925d61d5d88" width="200" alt="User App - Registration 2" />
    </td>
    <td align="center" width="33%">
      <img src="https://github.com/user-attachments/assets/c353dd40-4374-44f2-b272-63971ddabb8a" width="200" alt="User App - Registration 3" />
    </td>
  </tr>
</table>

### 2. Email Verification & QR Ticket

<table>
  <tr>
    <td align="center" width="50%">
      <img src="https://github.com/user-attachments/assets/765f9f40-261c-4a2c-9512-ff4ff7cb1561" width="200" alt="User App - Verification" />
    </td>
    <td align="center" width="50%">
      <img src="https://github.com/user-attachments/assets/30f688dd-9d33-4080-9093-44321fa82074" width="200" alt="User App - QR Ticket" />
    </td>
  </tr>
</table>

---

## 📷 Scanner App

### Scanner & Attendance

<table>
  <tr>
    <td align="center" width="33%">
      <img src="https://github.com/user-attachments/assets/c58cf434-bf6f-457c-a815-44a928c3b81c" width="200" alt="Scanner App - Scan 1" />
    </td>
    <td align="center" width="33%">
      <img src="https://github.com/user-attachments/assets/901f7d15-0a3a-4c3a-95b0-293e93dae1d4" width="200" alt="Scanner App - Scan 2" />
    </td>
    <td align="center" width="33%">
      <img src="https://github.com/user-attachments/assets/108330b4-0ff9-4771-8539-b99bcc89662a" width="200" alt="Scanner App - Statistics" />
    </td>
  </tr>
</table>

---

# 📊 Attendance Statistics

The scanner application provides event statistics such as:

- Total registrations.
- Verified attendees.
- Checked-in attendees.
- Remaining attendees.
- Attendance rate.

---

## 🛠️ Android Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Hilt
- Retrofit
- Gson
- CameraX
- ML Kit Barcode Scanning
- ZXing
- Navigation 3
- Kotlin Coroutines
- ViewModel

---

# 📦 Android Flavors

Both applications are maintained in the same Android project using product flavors.

| Flavor | Purpose |
|---|---|
| `user` | Attendee registration, email verification, and QR ticket |
| `scanner` | QR scanning, check-in, and attendance statistics |

This allows the two applications to share common project infrastructure while keeping their user experiences separate.

---

## API
The app talks to the [EventCheck Backend](https://github.com/Event-Check/EventCheck-Backend). See the backend README for the full API documentation.

---

# 🚀 Getting Started

## Prerequisites

- Android Studio
- JDK 21
- Android SDK
- Running EventCheck backend
- PostgreSQL
- Docker Desktop (for backend dependencies)

## Android

Set the API base URL in `AppModule.kt` according to your environment.

**Android emulator** (backend running on your computer):

```text
http://10.0.2.2:8080/api/v1/
```

> `10.0.2.2` allows the Android emulator to access the host machine's `localhost`.

**Physical phone** (same Wi-Fi as your computer):

```text
http://<your-computer-wifi-ip>:8080/api/v1/
```

### Build User App

```bash
./gradlew assembleUserDebug
```

### Build Scanner App

```bash
./gradlew assembleScannerDebug
```

---
