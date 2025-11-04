import React from "react";
import { Outlet } from "react-router-dom";
import Sidebar from "../components/dashboard/Sidebar.jsx";

export default function DoctorLayout() {
  const items = [
    { to: "/doctor/dashboard", label: "Overview", icon: "home" },
    { to: "/doctor/appointments", label: "Appointments", icon: "calendar" },
    { to: "/doctor/prescriptions", label: "Prescriptions", icon: "clipboard" },
    { to: "/doctor/slots", label: "Slots", icon: "calendar" },
    { to: "/doctor/feedbacks", label: "Feedbacks", icon: "feedback" },
  ];

  return (
    <div className="min-h-screen bg-gray-900 text-gray-100 md:flex">
      <Sidebar title="Doctor" items={items} />
      <main className="flex-1 p-4 md:p-6">
        <Outlet />
      </main>
    </div>
  );
}
