import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header.jsx";
import Footer from "../components/Footer.jsx";
import { apiGet } from "../api.js";

export default function PatientAppointments() {
  const navigate = useNavigate();
  const [phoneNumber, setPhoneNumber] = useState("");
  const [loading, setLoading] = useState(false);
  const [appointments, setAppointments] = useState([]);
  const [error, setError] = useState("");
  const [hasSearched, setHasSearched] = useState(false);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!phoneNumber.trim()) return;

    setLoading(true);
    setError("");
    setAppointments([]);
    setHasSearched(true);

    try {
      const response = await apiGet(`/api/home/appointment/${phoneNumber}`);

      if (response.success === false) {
        setError(response.message || "No appointments found");
        setAppointments([]);
      } else if (
        response.success === true &&
        Array.isArray(response.appointments)
      ) {
        setAppointments(response.appointments);
        setError("");
      } else {
        setError("Invalid response format");
        setAppointments([]);
      }
    } catch (err) {
      setError(err.message || "Failed to fetch appointments");
      setAppointments([]);
    } finally {
      setLoading(false);
    }
  };

  const handleDownloadReceipt = (receipt) => {
    if (!receipt) return;

    const receiptData = {
      appointmentId: receipt.appointmentId,
      doctorName: receipt.doctor?.name,
      doctorSpecialization: receipt.doctor?.specialization,
      patientName: receipt.patient?.name,
      patientPhone: receipt.patient?.phoneNumber,
      slotTiming: `${receipt.slot?.startTime} - ${receipt.slot?.endTime}`,
      amount: receipt.receipt?.amount,
      paymentMethod: receipt.receipt?.paymentMethod,
      timestamp: receipt.receipt?.timestamp,
    };

    const dataStr = JSON.stringify(receiptData, null, 2);
    const dataUri =
      "data:application/json;charset=utf-8," + encodeURIComponent(dataStr);

    const exportFileDefaultName = `receipt-${receipt.appointmentId}.json`;

    const linkElement = document.createElement("a");
    linkElement.setAttribute("href", dataUri);
    linkElement.setAttribute("download", exportFileDefaultName);
    linkElement.click();
  };

  const formatDateTime = (dateTimeString) => {
    if (!dateTimeString) return "N/A";
    try {
      const date = new Date(dateTimeString);
      return date.toLocaleString();
    } catch {
      return dateTimeString;
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case "PAID":
        return "text-green-400";
      case "PENDING":
        return "text-yellow-400";
      case "CANCELLED":
        return "text-red-400";
      default:
        return "text-gray-400";
    }
  };

  return (
    <div className="bg-gray-900 min-h-screen flex flex-col">
      <Header />
      <div className="flex-1 px-4 sm:px-8 py-8">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-4xl sm:text-5xl font-bold text-white text-center mb-10">
            My Appointments
          </h1>

          {/* Search Form */}
          <div className="bg-gray-800 rounded-xl p-6 mb-8">
            <form onSubmit={handleSearch} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-300 mb-2">
                  Phone Number
                </label>
                <input
                  type="tel"
                  value={phoneNumber}
                  onChange={(e) => setPhoneNumber(e.target.value)}
                  placeholder="Enter your phone number"
                  required
                  className="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <button
                type="submit"
                disabled={loading}
                className="w-full bg-blue-600 hover:bg-blue-700 disabled:opacity-60 text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-300"
              >
                {loading ? "Searching..." : "Search Appointments"}
              </button>
            </form>
          </div>

          {/* Error Message */}
          {error && (
            <div className="bg-red-900/20 border border-red-500 rounded-lg p-4 mb-6">
              <p className="text-red-400 text-center">{error}</p>
            </div>
          )}

          {/* Appointments List */}
          {hasSearched && !loading && appointments.length === 0 && !error && (
            <div className="bg-gray-800 rounded-xl p-6 text-center">
              <p className="text-gray-400">
                No appointments found for this phone number.
              </p>
            </div>
          )}

          {appointments.length > 0 && (
            <div className="space-y-6">
              <h2 className="text-2xl font-bold text-white mb-4">
                Your Appointments ({appointments.length})
              </h2>

              {appointments.map((appointment) => (
                <div
                  key={appointment.appointmentId}
                  className="bg-gray-800 rounded-xl p-6"
                >
                  <div className="grid md:grid-cols-2 gap-6">
                    {/* Appointment Info */}
                    <div className="space-y-4">
                      <div>
                        <h3 className="text-lg font-semibold text-white mb-2">
                          Appointment #{appointment.appointmentId}
                        </h3>
                        <div className="space-y-2 text-sm">
                          <p className="text-gray-300">
                            <span className="font-medium">Status:</span>{" "}
                            <span
                              className={getStatusColor(
                                appointment.paymentStatus
                              )}
                            >
                              {appointment.paymentStatus}
                            </span>
                          </p>
                          <p className="text-gray-300">
                            <span className="font-medium">Booking Time:</span>{" "}
                            {formatDateTime(appointment.bookingTime)}
                          </p>
                          <p className="text-gray-300">
                            <span className="font-medium">Type:</span>{" "}
                            {appointment.isOnline ? "Online" : "In-person"}
                          </p>
                          {appointment.meetLink && (
                            <p className="text-gray-300">
                              <span className="font-medium">Meet Link:</span>{" "}
                              <a
                                href={appointment.meetLink}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="text-blue-400 hover:text-blue-300"
                              >
                                Join Meeting
                              </a>
                            </p>
                          )}
                        </div>
                      </div>
                    </div>

                    {/* Doctor Info */}
                    <div className="space-y-4">
                      <div>
                        <h4 className="text-md font-semibold text-white mb-2">
                          Doctor Details
                        </h4>
                        <div className="space-y-2 text-sm">
                          <p className="text-gray-300">
                            <span className="font-medium">Name:</span>{" "}
                            {appointment.doctor?.name}
                          </p>
                          <p className="text-gray-300">
                            <span className="font-medium">Specialization:</span>{" "}
                            {appointment.doctor?.specialization}
                          </p>
                          <p className="text-gray-300">
                            <span className="font-medium">Contact:</span>{" "}
                            {appointment.doctor?.phone_number}
                          </p>
                        </div>
                      </div>

                      <div>
                        <h4 className="text-md font-semibold text-white mb-2">
                          Slot Details
                        </h4>
                        <div className="space-y-2 text-sm">
                          <p className="text-gray-300">
                            <span className="font-medium">Date:</span>{" "}
                            {appointment.slot?.date || "N/A"}
                          </p>
                          <p className="text-gray-300">
                            <span className="font-medium">Time:</span>{" "}
                            {appointment.slot?.time || "N/A"}
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>

                  {/* Receipt Download */}
                  {appointment.receipt && (
                    <div className="mt-6 pt-4 border-t border-gray-700">
                      <button
                        onClick={() => handleDownloadReceipt(appointment)}
                        className="bg-green-600 hover:bg-green-700 text-white font-semibold py-2 px-4 rounded-lg transition-colors duration-300"
                      >
                        Download Receipt
                      </button>
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}

          {/* Back Button */}
          <div className="mt-8 text-center">
            <button
              onClick={() => navigate("/doctors")}
              className="bg-gray-700 hover:bg-gray-600 text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-300"
            >
              Back to Doctors
            </button>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
}
