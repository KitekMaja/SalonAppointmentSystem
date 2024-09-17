# Salon Appointment System

## APPLICATION DESCRIPTION
The Salon Appointment System is designed to simplify the process of booking and managing appointments for manicure and pedicure services. This application is a convenient tool for both customers and technicians, offering an easy-to-use interface for scheduling, availability management, and payments.

Customers can use the system to book appointments with various technicians, choose specific treatments, and make secure payments online. Technicians can manage their schedules, view upcoming appointments, and track their earnings. The system streamlines the entire appointment process, providing clear communication between customers and technicians.

The web interface also allows customers to browse available services and see technician profiles, making it easy to choose the right service provider. Whether for a simple manicure or a full spa experience, the system offers everything a salon needs to manage their appointments efficiently.

## MICROSERVICES
The application consists of the following microservices:

### Appointment Booking Service (REST API)
* Allows customers to book appointments with different technicians for services such as manicures and pedicures.
* Tracks appointments, technician availability, and customer information.
* Integrates with a database to store and manage all appointment-related data.

### Technician Availability Service (REST API)
* Checks the availability of technicians based on their schedules.
* Synchronizes technician schedules with the Appointment Booking Service via gRPC.
* Ensures that appointment slots are updated in real time and prevents overbooking.

### Payment Service (REST API)
* Manages the payment process once a customer books an appointment.
* Integrates with popular payment gateways (e.g., Stripe, PayPal) to process payments.
* Stores payment records and customer transactions in a secure database.

### User Interface
* A web interface where customers can browse services, book appointments, and make payments.
* Technicians can log in to view and manage their schedules and appointments.
