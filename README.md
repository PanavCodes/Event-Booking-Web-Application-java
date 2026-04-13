# 🎫 Event Booking System — Full Stack

A full-stack web application for browsing events, managing events, and booking tickets. Built with **Spring Boot backend** and **MySQL database**, with a static HTML/AngularJS frontend.

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

## 🛠️ Tech Stack

### Frontend
- **HTML5 & CSS3** (Glassmorphism, dark theme, micro-animations)
- **AngularJS 1.8.3** (Data binding, controllers, HTTP requests)
- **Vercel** / **Railway Static** (Deployment)

### Backend
- **Java 17** (Spring Boot 3.2.5)
- **Spring Data JPA** (ORM)
- **MySQL** (Relational database)
- **Railway** (Deployment platform)

---

## 📁 Project Structure

```
WP_project/
├── backend-spring/           # Spring Boot backend
│   ├── src/main/
│   │   ├── java/com/eventbooking/
│   │   │   ├── controller/   # REST controllers
│   │   │   ├── model/        # JPA entities
│   │   │   ├── repository/   # Data repositories
│   │   │   └── config/       # CORS configuration
│   │   └── resources/
│   │       └── application.properties
│   ├── pom.xml               # Maven dependencies
│   ├── Procfile              # Railway startup
│   └── railway.json          # Railway config
│
├── frontend/                 # Static HTML frontend
│   ├── login.html            # Login page
│   ├── events.html           # Browse events
│   ├── admin.html            # Admin dashboard
│   ├── booking.html          # Ticket booking
│   ├── payment.html          # Payment confirmation
│   ├── index.html            # Home page
│   └── vercel.json           # Vercel config
│
├── RAILWAY_DEPLOY.md         # Deployment guide
└── README.md                 # Project documentation
```

---

## ⚙️ How to Run Locally

### Prerequisites
1. **Java 17** installed
2. **Maven** installed
3. **MySQL** installed and running

### Step 1: Database Setup
1. Create a MySQL database:
```sql
CREATE DATABASE event_booking;
```

### Step 2: Start the Backend
```bash
cd backend-spring
mvn spring-boot:run
```
The server will auto-seed default admin/user accounts and sample events.

### Step 3: Access the App
Open your browser and navigate to:
👉 **[http://localhost:8080](http://localhost:8080)**

---

## 🚂 Railway Deployment

### Backend
```bash
cd backend-spring
mvn clean package -DskipTests
railway init
railway up
railway add mysql
```

### Frontend
```bash
cd frontend
vercel deploy
```

See `RAILWAY_DEPLOY.md` for detailed deployment steps.

---

## 🔑 Login Credentials

| Role | Email | Password |
|------|-------|----------|
| **Admin** | `admin@gmail.com` | `admin123` |
| **User** | `user@gmail.com` | `user123` |

> *Note: If a user enters an email that does not exist in the database, the backend will automatically register them as a new user.*

---

## 📝 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| **GET** | `/api/health` | Health check |
| **POST** | `/api/login` | Authenticates User/Admin. Auto-registers new users. |
| **GET** | `/api/events` | Retrieves all events. |
| **POST** | `/api/events` | Creates a new event (Admin only). |
| **PUT** | `/api/events/:id` | Updates an event (Admin only). |
| **DELETE**| `/api/events/:id` | Deletes an event (Admin only). |
| **POST** | `/api/bookings` | Creates a booking & decrements available seats. |
| **GET** | `/api/bookings/:userId` | Get user's bookings |
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
"# Event-Booking-Web-Application-java" 
