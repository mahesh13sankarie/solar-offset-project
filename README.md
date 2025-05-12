## Member

- Beomsu Koh
- Shreyas Desai (testing university email)
- Asti Dhiya Anzaria
- Gaziza Zarkinbek
- Jeklin Indriani Purba
- Maheshvari Periyasamy Nallasamy

## How to run the application

### Prerequisites

- Docker and Docker Compose installed on your system
  - [Install Docker](https://docs.docker.com/get-docker/)
  - [Install Docker Compose](https://docs.docker.com/compose/install/)
- Git installed for cloning the repository

### Running the Application

1. Clone the repository:

   ```
   git clone https://git.shefcompsci.org.uk/com6103-2024-25/team16/project.git
   cd project
   ```

2. Start all services (frontend, backend, and database):

   ```
   docker-compose up
   ```

   The first build may take a few minutes as it downloads all necessary dependencies.

3. Access the application:
   - Frontend: http://localhost:5173
   - Backend API: http://localhost:8000

### Service Information

- Frontend: React application running on port 5173
- Backend: Spring Boot application running on port 8000
- Database: MySQL running on port 3306
  - Database name: solaroffset
  - Username: root
  - Password: Configured in docker-compose.yml

## API Contract

### Authentication & User Management

| Operation | Method | Endpoint | Description |
|-----------|--------|----------|-------------|
| Register Account | `POST` | `/api/v1/auth/register` | Create a new user account with standard credentials |
| User Login | `POST` | `/api/v1/auth/login` | Authenticate user with email and password |
| Google Authentication | `POST` | `/api/v1/auth/google-login` | Authenticate user with Google credentials |
| OAuth Token Generation | `GET` | `/api/v1/auth/generatetoken` | Generate token after OAuth redirect |
| User Logout | `POST` | `/api/v1/auth/logout` | Terminate user session for both standard and Google auth |
| Password Recovery | `POST` | `/api/v1/auth/forgot-password` | Initiate password reset via email |
| Password Update | `PUT` | `/api/v1/auth/update-password` | Change user password |
| Submit Contact Form | `POST` | `/api/v1/auth/contact-us` | Send user inquiry or feedback |
| Retrieve Inquiries | `GET` | `/api/v1/auth/enquiries` | Get all user inquiries (admin access) |

### Admin Dashboard

| Operation | Method | Endpoint | Description |
|-----------|--------|----------|-------------|
| User Management | `GET` | `/api/v1/dashboard/users` | Retrieve all non-admin users |
| Role Modification | `PUT` | `/api/v1/dashboard/update-role` | Promote/demote user roles (e.g., user to staff) |
| User Removal | `DELETE` | `/api/v1/dashboard/delete-user` | Remove user from system |

### Country Data

| Operation | Method | Endpoint | Description |
|-----------|--------|----------|-------------|
| Countries List | `GET` | `/api/v1/countries` | Retrieve all countries data for main page tables |
| Country Details | `GET` | `/api/v1/countries/{CountryCode}` | Get specific country information |

### Solar Panel Management

| Operation | Method | Endpoint | Description |
|-----------|--------|----------|-------------|
| Panel Collection | `GET` | `/api/v1/panels` | Retrieve all solar panels |
| Country-Specific Panels | `GET` | `/api/v1/panels?code={countryCode}` | Filter panels by country code |
| Panel Details | `GET` | `/api/v1/panels/{id}` | Get specific panel information |
| Panel Creation | `POST` | `/api/v1/panels` | Register new solar panel in system |
| Panel Update | `PUT` | `/api/v1/panels/{id}` | Modify existing panel specifications |
| Panel Deletion | `DELETE` | `/api/v1/panels/{id}` | Remove panel from inventory |

### Transactions & Payments

| Operation              | Method | Endpoint                       | Description                               |
| ---------------------- | ------ | ------------------------------ | ----------------------------------------- |
| Panel Purchase         | `POST` | `/api/v1/payments`             | Process solar panel purchase transaction  |
| Create Transaction     | `POST` | `/api/v1/transaction/add`      | Record new transaction                    |
| All Transactions       | `GET`  | `/api/v1/transaction/all`      | Retrieve complete transaction history     |
| User Transactions      | `GET`  | `/api/v1/transaction/{userid}` | Get transaction history for specific user |
| Staff Transaction View | `GET`  | `/api/v1/transaction/staff`    | View user transactions (staff access)     |
