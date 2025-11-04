import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Card from "../../components/ui/Card.jsx";
import { apiGet } from "../../api.js";

export default function AdminDashboard() {
  const navigate = useNavigate();
  const [stats, setStats] = useState([
    { label: "Total Doctors", value: 0 },
    { label: "Total Staff", value: 0 },
    { label: "Appointments Today", value: 0 },
  ]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadStats = async () => {
      try {
        setLoading(true);
        setError("");
        const [doctorsRes, staffRes, appointmentsRes] = await Promise.all([
          apiGet("/api/home/doctors"),
          apiGet("/api/admin/staff/all"),
          apiGet("/api/admin/all/appointments"),
        ]);

        const today = new Date().toISOString().split("T")[0];
        const appointments = Array.isArray(appointmentsRes)
          ? appointmentsRes
          : Array.isArray(appointmentsRes?.data)
          ? appointmentsRes.data
          : [];
        const todayAppointments = appointments.filter(
          (apt) => apt.bookingTime === today
        );

        const doctorsCount = Array.isArray(doctorsRes)
          ? doctorsRes.length
          : doctorsRes?.data?.length || 0;
        const staffCount = Array.isArray(staffRes)
          ? staffRes.length
          : staffRes?.data?.length || 0;

        setStats([
          { label: "Total Doctors", value: doctorsCount },
          { label: "Total Staff", value: staffCount },
          { label: "Appointments Today", value: todayAppointments.length },
        ]);
      } catch (error) {
        console.error("Failed to load stats:", error);
        setError("Failed to load stats. Please try again later.");
      } finally {
        setLoading(false);
      }
    };
    loadStats();
  }, []);

  return (
    <div className="space-y-6">
      {error && <div className="text-sm text-red-400">{error}</div>}
      {loading && <div className="text-sm text-gray-400">Loading stats...</div>}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {stats.map((s) => (
          <Card key={s.label} title={s.label}>
            <div className="text-3xl font-bold">{s.value}</div>
          </Card>
        ))}
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Card title="Quick Actions">
          <div className="flex gap-3">
            <button
              onClick={() => navigate("/admin/doctors")}
              className="bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded"
            >
              Manage Doctors
            </button>
            <button
              onClick={() => navigate("/admin/staff")}
              className="bg-green-600 hover:bg-green-700 px-4 py-2 rounded"
            >
              Manage Staff
            </button>
          </div>
        </Card>
        <Card title="Recent Appointments">
          <div className="text-gray-400">
            View all appointments in the appointments section
          </div>
        </Card>
      </div>
    </div>
  );
}
