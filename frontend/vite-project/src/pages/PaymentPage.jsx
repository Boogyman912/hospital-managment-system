import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Header from "../components/Header.jsx";
import Footer from "../components/Footer.jsx";
import { apiPost, apiGet } from "../api.js";

export default function PaymentPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const [appointmentId, setAppointmentId] = useState(null);
  const [slotId, setSlotId] = useState(null);
  const [doctorName, setDoctorName] = useState("");
  const [slotTiming, setSlotTiming] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [cancelling, setCancelling] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [paymentStatus, setPaymentStatus] = useState("PENDING");
  const [receipt, setReceipt] = useState(null);

  useEffect(() => {
    // Get appointmentId and other details from location state
    if (location.state?.appointmentId) {
      setAppointmentId(location.state.appointmentId);
      setSlotId(location.state.slotId);
      setDoctorName(location.state.doctorName || "");
      setSlotTiming(location.state.slotTiming || "");
      setPaymentStatus(location.state.paymentStatus || "PENDING");
    } else {
      // If no appointmentId, redirect back to doctors list
      navigate("/doctors");
    }
  }, [location.state, navigate]);

  // Auto-cancel after 10 minutes if payment not completed
  useEffect(() => {
    if (paymentStatus === "PENDING") {
      const timer = setTimeout(() => {
        handleCancelAppointment();
      }, 10 * 60 * 1000); // 10 minutes

      return () => clearTimeout(timer);
    }
  }, [paymentStatus]);

  const handlePayment = async () => {
    if (!appointmentId) return;

    setSubmitting(true);
    setError("");
    setSuccess("");

    try {
      const payload = {
        paymentMethod: "CARD",
        amount: 499.0,
      };

      const response = await apiPost(
        `/api/appointments/${appointmentId}/payment`,
        payload
      );

      if (response.success) {
        setSuccess(response.message || "Payment confirmed successfully");
        setPaymentStatus("PAID");
        setReceipt({
          appointmentId: appointmentId,
          amount: 499.0,
          paymentMethod: "CARD",
          timestamp: new Date().toISOString(),
        });
      } else {
        setError("Failed to confirm payment. Please try again.");
      }
    } catch (err) {
      setError("Something went wrong with payment. Please try again later.");
    } finally {
      setSubmitting(false);
    }
  };

  const handleCancelAppointment = async () => {
    if (!appointmentId || !slotId) return;

    setCancelling(true);
    setError("");
    setSuccess("");

    try {
      // Cancel the appointment
      await apiPost(
        `/api/appointments/cancel/appointment/${appointmentId}`,
        {}
      );

      // Release the slot
      const slotResponse = await apiPost(`/api/slots/${slotId}/release`, {});

      if (slotResponse.success) {
        setSuccess("Appointment cancelled and slot released successfully");
        setTimeout(() => {
          navigate("/doctors");
        }, 2000);
      } else {
        setError("Appointment cancelled but failed to release slot");
      }
    } catch (err) {
      setError("Failed to cancel appointment. Please try again later.");
    } finally {
      setCancelling(false);
    }
  };

  const handleDownloadReceipt = () => {
    if (!receipt) return;

    // Create a simple PDF-like receipt using HTML and print functionality
    const receiptHTML = `
      <!DOCTYPE html>
      <html>
      <head>
        <title>Receipt - ${receipt.appointmentId}</title>
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
          <h2>Payment Receipt</h2>
        </div>
        
        <div class="receipt-details">
          <div><strong>Receipt ID:</strong> ${receipt.appointmentId}</div>
          <div><strong>Date:</strong> ${new Date(
            receipt.timestamp
          ).toLocaleDateString()}</div>
          <div><strong>Time:</strong> ${new Date(
            receipt.timestamp
          ).toLocaleTimeString()}</div>
          <div><strong>Doctor:</strong> ${doctorName}</div>
          <div><strong>Appointment Time:</strong> ${slotTiming}</div>
          <div><strong>Payment Method:</strong> ${receipt.paymentMethod}</div>
        </div>
        
        <div class="total">
          <div>Total Amount: ₹${receipt.amount}</div>
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

  if (!appointmentId) {
    return (
      <div className="bg-gray-900 min-h-screen flex flex-col">
        <Header />
        <div className="flex-1 flex items-center justify-center">
          <p className="text-gray-400">Loading appointment details...</p>
        </div>
        <Footer />
      </div>
    );
  }

  return (
    <div className="bg-gray-900 min-h-screen flex flex-col">
      <Header />
      <div className="flex-1 flex items-center justify-center px-4">
        <div className="bg-gray-800 w-full max-w-md rounded-xl p-8 text-white">
          <div className="text-center mb-8">
            <h1 className="text-3xl font-bold mb-2">Payment Required</h1>
            <p className="text-gray-400">Complete your appointment booking</p>
          </div>

          <div className="space-y-6">
            <div className="bg-gray-700 rounded-lg p-4">
              <h3 className="text-lg font-semibold mb-2">
                Appointment Details
              </h3>
              <p className="text-gray-300">Appointment ID: {appointmentId}</p>
              <p className="text-gray-300">Status: {paymentStatus}</p>
              {doctorName && (
                <p className="text-gray-300">Doctor: {doctorName}</p>
              )}
              {slotTiming && (
                <p className="text-gray-300">Slot: {slotTiming}</p>
              )}
            </div>

            <div className="bg-gray-700 rounded-lg p-4">
              <h3 className="text-lg font-semibold mb-2">Payment Summary</h3>
              <div className="flex justify-between items-center">
                <span className="text-gray-300">Consultation Fee</span>
                <span className="text-2xl font-bold text-green-400">
                  ₹499.00
                </span>
              </div>
            </div>

            {success && (
              <div className="bg-green-900/20 border border-green-500 rounded-lg p-4">
                <p className="text-green-400 text-center">{success}</p>
              </div>
            )}

            {error && (
              <div className="bg-red-900/20 border border-red-500 rounded-lg p-4">
                <p className="text-red-400 text-center">{error}</p>
              </div>
            )}

            <div className="space-y-4">
              {paymentStatus === "PAID" && receipt ? (
                <button
                  onClick={handleDownloadReceipt}
                  className="w-full bg-green-600 hover:bg-green-700 text-white font-semibold py-4 px-6 rounded-lg transition-colors duration-300"
                >
                  Download Receipt
                </button>
              ) : (
                <button
                  onClick={handlePayment}
                  disabled={submitting || paymentStatus === "PAID"}
                  className="w-full bg-blue-600 hover:bg-blue-700 disabled:opacity-60 disabled:cursor-not-allowed text-white font-semibold py-4 px-6 rounded-lg transition-colors duration-300"
                >
                  {submitting
                    ? "Processing Payment..."
                    : paymentStatus === "PAID"
                    ? "Payment Completed"
                    : "Pay ₹499.00"}
                </button>
              )}

              {paymentStatus === "PENDING" && (
                <button
                  onClick={handleCancelAppointment}
                  disabled={cancelling}
                  className="w-full bg-red-600 hover:bg-red-700 disabled:opacity-60 disabled:cursor-not-allowed text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-300"
                >
                  {cancelling ? "Cancelling..." : "Cancel Appointment"}
                </button>
              )}

              <button
                onClick={() => navigate("/doctors")}
                className="w-full bg-gray-700 hover:bg-gray-600 text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-300"
              >
                Back to Doctors
              </button>
            </div>

            {paymentStatus === "PAID" && (
              <div className="text-center">
                <p className="text-green-400 text-sm">
                  Redirecting to doctors list in 3 seconds...
                </p>
              </div>
            )}
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
}
