import React, { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import { apiGet } from "../../api.js";

export default function Rooms() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const columns = [
    { key: "slotId", header: "Room/Slot" },
    { key: "doctorName", header: "Doctor" },
    { key: "time", header: "Time" },
    { key: "status", header: "Status" },
  ];
  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiGet("/api/staff/rooms/all");
      setData(res || []);
    } catch (e) {
      setError(e?.message || "Failed to load rooms");
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    load();
  }, []);
  return (
    <Card title="Rooms">
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}
      <Table columns={columns} data={data} />
    </Card>
  );
}
