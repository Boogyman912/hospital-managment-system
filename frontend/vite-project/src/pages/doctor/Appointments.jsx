import React, { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import { apiGet } from "../../api.js";

export default function DoctorAppointments() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const columns = [
    {
      key: "slot",
      header: "Time",
      render: (slot) => (slot?.time ? slot.time : "N/A"),
    },
    {
      key: "patient",
      header: "Patient",
      render: (patient) => (patient?.name ? patient.name : "N/A"),
    },
    { key: "appointmentStatus", header: "Status" },
  ];
  const load = async () => {
    setLoading(true);
    setError("");
    const prev = data;
    try {
      const res = await apiGet("/api/doctor/appointments");
      const list = Array.isArray(res)
        ? res
        : Array.isArray(res?.data)
        ? res.data
        : [];
      setData(list);
    } catch (e) {
      setData(prev);
      setError("Failed to load appointments. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);
  return (
    <Card title="Appointments">
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}
      <Table
        columns={columns}
        data={data}
        renderActions={() => (
          <>
            <button className="px-2 py-1 bg-gray-700 rounded">View</button>
            <button className="px-2 py-1 bg-blue-700 rounded">
              Add Prescription
            </button>
          </>
        )}
      />
    </Card>
  );
}
