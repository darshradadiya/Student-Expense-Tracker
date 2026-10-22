# Student Expense Tracker 🎓💰

A complete Android application built for **Mobile Application Development (MAD)** student projects using **Java**, **Android Architecture Components (MVVM)**, **Room Database**, and **Material Design Components**.

---

## 📱 Project Purpose & Overview
College and university students often struggle to balance their limited monthly allowances, track everyday expenses (food, travel, stationery, books, outings), and save money. **Student Expense Tracker** is designed with a student-first approach to:
- Track daily expenses with categories and notes.
- Monitor remaining bank balance in real time.
- Set monthly budget limits with automatic threshold alerts (80% and 100%).
- View category-wise spending visual charts (Donut Chart & Horizontal Bar Chart).
- Understand monthly savings calculated automatically using the formula:
  $$\text{Savings} = \text{Monthly Budget} - \text{Total Expenses}$$
- Export professional monthly financial summary reports to PDF.

---

## 🎨 UI/UX Design Specifications
- **Student-Friendly Aesthetic**: Clean, distraction-free cards with soft shadows and rounded corners (16dp).
- **Color Palette**:
  - **White & Light Gray** (`#FFFFFF`, `#F5F7FA`): Fresh, legible background and card surfaces.
  - **Primary Green** (`#2E7D32`, `#1B5E20`): Financial health, positive balances, and primary actions.
  - **Accent Orange** (`#F57C00`, `#E65100`): Expense metrics, warning thresholds, and interactive highlights.
  - **No Blue Backgrounds**: Strictly compliant with the design specification.
- **Dark Mode Support**: Seamless day/night theme toggle with high-contrast surfaces.
- **Responsive Layout**: Designed for diverse Android phone screen sizes using ConstraintLayout and NestedScrollView.

---

## 🚀 Key Features

### 1. Student Authentication & Session
- Clean Student Login screen.
- Pre-populated credentials (`rahul.patel@abc.edu` / `student123`).
- "Remember Me" checkbox with SharedPreferences persistence.
- "Fill Sample Credentials" button for instant MAD examiner demonstration.
- Form validation (email syntax, password length).

### 2. Comprehensive Dashboard
- Student Greeting: *"Hello, Rahul Patel 👋"* with College & Course details.
- 4 High-Impact Stat Cards:
  1. **Bank Balance**: ₹12,500 (Calculated dynamically: Initial Balance ₹20,000 − Total Expenses).
  2. **Total Expenses**: ₹4,500 (Current month total).
  3. **Total Savings**: ₹8,000 (Calculated: Budget ₹12,500 − Total Expenses ₹4,500).
  4. **Total Transactions**: Real-time transaction counter.
- **Budget Limit Alert Banner**: Dynamically warns the student when spending reaches 80% or exceeds 100% of their budget.
- **Quick Action Buttons**: "Add Expense", "View Summary".
- **Recent Expenses Preview**: Top 3 latest transactions with a "View All" link.

### 3. Add & Edit Expense
- Fields: Title, Amount (₹), Category, Date (with interactive `DatePickerDialog`), Notes.
- 6 Student Categories with dedicated icons & badges:
  - 🍕 **Food** (`#FF7043`)
  - 🚌 **Travel** (`#26A69A`)
  - 📚 **Books** (`#43A047`)
  - 🎮 **Entertainment** (`#AB47BC`)
  - 🛍️ **Shopping** (`#FFA726`)
  - 🏷️ **Other** (`#78909C`)
- System Notification triggered on saving expense.

### 4. Search & Filter Expense List
- RecyclerView with DiffUtil for smooth updates.
- Real-time search by title and notes.
- Category filter chips: *All, Food, Travel, Books, Entertainment, Shopping, Other*.
- Edit expense (tap or edit icon).
- Delete expense with Material Design confirmation dialog.
- Clean empty state illustration when no records match.

### 5. Monthly Summary & Interactive Charts
- Monthly Budget vs Spent vs Remaining vs Saved cards.
- **Donut Chart View**: Custom Canvas donut chart showing category distribution with center total.
- **Bar Chart View**: Horizontal comparison bars highlighting category spending.
- Category breakdown list with exact amounts and percentages.

### 6. Student Profile & Settings
- Student identity card: Rahul Patel | ABC College | BCA | STU001.
- Initial Bank Balance & Monthly Budget display.
- **Update Balance & Budget Dialog**: Easily adjust financial limits.
- **Dark Mode Switch**: System/App level toggle.
- **Reset to Sample Data**: One-tap restore to initial MAD project state.
- **Logout**: Safe session clearance.

### 7. Export Monthly Report to PDF
- Uses Android native `android.graphics.pdf.PdfDocument`.
- Generates clean, printable A4 PDF report with college header, student info, financial summary table, and itemized transaction list.
- Integrated with `FileProvider` for opening in any PDF viewer or sharing.

---

## 🏗️ Architecture & Technology Stack
- **Architecture**: MVVM (Model - View - ViewModel) + Repository Pattern.
- **Language**: Java 17.
- **Database**: Android Room ORM (SQLite) with background executor threads.
- **Reactive UI**: Android LiveData, MediatorLiveData, and ViewBinding.
- **Material Components**: `com.google.android.material:material:1.11.0`.
- **Target SDK**: Android 34 (UpsideDownCake) | **Min SDK**: Android 26 (Oreo).

---

## 📂 Project Directory Structure
```
StudentExpenseTracker/
├── app/
│   ├── build.gradle
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/student/expensetracker/
│       │   ├── activities/
│       │   │   ├── SplashActivity.java
│       │   │   ├── LoginActivity.java
│       │   │   ├── MainActivity.java
│       │   │   └── AddEditExpenseActivity.java
│       │   ├── fragments/
│       │   │   ├── DashboardFragment.java
│       │   │   ├── ExpensesFragment.java
│       │   │   ├── ReportsFragment.java
│       │   │   └── ProfileFragment.java
│       │   ├── models/
│       │   │   ├── Expense.java
│       │   │   ├── StudentProfile.java
│       │   │   └── CategorySum.java
│       │   ├── database/
│       │   │   ├── ExpenseDao.java
│       │   │   ├── ProfileDao.java
│       │   │   └── AppDatabase.java
│       │   ├── repository/
│       │   │   └── ExpenseRepository.java
│       │   ├── viewmodels/
│       │   │   ├── ExpenseViewModel.java
│       │   │   └── ProfileViewModel.java
│       │   ├── adapters/
│       │   │   └── ExpenseAdapter.java
│       │   ├── utils/
│       │   │   ├── DateTimeUtils.java
│       │   │   ├── PreferenceHelper.java
│       │   │   ├── NotificationHelper.java
│       │   │   └── PdfExporter.java
│       │   └── views/
│       │       ├── DonutChartView.java
│       │       └── BarChartView.java
│       └── res/
│           ├── drawable/       # Custom vector drawables & badge backgrounds
│           ├── layout/         # Clean XML layouts for all screens & dialogs
│           ├── menu/           # Bottom navigation menu
│           ├── values/         # colors.xml, strings.xml, themes.xml
│           ├── values-night/   # Dark mode themes & colors
│           └── xml/            # file_paths.xml for FileProvider
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle
├── settings.gradle
└── local.properties
```

---

## 🚀 How to Open and Run in Android Studio

1. Open **Android Studio**.
2. Select **File > Open...** (or click **Open** on the Welcome screen).
3. Navigate to and select the folder:
   `C:\Users\HP\.gemini\antigravity\scratch\StudentExpenseTracker`
4. Wait for Gradle Sync to finish automatically.
5. Select an Android Emulator (Pixel 6 / 7 / 8 running API 26 to 34) or connect a physical Android device with USB Debugging enabled.
6. Click the green **Run (▶)** button (or press `Shift + F10`).
7. The app will launch directly into the **Splash Screen** -> **Login Screen** -> **Dashboard**.

---

## 🧑‍🎓 Sample Demo Credentials
- **Student Email**: `rahul.patel@abc.edu`
- **Password**: `student123`
*(Or simply tap the **"Fill Sample Credentials"** button on the login screen!)*
