import React, { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import { apiGet } from "../../api.js";

export default function DoctorDashboard() {
  const [todayCount, setTodayCount] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      setError("");
      try {
        const res = await apiGet("/api/doctor/appointments");
        const list = Array.isArray(res)
          ? res
          : Array.isArray(res?.data)
          ? res.data
          : [];
        const today = new Date().toLocaleDateString("en-CA"); // YYYY-MM-DD
        const count = list.filter(
          (apt) =>
            String(apt?.appointmentStatus).toUpperCase() === "BOOKED" &&
            apt?.slot?.date === today
        ).length;
        setTodayCount(count);
      } catch (e) {
        setError("Failed to load appointments.");
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <Card title="Today's Appointments">
        {loading && <div className="text-sm text-gray-400">Loading...</div>}
        {error && <div className="text-sm text-red-400">{error}</div>}
        <div className="text-3xl font-bold">{todayCount}</div>
      </Card>
      <Card title="New Feedbacks">
        <div className="text-3xl font-bold">2</div>
      </Card>
    </div>
  );
}
