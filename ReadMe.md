# <h1 align="center">🌴Booking App🌴</h1>
#### <h4 align="center"> `The best application to plan your vacation!` </h4>

### 👋 Introduction
`The Booking application aims to provide an efficient and user-friendly platform for booking . It helps to both, customers and administrators, allowing customers to browse available accommodations, make reservations, and process payments securely, while administrators can manage accommodations, user roles, and track payments`

### 👩‍💻 Technologies Used
`The following technologies are used to build the Booking application:`
- ☕ **Java**: The primary programming language used for the application.
- 🌱 **Spring Boot**: A powerful framework that provides essential features for building web applications.
- 🌱🛢️ **Spring Data JPA**: Simplifies data access and persistence with JPA (Java Persistence API).
- 🌱🛡️ **Spring Security**: Enables robust and secure authentication and authorization mechanisms.
- 🗎 **Swagger**: Provides API documentation.
- 🐬 **MySQL**: The database management system used for data storage.
- <img src="https://img.icons8.com/?size=256&id=TCnKnYZFoOzM&format=png" width="30" alt=""/> **Telegram API**: Used to send notifications to administrators via Telegram.
- 🐋  **Docker**: Used for containerization of the application and database.
- 💳 **Stripe API**: Integrated with Stripe for secure payment processing.
- 🌶️ **Lombok**: Reduces boilerplate code with annotations.
- ↔️ **MapStruct**: Simplifies object mapping between DTOs and entities.

### ❓ How to use
`Before running the Booking app, ensure you have the following installed:`
- ☕ Java Development Kit (JDK)
- 🐋 Docker and Docker Compose

`Follow the steps below to install:`
1. Clone the repository from GitHub and navigate to the project directory.
2. Create a `.env` file with the necessary environment variables. (See `.env-sample` for a sample.)
3. Run the following command to build and start the Docker containers:
   `docker-compose up --build`.
4. The application should now be running at `http://localhost:8081`.

### 👷 Project architecture:
![architecture.png](assets%2Farchitecture.png)

### 🛢️ Database structure:
#### <h4 align="center"> ![DbPlan.png](assets%2FDbPlan.png) </h4>

###  API Endpoints
`The Booking app provides the following API endpoints:`

| **HTTP method** | **Endpoint**               | **Role**   | **Function**                                      |
|:----------------|:---------------------------|------------|:--------------------------------------------------|
| POST            | /register                  | ALL        | Register a new user.                              |
| POST            | /login                     | ALL        | Get JWT token for authentication.                 |
| PUT             | /users/{id}/role           | ADMIN      | Update user role.                                 |
| GET             | /users/me                  | ALL        | Get current user's profile info.                  |
| PUT             | /users/me                  | ALL        | Update current user's profile info.               |
| POST            | /accommodations            | ADMIN      | Add a new accommodation.                          |
| GET             | /accommodations            | ALL        | Get a list of all accommodations.                 |
| GET             | /accommodations/{id}       | ALL        | Get detailed information about a accommodation.   |
| PUT             | /accommodations/{id}       | ADMIN      | Update a accommodation information.               |
| DELETE          | /accommodations/{id}       | ADMIN      | Delete accommodation.                             |
| POST            | /bookings                  | ADMIN/USER | Add a new booking.                                |
| GET             | /bookings/my               | ADMIN/USER | Get bookings by authenticated user principals     |
| GET             | /bookings/{id}             | ADMIN/USER | Get specific booking by ID.                       |
| PUT             | /bookings/{id}             | ADMIN/USER | Updates check_in and check_out dates for booking  |
| DELETE          | /bookings/{id}             | ADMIN/USER | Cancels specific booking by booking id            |
| GET             | /bookings                  | ADMIN      | Get booking by status and user id                 |
| POST            | /payments/{bookingId}      | ADMIN/USER | Create a payment session for a booking.           |
| GET             | /payments                  | ADMIN/USER | Redirects user to stripe payment page             |
| GET             | /payments/history/{userId} | ADMIN/USER | Get payments history by user ID.                  |
| GET             | /payments/history/me       | ADMIN/USER | Get payments history for current user.            |
| GET             | /payments/success          | ALL        | Endpoint for handling successful Stripe payments. |
| GET             | /payments/cancel           | ALL        | Endpoint for handling canceled Stripe payments.   |

### 📱 Telegram Notifications
`The Booking app sends Telegram notifications for the following events:`
- New booking created: Notifications are sent to all service administrators.
- Expired booking: If a booking is expired (check_in date is in the past, and the booking is still not confirmed), notifications are sent to all service administrators.

### 🤝 Contribution Guidelines
`We welcome contributions to this project:`

Please create a new branch for each feature or bug fix and submit a pull request to the `main` branch. All PRs must be reviewed and approved by at least one team member before merging.