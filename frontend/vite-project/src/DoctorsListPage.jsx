import React from "react";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Header from "./components/Header.jsx";
import Footer from "./components/Footer.jsx";
import { apiGet, apiPost } from "./api.js";

// Fallback mock data when backend returns no doctors or errors
const mockDoctors = [
  { doctor_id: 101, name: "Dr. Mock One", specialization: "General Medicine" },
  { doctor_id: 102, name: "Dr. Mock Two", specialization: "Pediatrics" },
  { doctor_id: 103, name: "Dr. Mock Three", specialization: "Dermatology" },
];

// Doctors list is fetched from backend only

// --- SVG Icon for Default Avatar ---
const DoctorIcon = () => (
  <svg
    className="w-16 h-16 text-gray-400"
    fill="currentColor"
    viewBox="0 0 20 20"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      fillRule="evenodd"
      d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"
      clipRule="evenodd"
    ></path>
  </svg>
);

// --- Doctor Card Component ---
const DoctorCard = ({ doctor, onBook }) => {
  return (
    <div className="bg-gray-800 rounded-xl p-6 flex flex-col sm:flex-row items-center space-y-4 sm:space-y-0 sm:space-x-6 transform hover:scale-105 transition-transform duration-300 shadow-lg">
      {/* Always show default icon (backend doesn't provide profile pics) */}
      <div className="flex-shrink-0 w-24 h-24 rounded-full bg-gray-700 flex items-center justify-center overflow-hidden">
        <DoctorIcon />
      </div>
      {/* Doctor Info */}
      <div className="flex-1 text-center sm:text-left">
        <h3 className="text-2xl font-bold text-white">{doctor.name}</h3>
        <p className="text-blue-300 text-md">{doctor.specialization}</p>
      </div>
      {/* Book Appointment Button */}
      <div className="flex-shrink-0">
        <button
          onClick={() => onBook(doctor)}
          className="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
        >
          Book Appointment
        </button>
      </div>
    </div>
  );
};

// --- Main Doctors List Page Component ---
export default function DoctorsListPage() {
  const navigate = useNavigate();
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [doctors, setDoctors] = useState([]);
  const [loadingDoctors, setLoadingDoctors] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [loadingSlots, setLoadingSlots] = useState(false);
  const [slots, setSlots] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [form, setForm] = useState({
    name: "",
    phone_number: "",
    email: "",
    sex: "",
    emergency_contact_id: "",
  });

  React.useEffect(() => {
    let cancelled = false;
    async function load() {
      try {
        setLoadingDoctors(true);
        const data = await apiGet("/api/home/doctors");
        console.log("[load doctors] raw response:", data);
        if (cancelled) return;
        const list = Array.isArray(data)
          ? data
          : Array.isArray(data?.doctors)
          ? data.doctors
          : Array.isArray(data?.data)
          ? data.data
          : [];
        if (list.length === 0) {
          console.warn("[load doctors] No doctors received");
          setDoctors([]);
        } else {
          console.log(`[load doctors] Loaded ${list.length} doctors`);
          setDoctors(list);
        }
      } catch (e) {
        console.error("[load doctors] error:", e);
        if (!cancelled) {
          // rollback: keep whatever was already in state
          setError("Failed to load doctors. Please try again later.");
        }
      } finally {
        if (!cancelled) setLoadingDoctors(false);
      }
    }
    load();
    return () => {
      cancelled = true;
    };
  }, []);

  async function handleOpenForm(doctor) {
    setSelectedDoctor(doctor);
    setError("");
    setSuccess("");
    setSelectedSlot(null);
    setSlots([]);
    const doctorId = doctor.doctor_id || doctor.id || doctor._id;
    if (!doctorId) return;
    setLoadingSlots(true);
    try {
      const data = await apiGet(`/api/home/doctor-slots/${doctorId}`);
      const list = Array.isArray(data)
        ? data
        : Array.isArray(data?.slots)
        ? data.slots
        : [];
      setSlots(list);
    } catch (e) {
      setError(e.message || "Failed to load slots");
    } finally {
      setLoadingSlots(false);
    }
  }

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    if (!selectedDoctor) return;
    if (!selectedSlot) {
      setError("Please select a slot first");
      return;
    }
    setSubmitting(true);
    setError("");
    setSuccess("");
    try {
      // patient_id is PK auto-generated by backend
      const payload = {
        name: form.name,
        phoneNumber: form.phone_number,
        email: form.email || null,
        sex: form.sex,
        dob: form.dob || null,
      };
      const patientResp = await apiPost("/api/home/addpatient", payload);
      const patientId = patientResp?.patient?.patientId;
      if (!patientId) {
        throw new Error(
          patientResp?.message || "Failed to create or fetch patient"
        );
      }

      const doctorId = String(
        selectedDoctor.doctor_id ||
          selectedDoctor.id ||
          selectedDoctor._id ||
          ""
      );
      const bookingPayload = {
        slotId: selectedSlot.slotId,
        doctor_id: doctorId,
        patientId: String(patientId),
        isOnline: Boolean(selectedSlot.isOnline),
        meetLink: null,
      };
      const bookingResp = await apiPost(
        "/api/appointments/book",
        bookingPayload
      );
      if (bookingResp?.success) {
        // Redirect to payment page with appointmentId and other details
        navigate("/payment", {
          state: {
            appointmentId: bookingResp.appointmentId,
            paymentStatus: bookingResp.paymentStatus,
            slotId: selectedSlot.slotId,
            doctorName: selectedDoctor.name,
            slotTiming: `${selectedSlot.date} at ${selectedSlot.time}`,
          },
        });
      } else {
        setError(bookingResp?.message || "Failed to book appointment");
      }
    } catch (err) {
      setError(err.message || "Something went wrong");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="bg-gray-900 min-h-screen flex flex-col">
      <Header />
      <div className="px-4 sm:px-8 py-8">
        <h1 className="text-4xl sm:text-5xl font-bold text-white text-center mb-10">
          Find a Doctor
        </h1>
        {success && (
          <p className="text-green-400 text-center mb-4">{success}</p>
        )}
        {error && <p className="text-red-400 text-center mb-4">{error}</p>}
        <div className="space-y-6">
          {loadingDoctors && (
            <p className="text-gray-400 text-center">Loading doctors...</p>
          )}
          {!loadingDoctors && (!doctors || doctors.length === 0) && (
            <p className="text-gray-500 text-center">No doctors found.</p>
          )}
          {(doctors || []).map((doctor) => (
            <DoctorCard
              key={doctor.doctor_id || doctor.id || doctor._id}
              doctor={doctor}
              onBook={handleOpenForm}
            />
          ))}
        </div>
      </div>

      {selectedDoctor && (
        <div className="fixed inset-0 bg-black/70 flex items-center justify-center px-4">
          <div className="bg-gray-800 w-full max-w-lg rounded-xl p-6 text-white">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-xl font-semibold">
                Book with {selectedDoctor.name}
              </h2>
              <button
                className="text-gray-400 hover:text-white"
                onClick={() => setSelectedDoctor(null)}
              >
                ✕
              </button>
            </div>
            {/* Slots Section */}
            <div className="mb-6">
              <h3 className="text-lg font-medium mb-2">Available Slots</h3>
              {loadingSlots && (
                <p className="text-gray-400">Loading slots...</p>
              )}
              {!loadingSlots && slots && slots.length === 0 && (
                <p className="text-gray-400">No available slots</p>
              )}
              {!loadingSlots && slots && slots.length > 0 && (
                <div className="max-h-48 overflow-auto grid grid-cols-1 sm:grid-cols-2 gap-2">
                  {slots.map((slot) => {
                    const isSelected = selectedSlot?.slotId === slot.slotId;
                    return (
                      <button
                        key={`${slot.slotId}-${slot.date}-${slot.time}`}
                        type="button"
                        onClick={() => setSelectedSlot(slot)}
                        className={`text-left px-3 py-2 rounded-lg border ${
                          isSelected
                            ? "border-blue-500 bg-blue-600/20"
                            : "border-gray-700 bg-gray-900"
                        }`}
                      >
                        <div className="text-sm text-gray-300">{slot.date}</div>
                        <div className="text-white font-medium">
                          {slot.time}
                        </div>
                        <div className="text-xs text-gray-400">
                          {slot.isOnline ? "Online" : "In-person"}
                        </div>
                      </button>
                    );
                  })}
                </div>
              )}
              {selectedSlot && (
                <p className="mt-2 text-sm text-green-400">
                  Selected: {selectedSlot.date} at {selectedSlot.time}
                </p>
              )}
            </div>
            <form onSubmit={handleSubmit} className="space-y-4">
              {!selectedSlot && (
                <div className="text-yellow-400 text-sm">
                  Please select a slot above to proceed.
                </div>
              )}
              <div>
                <label className="block text-sm mb-1">Name</label>
                <input
                  name="name"
                  value={form.name}
                  onChange={handleChange}
                  required
                  className="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-3"
                />
              </div>
              <div>
                <label className="block text-sm mb-1">Phone Number</label>
                <input
                  name="phone_number"
                  value={form.phone_number}
                  onChange={handleChange}
                  required
                  className="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-3"
                />
              </div>
              <div>
                <label className="block text-sm mb-1">Date of Birth</label>
                <input
                  type="date"
                  name="dob"
                  value={form.dob || ""}
                  onChange={handleChange}
                  className="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-3"
                />
              </div>
              <div>
                <label className="block text-sm mb-1">Email (optional)</label>
                <input
                  type="email"
                  name="email"
                  value={form.email}
                  onChange={handleChange}
                  className="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-3"
                />
              </div>
              <div>
                <label className="block text-sm mb-1">Sex</label>
                <select
                  name="sex"
                  value={form.sex}
                  onChange={handleChange}
                  required
                  className="w-full bg-gray-900 border border-gray-700 rounded-lg px-4 py-3"
                >
                  <option value="" disabled>
                    Select
                  </option>
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                  <option value="Other">Other</option>
                </select>
              </div>
              {/* emergency_contact_id removed from payload per latest schema */}
              <div className="flex justify-end gap-3 pt-2">
                <button
                  type="button"
                  onClick={() => setSelectedDoctor(null)}
                  className="bg-gray-700 hover:bg-gray-600 px-5 py-2 rounded-lg"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={submitting || !selectedSlot}
                  className="bg-blue-600 hover:bg-blue-700 disabled:opacity-60 px-5 py-2 rounded-lg"
                >
                  {submitting ? "Booking..." : "Confirm Booking"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
      <Footer />
    </div>
  );
}
