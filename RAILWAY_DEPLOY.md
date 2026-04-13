# Railway Deployment Guide

## Prerequisites
- Railway account (railway.app)
- MySQL plugin added to Railway project
- Maven installed locally (for building)

## Backend Deployment (Spring Boot + MySQL)

### 1. Backend Configuration
The backend is already configured in `backend-spring/src/main/resources/application.properties` with Railway MySQL environment variables:
- `MYSQLHOST` - MySQL hostname
- `MYSQLPORT` - MySQL port (default 3306)
- `MYSQLDATABASE` - Database name
- `MYSQLUSER` - MySQL username
- `MYSQLPASSWORD` - MySQL password

### 2. Deploy Backend to Railway
```bash
cd backend-spring
mvn clean package -DskipTests
railway login
railway init
railway up
```

### 3. Add MySQL Database
```bash
railway add mysql
```

### 4. Set Environment Variables (if needed)
```bash
railway variables set MYSQLUSER=root
railway variables set MYSQLPASSWORD=your_password
railway variables set MYSQLDATABASE=event_booking
```

### 5. Get Backend URL
```bash
railway domain
```

## Frontend Deployment

### Option 1: Vercel (Recommended for static files)
The frontend is already configured in `frontend/vercel.json`.

```bash
cd frontend
vercel deploy
```

### Option 2: Railway Static Hosting
Create `railway.json` in frontend folder:
```json
{
  "$schema": "https://railway.app/railway.json",
  "build": {
    "builder": "static",
    "publicDir": "."
  }
}
```

Then deploy:
```bash
cd frontend
railway up
```

## Update Frontend API URL

After deploying backend, update the API_BASE in all frontend files:
- `login.html`
- `events.html`
- `booking.html`
- `admin.html`
- `payment.html`
- `index.html`

Replace:
```javascript
var API_BASE = 'https://event-booking-web-application-production-6550.up.railway.app/api';
```

With your new Railway backend URL.

## Project Structure
```
WP_project/
├── backend-spring/          # Spring Boot backend
│   ├── src/
│   │   └── main/
│   │       ├── java/com/eventbooking/
│   │       │   ├── controller/   # REST controllers
│   │       │   ├── model/       # JPA entities
│   │       │   └── repository/  # Data repositories
│   │       └── resources/
│   │           └── application.properties
│   ├── pom.xml
│   ├── Procfile              # Railway startup command
│   └── railway.json          # Railway config
│
├── frontend/              # Static HTML frontend
│   ├── login.html
│   ├── events.html
│   ├── booking.html
│   ├── admin.html
│   ├── payment.html
│   ├── index.html
│   └── vercel.json          # Vercel config
│
└── README.md
```

## Testing Locally

```bash
# Backend
cd backend-spring
mvn spring-boot:run

# Frontend
# Simply open frontend/index.html in browser
# Or use a local server:
cd frontend
python -m http.server 8080
```