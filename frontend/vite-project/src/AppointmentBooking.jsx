import React, { useState, useEffect } from 'react';

const API_BASE_URL = 'http://localhost:8080/api';

const AppointmentBooking = () => {
    const [currentStep, setCurrentStep] = useState(1);
    const [doctors, setDoctors] = useState([]);
    const [selectedDoctor, setSelectedDoctor] = useState(null);
    const [availableSlots, setAvailableSlots] = useState([]);
    const [selectedSlot, setSelectedSlot] = useState(null);
    const [patientInfo, setPatientInfo] = useState({
        name: '',
        phoneNumber: '',
        email: '',
        sex: '',
        dob: ''
    });
    const [appointmentDetails, setAppointmentDetails] = useState({
        isOnline: false,
        meetLink: ''
    });
    const [appointment, setAppointment] = useState(null);
    const [paymentDetails, setPaymentDetails] = useState({
        paymentMethod: 'card',
        amount: 100
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    // Load doctors on component mount
    useEffect(() => {
        fetchDoctors();
    }, []);

    const fetchDoctors = async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/doctors/`);
            const data = await response.json();
            setDoctors(data);
        } catch (err) {
            setError('Failed to load doctors');
        }
    };

    const fetchAvailableSlots = async (doctor_id, date) => {
        try {
            const response = await fetch(`${API_BASE_URL}/appointments/slots/doctor/${doctor_id}/date/${date}`);
            const data = await response.json();
            setAvailableSlots(data);
        } catch (err) {
            setError('Failed to load available slots');
        }
    };

    const handleDoctorSelect = (doctor) => {
        setSelectedDoctor(doctor);
        setCurrentStep(2);
        // Load slots for today
        const today = new Date().toISOString().split('T')[0];
        fetchAvailableSlots(doctor.doctor_id, today);
    };

    const handleSlotSelect = (slot) => {
        setSelectedSlot(slot);
        setCurrentStep(3);
    };

    const handlePatientInfoSubmit = (e) => {
        e.preventDefault();
        setCurrentStep(4);
    };

    const handleAppointmentBooking = async () => {
        setLoading(true);
        try {
            // First, create patient
            const patientResponse = await fetch(`${API_BASE_URL}/patients/create`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(patientInfo)
            });

            if (!patientResponse.ok) {
                throw new Error('Failed to create patient');
            }

            // Get the created patient ID (in a real app, you'd get this from the response)
            const patientId = 1; // This should come from the patient creation response

            // Book appointment
            const bookingData = {
                patientId: patientId,
                doctor_id: selectedDoctor.doctor_id,
                slotId: selectedSlot.slotId,
                isOnline: appointmentDetails.isOnline,
                meetLink: appointmentDetails.meetLink
            };

            const appointmentResponse = await fetch(`${API_BASE_URL}/appointments/book`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(bookingData)
            });

            const appointmentData = await appointmentResponse.json();
            
            if (appointmentData.success) {
                setAppointment(appointmentData);
                setCurrentStep(5);
            } else {
                setError(appointmentData.message);
            }
        } catch (err) {
            setError('Failed to book appointment: ' + err.message);
        } finally {
            setLoading(false);
        }
    };

    const handlePayment = async () => {
        setLoading(true);
        try {
            const response = await fetch(`${API_BASE_URL}/appointments/${appointment.appointmentId}/payment`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(paymentDetails)
            });

            const data = await response.json();
            
            if (data.success) {
                setCurrentStep(6);
            } else {
                setError(data.message);
            }
        } catch (err) {
            setError('Payment failed: ' + err.message);
        } finally {
            setLoading(false);
        }
    };

    const renderStep1 = () => (
        <div className="max-w-4xl mx-auto p-6">
            <h2 className="text-3xl font-bold text-white mb-8 text-center">Select a Doctor</h2>
            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                {doctors.map((doctor) => (
                    <div key={doctor.doctor_id} className="bg-gray-800 rounded-lg p-6 hover:bg-gray-700 transition-colors cursor-pointer"
                         onClick={() => handleDoctorSelect(doctor)}>
                        <div className="text-center">
                            <div className="w-16 h-16 bg-blue-600 rounded-full mx-auto mb-4 flex items-center justify-center">
                                <span className="text-white font-bold text-xl">
                                    {doctor.name.charAt(0)}
                                </span>
                            </div>
                            <h3 className="text-xl font-semibold text-white mb-2">{doctor.name}</h3>
                            <p className="text-blue-400 mb-2">{doctor.specialization}</p>
                            <p className="text-gray-400 text-sm">{doctor.email}</p>
                            <p className="text-gray-400 text-sm">{doctor.phone_number}</p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );

    const renderStep2 = () => (
        <div className="max-w-4xl mx-auto p-6">
            <h2 className="text-3xl font-bold text-white mb-8 text-center">Select Time Slot</h2>
            <div className="bg-gray-800 rounded-lg p-6 mb-6">
                <h3 className="text-xl font-semibold text-white mb-2">Dr. {selectedDoctor?.name}</h3>
                <p className="text-blue-400">{selectedDoctor?.specialization}</p>
            </div>
            
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                {availableSlots.map((slot) => (
                    <button
                        key={slot.slotId}
                        onClick={() => handleSlotSelect(slot)}
                        className="bg-gray-700 hover:bg-blue-600 text-white p-4 rounded-lg transition-colors"
                    >
                        <div className="text-center">
                            <div className="font-semibold">{slot.time}</div>
                            <div className="text-sm text-gray-300">
                                {slot.isOnline ? 'Online' : 'In-person'}
                            </div>
                        </div>
                    </button>
                ))}
            </div>
            
            {availableSlots.length === 0 && (
                <div className="text-center text-gray-400 py-8">
                    No available slots for today. Please try another date.
                </div>
            )}
        </div>
    );

    const renderStep3 = () => (
        <div className="max-w-2xl mx-auto p-6">
            <h2 className="text-3xl font-bold text-white mb-8 text-center">Patient Information</h2>
            <form onSubmit={handlePatientInfoSubmit} className="space-y-6">
                <div className="grid md:grid-cols-2 gap-6">
                    <div>
                        <label className="block text-white mb-2">Full Name</label>
                        <input
                            type="text"
                            value={patientInfo.name}
                            onChange={(e) => setPatientInfo({...patientInfo, name: e.target.value})}
                            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-white mb-2">Phone Number</label>
                        <input
                            type="tel"
                            value={patientInfo.phoneNumber}
                            onChange={(e) => setPatientInfo({...patientInfo, phoneNumber: e.target.value})}
                            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required
                        />
                    </div>
                </div>
                
                <div className="grid md:grid-cols-2 gap-6">
                    <div>
                        <label className="block text-white mb-2">Email</label>
                        <input
                            type="email"
                            value={patientInfo.email}
                            onChange={(e) => setPatientInfo({...patientInfo, email: e.target.value})}
                            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>
                    <div>
                        <label className="block text-white mb-2">Gender</label>
                        <select
                            value={patientInfo.sex}
                            onChange={(e) => setPatientInfo({...patientInfo, sex: e.target.value})}
                            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required
                        >
                            <option value="">Select Gender</option>
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                            <option value="Other">Other</option>
                        </select>
                    </div>
                </div>
                
                <div>
                    <label className="block text-white mb-2">Date of Birth</label>
                    <input
                        type="date"
                        value={patientInfo.dob}
                        onChange={(e) => setPatientInfo({...patientInfo, dob: e.target.value})}
                        className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required
                    />
                </div>
                
                <div className="flex items-center space-x-4">
                    <input
                        type="checkbox"
                        id="isOnline"
                        checked={appointmentDetails.isOnline}
                        onChange={(e) => setAppointmentDetails({...appointmentDetails, isOnline: e.target.checked})}
                        className="w-4 h-4 text-blue-600 bg-gray-800 border-gray-700 rounded focus:ring-blue-500"
                    />
                    <label htmlFor="isOnline" className="text-white">Online consultation</label>
                </div>
                
                {appointmentDetails.isOnline && (
                    <div>
                        <label className="block text-white mb-2">Meeting Link (Optional)</label>
                        <input
                            type="url"
                            value={appointmentDetails.meetLink}
                            onChange={(e) => setAppointmentDetails({...appointmentDetails, meetLink: e.target.value})}
                            placeholder="https://meet.google.com/..."
                            className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>
                )}
                
                <button
                    type="submit"
                    className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-6 rounded-lg transition-colors"
                >
                    Continue to Booking
                </button>
            </form>
        </div>
    );

    const renderStep4 = () => (
        <div className="max-w-2xl mx-auto p-6">
            <h2 className="text-3xl font-bold text-white mb-8 text-center">Confirm Appointment</h2>
            
            <div className="bg-gray-800 rounded-lg p-6 mb-6">
                <h3 className="text-xl font-semibold text-white mb-4">Appointment Details</h3>
                <div className="space-y-2 text-gray-300">
                    <p><span className="font-semibold">Doctor:</span> Dr. {selectedDoctor?.name}</p>
                    <p><span className="font-semibold">Specialization:</span> {selectedDoctor?.specialization}</p>
                    <p><span className="font-semibold">Time:</span> {selectedSlot?.time}</p>
                    <p><span className="font-semibold">Type:</span> {appointmentDetails.isOnline ? 'Online' : 'In-person'}</p>
                    <p><span className="font-semibold">Patient:</span> {patientInfo.name}</p>
                    <p><span className="font-semibold">Phone:</span> {patientInfo.phoneNumber}</p>
                </div>
            </div>
            
            <button
                onClick={handleAppointmentBooking}
                disabled={loading}
                className="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-6 rounded-lg transition-colors disabled:opacity-50"
            >
                {loading ? 'Booking...' : 'Book Appointment'}
            </button>
        </div>
    );

    const renderStep5 = () => (
        <div className="max-w-2xl mx-auto p-6">
            <h2 className="text-3xl font-bold text-white mb-8 text-center">Payment Required</h2>
            
            <div className="bg-gray-800 rounded-lg p-6 mb-6">
                <h3 className="text-xl font-semibold text-white mb-4">Appointment Booked!</h3>
                <p className="text-gray-300 mb-4">Your appointment has been booked with status: <span className="text-yellow-400 font-semibold">{appointment?.paymentStatus}</span></p>
                <p className="text-gray-300 mb-4">Please complete payment to confirm your appointment.</p>
            </div>
            
            <div className="space-y-4">
                <div>
                    <label className="block text-white mb-2">Payment Method</label>
                    <select
                        value={paymentDetails.paymentMethod}
                        onChange={(e) => setPaymentDetails({...paymentDetails, paymentMethod: e.target.value})}
                        className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                        <option value="card">Credit/Debit Card</option>
                        <option value="upi">UPI</option>
                        <option value="netbanking">Net Banking</option>
                    </select>
                </div>
                
                <div>
                    <label className="block text-white mb-2">Amount</label>
                    <input
                        type="number"
                        value={paymentDetails.amount}
                        onChange={(e) => setPaymentDetails({...paymentDetails, amount: parseFloat(e.target.value)})}
                        className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
                
                <button
                    onClick={handlePayment}
                    disabled={loading}
                    className="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-6 rounded-lg transition-colors disabled:opacity-50"
                >
                    {loading ? 'Processing...' : 'Pay Now'}
                </button>
            </div>
        </div>
    );

    const renderStep6 = () => (
        <div className="max-w-2xl mx-auto p-6 text-center">
            <div className="bg-green-800 rounded-lg p-8">
                <div className="text-6xl mb-4">✅</div>
                <h2 className="text-3xl font-bold text-white mb-4">Appointment Confirmed!</h2>
                <p className="text-gray-300 mb-6">Your appointment has been successfully booked and payment confirmed.</p>
                
                <div className="bg-gray-800 rounded-lg p-6 mb-6 text-left">
                    <h3 className="text-xl font-semibold text-white mb-4">Appointment Summary</h3>
                    <div className="space-y-2 text-gray-300">
                        <p><span className="font-semibold">Appointment ID:</span> {appointment?.appointmentId}</p>
                        <p><span className="font-semibold">Doctor:</span> Dr. {selectedDoctor?.name}</p>
                        <p><span className="font-semibold">Time:</span> {selectedSlot?.time}</p>
                        <p><span className="font-semibold">Status:</span> <span className="text-green-400">Confirmed</span></p>
                    </div>
                </div>
                
                <button
                    onClick={() => {
                        setCurrentStep(1);
                        setSelectedDoctor(null);
                        setSelectedSlot(null);
                        setAppointment(null);
                        setPatientInfo({
                            name: '',
                            phoneNumber: '',
                            email: '',
                            sex: '',
                            dob: ''
                        });
                    }}
                    className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-6 rounded-lg transition-colors"
                >
                    Book Another Appointment
                </button>
            </div>
        </div>
    );

    return (
        <div className="min-h-screen bg-gray-900 py-8">
            {error && (
                <div className="max-w-4xl mx-auto p-4 mb-6">
                    <div className="bg-red-800 border border-red-600 text-red-200 px-4 py-3 rounded">
                        {error}
                    </div>
                </div>
            )}
            
            <div className="max-w-4xl mx-auto mb-8">
                <div className="flex justify-center">
                    <div className="flex space-x-4">
                        {[1, 2, 3, 4, 5, 6].map((step) => (
                            <div key={step} className="flex items-center">
                                <div className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold ${
                                    currentStep >= step ? 'bg-blue-600 text-white' : 'bg-gray-700 text-gray-400'
                                }`}>
                                    {step}
                                </div>
                                {step < 6 && (
                                    <div className={`w-8 h-0.5 ${currentStep > step ? 'bg-blue-600' : 'bg-gray-700'}`}></div>
                                )}
                            </div>
                        ))}
                    </div>
                </div>
                <div className="flex justify-center mt-4">
                    <div className="flex space-x-16 text-sm text-gray-400">
                        <span>Select Doctor</span>
                        <span>Choose Time</span>
                        <span>Patient Info</span>
                        <span>Confirm</span>
                        <span>Payment</span>
                        <span>Complete</span>
                    </div>
                </div>
            </div>

            {currentStep === 1 && renderStep1()}
            {currentStep === 2 && renderStep2()}
            {currentStep === 3 && renderStep3()}
            {currentStep === 4 && renderStep4()}
            {currentStep === 5 && renderStep5()}
            {currentStep === 6 && renderStep6()}
        </div>
    );
};

export default AppointmentBooking;
