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
- Handle loading, success, and error states.

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

### Registration & Verification

<p align="center">
  <img src="screenshots/user/01.jpg" width="220" alt="User App Screenshot 1" />
  <img src="screenshots/user/02.jpg" width="220" alt="User App Screenshot 2" />
  <img src="screenshots/user/03.jpg" width="220" alt="User App Screenshot 3" />
</p>

### QR Ticket

<p align="center">
  <img src="screenshots/user/04.jpg" width="220" alt="User App Screenshot 4" />
  <img src="screenshots/user/05.jpg" width="220" alt="User App Screenshot 5" />
</p>

---

## 📷 Scanner App

### Scanner & Attendance

<p align="center">
  <img src="screenshots/scanner/01.jpg" width="220" alt="Scanner App Screenshot 1" />
  <img src="screenshots/scanner/02.jpg" width="220" alt="Scanner App Screenshot 2" />
  <img src="screenshots/scanner/03.jpg" width="220" alt="Scanner App Screenshot 3" />
</p>

---

# 🏗️ Architecture

The Android application follows a layered architecture with separate presentation and data responsibilities.

```text
Presentation
│
├── Jetpack Compose UI
├── ViewModels
└── UI State
│
▼
Data
│
├── Retrofit API
├── DTOs
└── Event Data Source
│
▼
Backend API
│
├── Registration
├── Email Verification
├── QR Check-in
└── Event Statistics
```

---

# 🛠️ Tech Stack

## Android

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

## Backend

- Kotlin
- Spring Boot
- JDK 21
- PostgreSQL
- Docker
- Mailpit
- REST API
- QR generation
- Email verification

---

# 📦 Android Flavors

Both applications are maintained in the same Android project using product flavors.

| Flavor | Purpose |
|---|---|
| `user` | Attendee registration, email verification, and QR ticket |
| `scanner` | QR scanning, check-in, and attendance statistics |

This allows the two applications to share common project infrastructure while keeping their user experiences separate.

---

# 🔌 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/registrations` | Create a registration |
| `POST` | `/api/v1/registrations/verify` | Verify attendee email |
| `POST` | `/api/v1/registrations/resend` | Resend verification code |
| `POST` | `/api/v1/check-in` | Check in attendee using QR token |
| `GET` | `/api/v1/admin/stats` | Get event statistics |

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

Configure the API base URL according to your environment.

For an Android emulator, if the backend is running locally on your computer, use:

```text
http://10.0.2.2:8080/
```

> `10.0.2.2` allows the Android emulator to access the host machine's `localhost`.

### Build User App

```bash
./gradlew assembleUserDebug
```

### Build Scanner App

```bash
./gradlew assembleScannerDebug
```

---

# 🔐 Backend

The backend is responsible for:

- Registration management.
- Email verification.
- QR token generation.
- Attendee check-in.
- Duplicate check-in validation.
- Attendance statistics.

The backend uses PostgreSQL for persistent data and Mailpit for local email testing during development.

---

# 📊 Attendance Statistics

The scanner application provides event statistics such as:

- Total registrations.
- Verified attendees.
- Checked-in attendees.
- Remaining attendees.
- Attendance rate.

---

# 🎯 Project Flow

**Register → Verify Email → Receive QR Ticket → Scan QR → Check In → Track Attendance**

EventCheck provides a simple digital workflow for managing event registration and attendance without relying on manual check-in.

---

## 👩‍💻 Project

**EventCheck** — Android event registration and QR-based attendance management system.

Built with **Kotlin, Jetpack Compose, Hilt, Retrofit, CameraX, ML Kit, ZXing, Spring Boot, and PostgreSQL**.
