import { useEffect, useState, useCallback } from "react";
import Card from "../components/ui/Card.jsx";
import Table from "../components/ui/Table.jsx";
import Header from "../components/Header.jsx";
import Footer from "../components/Footer.jsx";
import { apiGet } from "../api.js";

const handleDownloadReceipt = (appointment) => {
  if (!appointment) return;

  // Create receipt data from appointment
  const receiptData = {
    appointmentId: appointment.appointmentId,
    doctorName: appointment.doctor?.name || "N/A",
    doctorSpecialization: appointment.doctor?.specialization || "N/A",
    patientName: appointment.patient?.name || "N/A",
    patientPhone: appointment.patient?.phoneNumber || "N/A",
    slotDate: appointment.slot?.date || "N/A",
    slotTime: appointment.slot?.time || "N/A",
    appointmentType: appointment.isOnline ? "Online" : "In-person",
    amount: appointment.receipt?.amount || "N/A",
    paymentMethod: appointment.receipt?.paymentMethod || "N/A",
    timestamp: appointment.receipt?.timestamp || new Date().toISOString(),
  };

  // Create a professional HTML receipt using HTML and print functionality
  const receiptHTML = `
    <!DOCTYPE html>
    <html>
    <head>
      <title>Receipt - ${receiptData.appointmentId}</title>
      <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 10px; margin-bottom: 20px; }
        .receipt-details { margin: 20px 0; }
        .receipt-details div { margin: 10px 0; }
        .total { font-weight: bold; font-size: 18px; border-top: 1px solid #333; padding-top: 10px; margin-top: 20px; }
        .footer { text-align: center; margin-top: 30px; font-size: 12px; color: #666; }
      </style>
    </head>
    <body>
      <div class="header">
        <h1>Hospital Management System</h1>
        <h2>Appointment Receipt</h2>
      </div>
      
      <div class="receipt-details">
        <div><strong>Appointment ID:</strong> ${receiptData.appointmentId}</div>
        <div><strong>Date:</strong> ${new Date(
          receiptData.timestamp
        ).toLocaleDateString()}</div>
        <div><strong>Time:</strong> ${new Date(
          receiptData.timestamp
        ).toLocaleTimeString()}</div>
        <div><strong>Patient Name:</strong> ${receiptData.patientName}</div>
        <div><strong>Patient Phone:</strong> ${receiptData.patientPhone}</div>
        <div><strong>Doctor:</strong> ${receiptData.doctorName}</div>
        <div><strong>Specialization:</strong> ${
          receiptData.doctorSpecialization
        }</div>
        <div><strong>Appointment Date:</strong> ${receiptData.slotDate}</div>
        <div><strong>Appointment Time:</strong> ${receiptData.slotTime}</div>
        <div><strong>Appointment Type:</strong> ${
          receiptData.appointmentType
        }</div>
        <div><strong>Payment Method:</strong> ${receiptData.paymentMethod}</div>
      </div>
      
      <div class="total">
        <div>Total Amount: ₹${receiptData.amount}</div>
      </div>
      
      <div class="footer">
        <p>Thank you for choosing our hospital!</p>
        <p>For any queries, please contact us.</p>
      </div>
    </body>
    </html>
  `;

  // Create a new window with the receipt content
  const printWindow = window.open("", "_blank");
  printWindow.document.write(receiptHTML);
  printWindow.document.close();

  // Wait for content to load, then trigger print dialog
  printWindow.onload = function () {
    printWindow.print();
  };
};

export default function PatientAppointments() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const [phoneNumber, setPhoneNumber] = useState("");
  const [hasSearched, setHasSearched] = useState(false);

  // Helper function to sanitize phone number input
  const sanitizePhoneNumber = (value) => value.replace(/\D/g, "");

  const columns = [
    {
      key: "slot",
      header: "Date",
      render: (slot) => (slot?.date ? slot.date : "-"),
    },
    {
      key: "slot",
      header: "Time",
      render: (slot) => (slot?.time ? slot.time : "-"),
    },
    {
      key: "doctor",
      header: "Doctor",
      render: (doctor) =>
        doctor ? `${doctor.name} (${doctor.specialization})` : "N/A",
    },
    { key: "appointmentStatus", header: "Status" },
    { key: "paymentStatus", header: "Payment" },
  ];

  const load = useCallback(async () => {
    if (!phoneNumber) {
      setError("Please enter a phone number.");
      return;
    }

    // Validate phone number format (10-15 digits)
    if (!/^\d{10,15}$/.test(phoneNumber)) {
      setError("Please enter a valid phone number (10-15 digits).");
      return;
    }

    setLoading(true);
    setError("");
    setHasSearched(true);
    try {
      const res = await apiGet(`/api/home/appointment/${phoneNumber}`);
      const list = Array.isArray(res)
        ? res
        : Array.isArray(res?.appointments)
        ? res.appointments
        : [];
      setData(list);

      if (list.length === 0) {
        setError("No appointments found for this phone number.");
      }
    } catch (err) {
      // Show friendly message on error
      setError(
        err?.message || "Failed to load appointments. Please try again later."
      );
      setData([]);
    } finally {
      setLoading(false);
    }
  }, [phoneNumber]);

  useEffect(() => {
    // Try to get phone number from localStorage if user is logged in
    try {
      const user = JSON.parse(localStorage.getItem("user") || "{}");
      if (user.phoneNumber) {
        setPhoneNumber(user.phoneNumber);
      }
    } catch {
      // Ignore parse errors
    }
  }, []);

  return (
    <div className="min-h-screen bg-gray-900 flex flex-col">
      <Header />
      <div className="flex-1 flex items-center justify-center p-4">
        <div className="w-full max-w-4xl">
          <div className="bg-gray-800 border border-gray-700 rounded-lg p-6 mb-4">
            <h1 className="text-2xl font-semibold mb-4 text-white">
              My Appointments
            </h1>
            <p className="text-gray-400 mb-4">
              Enter your phone number to view your appointments
            </p>

            <div className="flex gap-2">
              <input
                type="tel"
                className="flex-1 px-3 py-2 bg-gray-900 border border-gray-700 rounded text-white"
                value={phoneNumber}
                onChange={(e) =>
                  setPhoneNumber(sanitizePhoneNumber(e.target.value))
                }
                placeholder="Enter phone number (10-15 digits)"
                maxLength="15"
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    load();
                  }
                }}
              />
              <button
                onClick={load}
                disabled={loading || !phoneNumber}
                className="px-6 py-2 bg-blue-600 hover:bg-blue-700 disabled:opacity-60 disabled:cursor-not-allowed rounded font-medium text-white"
              >
                {loading ? "Loading..." : "Search"}
              </button>
            </div>

            {error && <div className="mt-3 text-red-400 text-sm">{error}</div>}
          </div>

          {hasSearched && !loading && (
            <Card title={`Appointments for ${phoneNumber}`}>
              <Table
                columns={columns}
                data={data}
                renderActions={(row) => (
                  <div className="flex gap-2">
                    {row?.receipt && (
                      <button
                        onClick={() => handleDownloadReceipt(row)}
                        className="px-2 py-1 bg-blue-600 hover:bg-blue-700 rounded"
                      >
                        Download Receipt
                      </button>
                    )}
                  </div>
                )}
              />
            </Card>
          )}
        </div>
      </div>
      <Footer />
    </div>
  );
}
