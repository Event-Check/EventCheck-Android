# EventCheck 🎟️

EventCheck is an Android event registration and attendance management system built to simplify the complete event check-in process — from attendee registration and email verification to QR-based check-in, attendance statistics, and report exports.

The project includes **two Android flavors in the same project**:

- **User** — for event attendees.
- **Scanner** — for event staff and organizers.

---

## ✨ Features

### 👤 User App

- Register using name and email.
- Verify email using a 6-digit verification code.
- Resend the verification code.
- Generate a unique QR ticket after verification.
- Display the attendee's event ticket and QR code.
- Use the QR code for event check-in.

### 📷 Scanner App

- Scan attendee QR codes using the device camera (CameraX + ML Kit).
- Check attendees in through the backend.
- Show attendee name, email, and registration ID on scan.
- Prevent duplicate check-ins.
- View real-time event attendance statistics and progress.
- **Export Attendance Reports**:
  - Export report summary & attendee list in **PDF** or **Excel** (`.xlsx`) formats.
  - Choose whether to include the detailed attendee list in exports.
  - Saves reports directly to the device's public `Downloads` folder.
  - **Open File Feature**: Launch and view exported PDF/Excel files instantly in external viewer apps via `FileProvider`.

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
Event Statistics & Export Report (PDF / Excel)
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

### Scanner, Attendance & Reports

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/c58cf434-bf6f-457c-a815-44a928c3b81c" width="180" alt="Scanner App - Scan 1" />
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/901f7d15-0a3a-4c3a-95b0-293e93dae1d4" width="180" alt="Scanner App - Scan 2" />
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/837ed338-d1c4-4d0d-862d-13d6a6c566e5"  width="180" alt="Scanner App - Statistics" />
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/cef9bbe2-4280-45be-b526-64fc4ba47ebe" width="180" alt="Scanner App - Success" />
    </td>
  </tr>
</table>

---

# 📊 Attendance Statistics & Export Reports

The scanner application provides comprehensive attendance analytics and export capabilities:

- **Real-Time Overview**:
  - Total registrations.
  - Verified attendees.
  - Checked-in attendees.
  - No-shows / remaining attendees.
  - Attendance rate percentage.

- **Report Export System**:
  - **PDF Export**: Generates a clean PDF document containing attendance summary and attendee details.
  - **Excel Export**: Generates a multi-sheet `.xlsx` spreadsheet (`Summary` sheet + `Attendees` sheet).
  - **Include Attendees Toggle**: Option to toggle inclusion of full attendee table in reports.
  - **Instant File Viewer**: Success dialog with an **"Open File"** button to launch PDF/Excel viewers via `FileProvider`.

---

## 🛠️ Android Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Hilt (Dependency Injection)
- Retrofit & OkHttp
- HttpLoggingInterceptor
- Gson
- CameraX
- ML Kit Barcode Scanning
- ZXing (QR Generator)
- Navigation 3
- FileProvider & MediaStore API
- Kotlin Coroutines & Flow
- ViewModel Architecture

---

# 📦 Android Flavors

Both applications are maintained in the same Android project using product flavors.

| Flavor | Purpose |
|---|---|
| `user` | Attendee registration, email verification, and QR ticket display |
| `scanner` | QR scanning, check-in, attendance statistics, and PDF/Excel report exports |

This allows the two applications to share common project infrastructure while keeping their user experiences separate.

---

## 🌐 API Overview

The app communicates with the [EventCheck Backend](https://github.com/Event-Check/EventCheck-Backend). 

### Main Endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/api/v1/registrations` | Public | Register new attendee |
| `POST` | `/api/v1/registrations/verify` | Public | Verify email code & return `qrToken` |
| `POST` | `/api/v1/registrations/resend-verification` | Public | Resend verification email |
| `POST` | `/api/v1/check-in` | Admin | Validate QR token and mark attendance |
| `GET` | `/api/v1/admin/stats` | Admin | Fetch real-time attendance counts |
| `GET` | `/api/v1/admin/report/summary` | Admin | Fetch dashboard summary numbers |
| `GET` | `/api/v1/admin/report/export` | Admin | Download PDF or Excel report file (`format=pdf\|excel`) |

---

# 🚀 Getting Started

## Prerequisites

- Android Studio
- JDK 21
- Android SDK
- Running EventCheck backend
- PostgreSQL
- Docker Desktop (for backend dependencies)

## Android Setup

Set the API base URL in `AppModule.kt` according to your environment.

**Android Emulator** (backend running on your computer):

```text
http://10.0.2.2:8080/api/v1/
```

> `10.0.2.2` allows the Android emulator to access the host machine's `localhost`.

**Physical Phone with ADB Port Forwarding**:

```bash
adb reverse tcp:8080 tcp:8080
```

Set URL in `AppModule.kt`:

```text
http://127.0.0.1:8080/api/v1/
```

**Physical Phone via Wi-Fi**:

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
## 📄 License

EventCheck is proprietary software.

The source code is publicly available for portfolio, educational, and
evaluation purposes only. Copying, modifying, redistributing, or commercially
using the source code is not permitted without prior written permission.

See the [LICENSE](LICENSE) file for the full license terms.

Copyright © 2026 Hend Sayed. All rights reserved.
