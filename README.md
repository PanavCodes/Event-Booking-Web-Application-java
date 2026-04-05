# 🎫 Event Booking System — Full Stack

A full-stack web application for browsing events, managing events, and booking tickets. Originally built solely with frontend technologies, it has been migrated to use a **Node.js/Express backend** and a **MySQL database**.

---

## 📸 Features

### 👤 User Flow
- **Login** (Auto-registration for new users)
- **Browse Events** (Dynamic data from database)
- **Book Tickets** (Checks availability, reserves seats)
- **Make Payment** (Records transaction, shows confetti success)

### 🔧 Admin Flow
- **Login (Admin)** 
- **Manage Events** (Add / Update / Delete events via REST APIs)

---

## 🛠️ Architecture & Technologies Used

### Frontend
- **HTML5 & CSS3** (Glassmorphism, dark theme, micro-animations)
- **AngularJS 1.8.3** (Data binding, controllers, HTTP requests via `$http`)

### Backend
- **Node.js & Express** (RESTful APIs, static file serving)
- **MySQL2** (Relational database with connection pooling)
- **CORS** (Cross-origin resource sharing setup)

---

## 📁 Project Structure

```
WP_project/
├── backend/
│   ├── server.js          # Main Express server (serves frontend & APIs)
│   ├── db.js              # MySQL connection pool
│   ├── DBMS_proj.sql      # Database Schema
│   ├── package.json       # Node dependencies
│   └── routes/
│       ├── users.js       # Login / Auto-register API
│       ├── events.js      # Event CRUD APIs
│       └── bookings.js    # Bookings & Payments APIs
│
├── frontend/
│   ├── login.html         # Login page
│   ├── events.html        # Browse events
│   ├── admin.html         # Admin dashboard
│   ├── booking.html       # Ticket booking
│   └── payment.html       # Payment confirmation
│
└── README.md              # Project documentation
```

---

## ⚙️ How to Run

### Prerequisites
1. **Node.js** installed
2. **MySQL** installed and running

### Step 1: Database Setup
1. Open MySQL Command Line or Workbench.
2. Ensure your local MySQL user is `root` with the password `panav` (or update `backend/db.js` if different).
3. Import the SQL schema file:
```bash
mysql -u root -ppanav < backend/DBMS_proj.sql
```

### Step 2: Start the Backend
1. Open a terminal in the `backend` folder:
```bash
cd backend
```
2. Install dependencies:
```bash
npm install
```
3. Start the server (this will automatically seed default accounts and events on the first run):
```bash
npm start
# OR
node server.js
```

### Step 3: Access the App
Open your browser and navigate to:
👉 **[http://localhost:3000/login.html](http://localhost:3000/login.html)**

---

## 🔑 Login Credentials

The server automatically seeds the following credentials on the first run:

| Role | Email | Password |
|------|-------|----------|
| **Admin** | `admin@gmail.com` | `admin123` |
| **User** | `user@gmail.com` | `admin123` |

> *Note: If a user enters an email that does not exist in the database, the backend will automatically register them as a new user with the provided password.*

---

## 📝 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| **POST** | `/api/login` | Authenticates User/Admin. Auto-registers new users. |
| **GET**  | `/api/events` | Retrieves all events. |
| **POST** | `/api/events` | Creates a new event (Admin only). |
| **PUT**  | `/api/events/:id` | Updates an event (Admin only). |
| **DELETE**| `/api/events/:id` | Deletes an event (Admin only). |
| **POST** | `/api/bookings` | Creates a booking & decrements available seats. |
| **POST** | `/api/payments` | Records a payment for a booking. |

---

## 🎨 Design Highlights

- 🌙 **Dark mode** theme across all pages
- 🪟 **Glassmorphism** card effects with backdrop blur
- ✨ **Micro-animations** — hover effects, fade-ins, ripple clicks
- 🎊 **Confetti celebration** on successful payment
- 🎯 **Step indicator** on payment page (Select → Book → Pay)

---

## 🧑‍💻 Author

**Panav**
