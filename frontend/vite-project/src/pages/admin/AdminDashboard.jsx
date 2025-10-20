import React from "react";
import Card from "../../components/ui/Card.jsx";

export default function AdminDashboard() {
  const stats = [
    { label: "Total Doctors", value: 24 },
    { label: "Total Staff", value: 56 },
    { label: "Appointments Today", value: 34 },
  ];

  return (
    <div className="space-y-6">
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
            <button className="bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded">
              Add Doctor
            </button>
            <button className="bg-green-600 hover:bg-green-700 px-4 py-2 rounded">
              Add Staff
            </button>
          </div>
        </Card>
        <Card title="Daily Appointments (Mock)">
          <div className="text-gray-400">Simple chart placeholder</div>
        </Card>
      </div>
    </div>
  );
}
