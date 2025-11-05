const handleDownloadReceipt = (appointment) => {
  if (!appointment) return;

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

  // Create a professional HTML receipt
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
        <div><strong>Date:</strong> ${new Date(receiptData.timestamp).toLocaleDateString()}</div>
        <div><strong>Time:</strong> ${new Date(receiptData.timestamp).toLocaleTimeString()}</div>
        <div><strong>Patient Name:</strong> ${receiptData.patientName}</div>
        <div><strong>Patient Phone:</strong> ${receiptData.patientPhone}</div>
        <div><strong>Doctor:</strong> ${receiptData.doctorName}</div>
        <div><strong>Specialization:</strong> ${receiptData.doctorSpecialization}</div>
        <div><strong>Appointment Date:</strong> ${receiptData.slotDate}</div>
        <div><strong>Appointment Time:</strong> ${receiptData.slotTime}</div>
        <div><strong>Appointment Type:</strong> ${receiptData.appointmentType}</div>
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