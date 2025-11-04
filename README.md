# Hospital Management System

A comprehensive hospital management system built with Spring Boot backend and React frontend, featuring doctor management, patient registration, and appointment booking with payment processing.

## Features

### 1. Doctor Management
- Add new doctors with complete information
- View all doctors with their specializations
- Doctor authentication system
- Active/Inactive status management

### 2. Patient Registration
- Patient registration with personal details
- Emergency contact information
- Age calculation from date of birth
- Patient history tracking

### 3. Appointment Booking System
- **Step 1**: Select a doctor from available specialists
- **Step 2**: Choose from available time slots
- **Step 3**: Enter patient information
- **Step 4**: Confirm appointment details
- **Step 5**: Process payment (held status until payment)
- **Step 6**: Complete booking with confirmation

### 4. Slot Management
- Create time slots for doctors
- Hold slots during booking process
- Release slots if booking is cancelled
- Online/Offline appointment options

### 5. Payment Processing
- Multiple payment methods (Card, UPI, Net Banking)
- Receipt generation
- Payment status tracking
- Appointment confirmation after payment

## Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2 Database** (for development)
- **Maven** for dependency management

### Frontend
- **React 18**
- **Vite** for build tooling
- **Tailwind CSS** for styling
- **Axios** for API calls

## Project Structure

```
hospital-management-system/
├── src/main/java/com/hms/hospital_management_system/
│   ├── models/                 # JPA Entities
│   │   ├── Doctor.java
│   │   ├── Patient.java
│   │   ├── Appointment.java
│   │   ├── Slot.java
│   │   └── Receipt.java
│   ├── controllers/            # REST Controllers
│   │   ├── DoctorController.java
│   │   ├── PatientController.java
│   │   ├── AppointmentController.java
│   │   └── SlotController.java
│   ├── services/              # Business Logic
│   │   ├── DoctorService.java
│   │   ├── PatientService.java
│   │   ├── AppointmentService.java
│   │   ├── SlotService.java
│   │   └── PaymentService.java
│   └── jpaRepository/         # Data Access Layer
│       ├── DoctorRepository.java
│       ├── PatientRepository.java
│       ├── AppointmentRepository.java
│       ├── SlotRepository.java
│       └── ReceiptRepository.java
├── frontend/vite-project/     # React Frontend
│   ├── src/
│   │   ├── App.jsx
│   │   ├── AppointmentBooking.jsx
│   │   └── DoctorManagement.jsx
│   └── package.json
└── pom.xml                    # Maven Configuration
```

## API Endpoints

### Doctor Management
- `GET /api/doctors/` - Get all doctors
- `GET /api/doctors/{id}` - Get doctor by ID
- `POST /api/doctors/create` - Create new doctor
- `GET /api/doctors/specialization/{specialization}` - Get doctors by specialization

### Patient Management
- `GET /api/patients/` - Get all patients
- `GET /api/patients/{id}` - Get patient by ID
- `POST /api/patients/create` - Create new patient

### Appointment Management
- `GET /api/appointments/doctor/{doctor_id}` - Get appointments by doctor
- `GET /api/appointments/patient/{patientId}` - Get appointments by patient
- `GET /api/appointments/{appointmentId}` - Get appointment by ID
- `POST /api/appointments/book` - Book new appointment
- `POST /api/appointments/{appointmentId}/payment` - Process payment
- `POST /api/appointments/{appointmentId}/cancel` - Cancel appointment

### Slot Management
- `GET /api/slots/doctor/{doctor_id}/date/{date}` - Get available slots
- `POST /api/slots/create` - Create slots for doctor
- `POST /api/slots/{slotId}/hold` - Hold a slot
- `POST /api/slots/{slotId}/release` - Release a slot

## Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- Maven 3.6 or higher

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd hospital-management-system
   ```

2. **Run the Spring Boot application**
   ```bash
   mvn spring-boot:run
   ```
   
   The backend will start on `http://localhost:8080`

3. **Database Configuration**
   - The application uses H2 in-memory database by default
   - Database console available at `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave empty)

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd frontend/vite-project
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm run dev
   ```
   
   The frontend will start on `http://localhost:5173`

## Usage Workflow

### 1. Adding Doctors (Admin)
1. Navigate to "Manage Doctors" in the frontend
2. Click "Add New Doctor"
3. Fill in doctor details:
   - Name, Specialization, Username, Password
   - Email, Phone Number, Active Status
4. Click "Add Doctor"

### 2. Booking Appointments (Patients)
1. Navigate to "Book an Appointment"
2. **Step 1**: Select a doctor from the list
3. **Step 2**: Choose an available time slot
4. **Step 3**: Enter patient information:
   - Personal details (name, phone, email, gender, DOB)
   - Choose online or in-person consultation
5. **Step 4**: Review and confirm appointment details
6. **Step 5**: Complete payment (appointment held until payment)
7. **Step 6**: Receive confirmation with appointment ID

### 3. Managing Slots (Admin)
- Create time slots for doctors using the API
- Slots can be held during booking process
- Automatic release if booking is cancelled

## Database Schema

### Key Tables
- **doctors**: Doctor information and credentials
- **patients**: Patient personal information
- **appointments**: Appointment bookings with status
- **slots**: Available time slots for doctors
- **receipts**: Payment receipts and transaction details

### Relationships
- Doctor → Appointments (One-to-Many)
- Patient → Appointments (One-to-Many)
- Slot → Appointment (One-to-One)
- Appointment → Receipt (One-to-One)

## Payment Flow

1. **Appointment Booking**: Patient books appointment with "PENDING" payment status
2. **Slot Holding**: Selected slot is held for 15 minutes
3. **Payment Processing**: Patient completes payment
4. **Confirmation**: Appointment status changes to "PAID"
5. **Receipt Generation**: Payment receipt is generated
6. **Slot Booking**: Slot status changes to "BOOKED"

## Features in Detail

### Slot Management
- **Available**: Slot is open for booking
- **Held**: Slot is temporarily reserved during booking process
- **Booked**: Slot is confirmed after payment

### Appointment Status
- **BOOKED**: Initial booking (payment pending)
- **COMPLETED**: Appointment finished
- **CANCELLED**: Appointment cancelled
- **ISSUE_RAISED**: Issue reported

### Payment Status
- **PENDING**: Payment not yet processed
- **PAID**: Payment confirmed
- **FAILED**: Payment failed

## Development Notes

### CORS Configuration
The backend includes CORS configuration to allow frontend requests from `http://localhost:5173`.

### Error Handling
- Comprehensive error handling in all services
- User-friendly error messages in frontend
- Proper HTTP status codes

### Security Considerations
- Password hashing (implement proper hashing in production)
- Input validation
- SQL injection prevention through JPA

## Future Enhancements

1. **Authentication & Authorization**
   - JWT token-based authentication
   - Role-based access control
   - Session management

2. **Advanced Features**
   - Email/SMS notifications
   - Calendar integration
   - Prescription management
   - Medical history tracking

3. **Database Migration**
   - PostgreSQL/MySQL for production
   - Database migration scripts
   - Connection pooling

4. **Testing**
   - Unit tests for services
   - Integration tests for controllers
   - Frontend component tests

## Troubleshooting

### Common Issues

1. **Port Conflicts**
   - Backend: Change port in `application.properties`
   - Frontend: Change port in `vite.config.js`

2. **Database Connection**
   - Ensure H2 database is properly configured
   - Check database console access

3. **CORS Issues**
   - Verify CORS configuration in `SecurityConfig.java`
   - Check frontend API base URL

4. **Frontend Build Issues**
   - Clear node_modules and reinstall
   - Check Node.js version compatibility

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
