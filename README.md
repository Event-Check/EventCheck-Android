EventCheck
EventCheck is an Android event registration and attendance management
app designed to make event check-in simple, fast, and reliable.
The project contains two app experiences in the same Android project:
User app --- attendees register, verify their email, and receive
a unique QR event ticket.
Scanner app --- event staff scan attendee QR codes, check
attendees in, and view real-time attendance statistics.
✨ Features
👤 Attendee App
Register with name and email.
Email verification using a one-time verification code.
Resend verification code when needed.
Generate a unique QR ticket after successful verification.
Display attendee information with the generated ticket.
Show the QR code at the event entrance for check-in.
Registration and ticket states with loading/error handling.
📷 Scanner App
Dedicated scanner experience for event staff.
Scan attendee QR codes using the device camera.
Check in attendees using their QR token.
Prevent duplicate check-ins through backend validation.
Display the attendee's name and check-in status.
View event statistics.
Refresh attendance statistics in real time.
🔄 User Flow
``` text
Enter Details
     │
     ▼
Email Verification
     │
     ▼
QR Event Ticket
     │
     ▼
Show QR at Event Entrance
     │
     ▼
Staff Scans QR
     │
     ▼
Attendance Confirmed
```
📱 Screenshots
User / Attendee App
Splash Screen
```{=html}
<p align="center">
```
`<img src="screenshots/user/01_splash.png" width="220" alt="EventCheck user app splash screen">`{=html}
```{=html}
</p>
```
Registration
```{=html}
<p align="center">
```
`<img src="screenshots/user/02_registration_empty.png" width="220" alt="Registration screen">`{=html}
`<img src="screenshots/user/03_registration_filled.png" width="220" alt="Filled registration screen">`{=html}
```{=html}
</p>
```
Email Verification
```{=html}
<p align="center">
```
`<img src="screenshots/user/04_email_verification.png" width="220" alt="Email verification screen">`{=html}
```{=html}
</p>
```
Event Ticket
```{=html}
<p align="center">
```
`<img src="screenshots/user/05_event_ticket.png" width="220" alt="Event ticket with QR code">`{=html}
```{=html}
</p>
```
Scanner App
Scanner Splash Screen
```{=html}
<p align="center">
```
`<img src="screenshots/scanner/01_splash.png" width="220" alt="EventCheck scanner app splash screen">`{=html}
```{=html}
</p>
```
Scanner Home
```{=html}
<p align="center">
```
`<img src="screenshots/scanner/02_scanner_home.png" width="220" alt="Scanner home screen">`{=html}
```{=html}
</p>
```
Event Statistics
```{=html}
<p align="center">
```
`<img src="screenshots/scanner/03_event_statistics.png" width="220" alt="Event attendance statistics">`{=html}
```{=html}
</p>
```
🏗️ Architecture
The Android client follows a modern layered architecture with a clear
separation between presentation and data responsibilities.
``` text
Presentation
   │
   ├── Compose UI
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
The project uses Hilt for dependency injection and Jetpack
Compose for the UI.
🛠️ Tech Stack
Android
Kotlin
Jetpack Compose
Material 3
Hilt
Retrofit
Gson
CameraX
ML Kit Barcode Scanning
ZXing QR Code
Navigation 3
Kotlin Coroutines
ViewModel
Backend
Spring Boot
Kotlin
JDK 21
PostgreSQL
Docker
Mailpit
REST API
Email verification
QR token generation
📦 App Flavors
EventCheck uses two Android flavors within the same project:
Flavor      Purpose
---
`user`      Attendee registration and QR ticket
`scanner`   Staff QR scanning and attendance statistics
This keeps both experiences in one codebase while allowing each build to
have its own entry point, UI, theme, and functionality.
🔌 API Overview
The Android app communicates with the EventCheck backend through REST
APIs.
Method   Endpoint                         Purpose
---
`POST`   `/api/v1/registrations`          Create a registration
`POST`   `/api/v1/registrations/verify`   Verify attendee email
`POST`   `/api/v1/registrations/resend`   Resend verification code
`POST`   `/api/v1/check-in`               Check in an attendee using QR token
`GET`    `/api/v1/admin/stats`            Get attendance statistics
🗄️ Backend Flow
``` text
Attendee
   │
   │ Registration
   ▼
Spring Boot API
   │
   ├── PostgreSQL
   │
   └── Email Verification
          │
          ▼
     Verified Attendee
          │
          ▼
      QR Ticket
          │
          ▼
    Scanner App
          │
          ▼
       Check-in
          │
          ▼
 Attendance Statistics
```
🚀 Getting Started
Prerequisites
Make sure you have:
Android Studio
JDK 21
Android SDK
A running EventCheck backend
A PostgreSQL database for the backend
Docker Desktop if you want to run the backend dependencies through
Docker
Android Configuration
Update the API base URL in the Android project to point to your running
backend.
For an Android emulator, a local backend can typically be accessed
through:
``` text
http://10.0.2.2:8080/
```
> `localhost` inside the Android emulator refers to the emulator itself,
> not your development machine.
Build the User App
``` bash
./gradlew assembleUserDebug
```
Build the Scanner App
``` bash
./gradlew assembleScannerDebug
```
The exact Gradle variant name can vary depending on the configured
flavor/build-type combination.
🔐 Security Notes
Email verification is required before issuing the attendee ticket.
QR tokens are used for event check-in rather than relying only on
attendee names.
Duplicate check-ins are handled by the backend.
Administrative statistics are exposed through dedicated backend
endpoints.
Production deployments should use HTTPS and secure environment-based
configuration for credentials and secrets.
📊 Attendance Statistics
The scanner application provides an overview of the event, including:
Total registrations
Verified registrations
Checked-in attendees
Not checked-in attendees
Attendance rate
This allows event staff to monitor attendance without manually counting
participants.
🎯 Project Goals
EventCheck was designed around a simple event workflow:
Register → Verify → Receive QR Ticket → Scan → Check In → Track
Attendance
The goal is to reduce manual registration and check-in work while giving
event organizers a clear view of attendance.
👩‍💻 Project
EventCheck --- Android event registration and QR-based attendance
system.
Built with Kotlin, Jetpack Compose, Hilt, Retrofit, CameraX, ML Kit,
ZXing, Spring Boot, and PostgreSQL.
