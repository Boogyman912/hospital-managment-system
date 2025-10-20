import React, { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import { apiGet, apiPost } from "../../api.js";

export default function Appointments() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const columns = [
    { key: "date", header: "Date" },
    { key: "doctor", header: "Doctor" },
    { key: "patient", header: "Patient" },
    { key: "status", header: "Status" },
  ];
  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiGet("/api/admin/all/appointments");
      setData(res || []);
    } catch (e) {
      setError(e?.message || "Failed to load appointments");
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
        renderActions={(row) => (
          <button
            onClick={async () => {
              try {
                await apiPost(
                  `/api/admin/cancel/appointment/${row.appointmentId}`
                );
                load();
              } catch (e) {
                alert(e?.message || "Cancel failed");
              }
            }}
            className="px-2 py-1 bg-red-700 rounded"
          >
            Cancel
          </button>
        )}
      />
    </Card>
  );
}
