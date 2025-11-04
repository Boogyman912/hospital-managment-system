# Performance Improvements

This document outlines the performance optimizations made to the Hospital Management System to improve efficiency and reduce database load.

## Summary of Improvements

### 1. N+1 Query Prevention with JOIN FETCH
**Impact:** High - Reduces database round trips significantly

**Changes Made:**
- **AppointmentRepository**: Added `LEFT JOIN FETCH` to eagerly load related entities (Doctor, Patient, Slot) in all query methods
  - `findByDoctorId()`: Now fetches appointment with all related entities in a single query
  - `findByPatientPatientId()`: Loads appointment data with joins to prevent lazy loading issues
  - `findByPhoneNumber()`: Optimized to fetch all related data upfront

**Before:** Each appointment query required 4 separate database queries (1 for appointment + 3 for related entities)
**After:** Single query retrieves all data using JOINs

### 2. Database Aggregation for Rating Calculations
**Impact:** High - Reduces data transfer and computation overhead

**Changes Made:**
- **FeedbackRepository**: Added `calculateAverageRatingByDoctorId()` method using SQL AVG() function
- **FeedbackService**: 
  - `addFeedback()`: Now uses database aggregation instead of fetching all feedbacks and calculating in Java
  - `deleteFeedback()`: Similarly optimized to recalculate average using SQL

**Before:** Fetched all feedbacks, converted DTOs to entities, calculated average in Java (O(n) memory + transfer)
**After:** Single aggregation query on the database (O(1) memory, minimal transfer)

### 3. Removal of Blocking Thread.sleep()
**Impact:** Medium - Improves throughput and resource utilization

**Changes Made:**
- **BillingService.payBill()**: Removed `Thread.sleep(1000)` that was simulating payment processing delay

**Before:** Each payment blocked the thread for 1 second
**After:** Immediate processing (actual payment gateway integration should be non-blocking/async)

### 4. Database-Level Filtering
**Impact:** Medium - Reduces data transfer and application-side processing

**Changes Made:**
- **BillingRepository**: Added `findUnpaidBillsByPatientId()` with status filter in SQL
- **BillingService.getUnpaidBillsByPatientId()**: Now uses optimized repository query
- **SlotRepository.findByDoctorAndStatus()**: Added date filtering (`>= CURRENT_DATE`) in SQL
- **SlotService.getAvailableSlotsByDoctor()**: Removed Java-side date filtering loop

**Before:** Fetched all records and filtered in Java
**After:** Database filters data before transfer

### 5. Database Indexes
**Impact:** High - Dramatically improves query performance for large datasets

**Indexes Added:**
- **Doctor**: `specialization`, `email`, `phone_number`
- **Patient**: `phoneNumber`, `email`
- **Slot**: Composite index on `(doctor_id, date, status)`, plus individual indexes on `status` and `date`
- **Appointment**: `doctor_id`, `patient_id`, `appointmentStatus`, `paymentStatus`
- **Billing**: `patient_id`, `status`, `prescription_id`
- **Inventory**: Composite index on `(itemName, brandName)`, `quantity`

**Impact:** These indexes enable fast lookups on frequently queried columns, especially beneficial as data grows.

### 6. Batch Insert Optimization
**Impact:** Medium - Reduces database round trips for bulk operations

**Changes Made:**
- **DoctorService.createDoctors()**: Changed from individual `save()` calls in a loop to `saveAll()` batch operation

**Before:** N database round trips for N doctors
**After:** Single batch insert (implementation-dependent, but typically much faster)

**⚠️ Behavioral Change:** The previous implementation allowed partial success (some doctors saved, others skipped on error). The new batch implementation uses all-or-nothing semantics - if any doctor fails validation, the entire batch fails. This is more efficient but changes the error handling behavior.

### 7. Inventory Lookup Caching
**Impact:** Medium - Eliminates duplicate database queries

**Changes Made:**
- **BillingService.generateBillByPrescriptionId()**: Added HashMap cache to store inventory items during bill generation
  - Prevents looking up the same inventory item multiple times for duplicate medications
  - Uses cache key format: `itemName|brandName`

**Before:** 2N queries for N medications (lookup for price + lookup for quantity decrease)
**After:** N queries maximum (one per unique medication)

### 8. Reduced Redundant Database Calls
**Impact:** Low-Medium - Eliminates unnecessary queries

**Changes Made:**
- **BillingService.generateBillByPrescriptionId()**: Reorganized to use cached inventory lookups (see #7 above)

## Performance Metrics Estimation

For a typical appointment query:
- **Before:** 4 database queries per appointment × N appointments
- **After:** 1 database query for all appointments

For feedback rating calculation with 100 feedbacks:
- **Before:** 101 queries (1 to find DTOs + 100 to fetch entities) + Java calculation
- **After:** 1 aggregation query

For slot filtering with 1000 slots:
- **Before:** Transfer 1000 rows, filter in Java
- **After:** Database filters, transfers only matching rows

## Recommendations for Future Optimization

1. **Implement Caching**: Add Redis/Spring Cache for frequently accessed, rarely-changed data (doctors, lab test prices, inventory items)

2. **Pagination**: Implement pagination for `findAll()` methods to avoid loading entire tables

3. **Async Processing**: Make payment processing and billing generation asynchronous using Spring's `@Async` or message queues

4. **Connection Pooling**: Ensure proper database connection pooling configuration (HikariCP settings)

5. **Query Result Caching**: Enable Hibernate second-level cache for read-heavy entities

6. **Database Monitoring**: Add query performance monitoring to identify slow queries in production

7. **Lazy Loading Strategy**: Review fetch strategies on relationships - consider making some LAZY instead of EAGER where appropriate

## Testing Recommendations

To validate these improvements:

1. **Load Testing**: Use JMeter or Gatling to compare before/after performance
2. **Query Logging**: Enable Hibernate SQL logging to verify JOIN FETCH queries
3. **Database Profiling**: Use EXPLAIN ANALYZE to verify indexes are being used
4. **Monitoring**: Track query execution times, connection pool usage, and response times

## Migration Notes

The index additions require database migrations. When deploying:
- JPA will attempt to create indexes automatically on first run
- For production, consider creating indexes manually during a maintenance window
- Monitor index creation on large tables as it may lock tables temporarily
