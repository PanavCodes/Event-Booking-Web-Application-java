# Event Booking System

A full-stack Event Booking Web Application designed to allow users to seamlessly browse upcoming events, book tickets, and manage payments. Admins have access to a dedicated dashboard to create, update, and delete events.

## 🚀 Features
- **User Authentication:** Login system distinguishing between regular users and administrators.
- **Event Discovery:** Search and browse upcoming events, complete with sorting and filtering functionalities.
- **Ticket Booking:** Select events and reserve seats with real-time seat availability visualization.
- **Payment Processing:** Integrated mock payment portal simulating transaction handling (UPI, Net Banking, Credit/Debit card).
- **Admin Dashboard:** Total CRUD (Create, Read, Update, Delete) capability for administrators to manage their event roster.
- **Responsive Aesthetics:** Designed with Modern UI glassmorphism aesthetics and animated micro-interactions for a premium feel.

## 🛠️ Technology Stack
- **Frontend:** Vanilla HTML5, CSS3 built with modern design principles (Glassmorphism), and AngularJS for reactive state management.
- **Backend:** Java 17 Spring Boot, utilizing Spring Data JPA and Hibernate for powerful REST API generation.
- **Database:** MySQL relational database.
- **Deployments:**
  - **Frontend:** Deployed to [Vercel](https://vercel.com/) (configured with reverse proxy rewrites to bypass Mixed Content/CORS restrictions natively).
  - **Backend:** Designed for deployment on [DigitalOcean App Platform](https://www.digitalocean.com/products/app-platform) / Droplets.

## 🔌 Setup & Deployment

### Backend Setup (Local)
1. Ensure Java 17 and Maven are installed.
2. Navigate to the `backend` directory.
3. Feed MySQL properties via environment variables or straight inside `application.properties`: `MYSQLHOST`, `MYSQLUSER`, `MYSQLPASSWORD`, etc.
4. Compile and run using Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
5. Backend defaults to running on port `8080`.

### Frontend Setup (Local)
1. Serve the `frontend` directory using any local web server. Example:
   ```bash
   npx serve frontend
   ```
2. Navigate to the served `login.html`.

### Vercel Deployment details (`vercel.json`)
The frontend is pre-configured to deploy on Vercel gracefully and avoid strict CORS / Mixed content security violations. The repository maps inverse proxies natively:
```json
{
  "rewrites": [
    { "source": "/api/(.*)", "destination": "http://<YOUR_BACKEND_IP>:8080/api/$1" }
  ]
}
```
Thus, all API requests safely flow through the `/api` route.

## 👥 Authors
- **PanavCodes**
