import React, { useEffect, useState, useCallback } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import { apiGet, apiPost } from "../../api.js";

export default function Appointments() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
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
    {
      key: "patient",
      header: "Patient",
      render: (patient) =>
        patient ? `${patient.name} (${patient.phoneNumber})` : "N/A",
    },
    { key: "appointmentStatus", header: "Status" },
    { key: "paymentStatus", header: "Payment" },
  ];
  const load = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiGet("/api/admin/all/appointments");
      const list = Array.isArray(res)
        ? res
        : Array.isArray(res?.data)
        ? res.data
        : [];
      setData(list);
    } catch {
      // Show friendly message on error
      setError("Failed to load appointments. Please try again later.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);
  return (
    <Card title="Appointments">
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}
      <Table
        columns={columns}
        data={data}
        renderActions={(row) => (
          <div className="flex gap-2">
            <button
              onClick={async () => {
                try {
                  const response = await apiPost(
                    `/api/admin/cancel/appointment/${row.appointmentId}`,
                    null
                  );
                  if (response?.success) {
                    alert(
                      response.message || "Appointment cancelled successfully"
                    );
                    load();
                  } else {
                    alert("Failed to cancel appointment");
                  }
                } catch (e) {
                  alert(e?.message || "Cancel failed");
                }
              }}
              disabled={
                String(row.appointmentStatus).toUpperCase() === "CANCELLED"
              }
              className="px-2 py-1 bg-red-700 disabled:opacity-50 rounded"
            >
              Cancel
            </button>
            {row?.receipt?.pdfUrl && (
              <a
                href={row.receipt.pdfUrl}
                target="_blank"
                rel="noreferrer"
                className="px-2 py-1 bg-gray-700 rounded"
              >
                Receipt
              </a>
            )}
          </div>
        )}
      />
    </Card>
  );
}
