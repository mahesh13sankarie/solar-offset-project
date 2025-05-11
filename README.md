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
