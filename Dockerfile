# Use the official MySQL image from Docker Hub
FROM mysql:latest

# Set the environment variables for MySQL database and user
#ENV MYSQL_DATABASE=company
#ENV MYSQL_USER=root
#ENV MYSQL_PASSWORD=password

# Copy the SQL script to initialize the database schema
COPY init.sql /docker-entrypoint-initdb.d

# Expose the default MySQL port
EXPOSE 3306
