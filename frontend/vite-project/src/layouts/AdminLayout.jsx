import { Outlet } from "react-router-dom";
import Sidebar from "../components/dashboard/Sidebar.jsx";

export default function AdminLayout() {
  const items = [
    { to: "/admin/dashboard", label: "Overview", icon: "home" },
    { to: "/admin/doctors", label: "Doctors", icon: "stethoscope" },
    { to: "/admin/staff", label: "Staff", icon: "users" },
    { to: "/admin/appointments", label: "Appointments", icon: "calendar" },
    { to: "/admin/feedbacks", label: "Feedbacks", icon: "feedback" },
    { to: "/admin/rooms", label: "Manage Rooms", icon: "clipboard" },
    { to: "/admin/inpatients", label: "Inpatients", icon: "users" },
    { to: "/admin/inventory", label: "Inventory", icon: "package" },
    { to: "/admin/labtests", label: "Lab Tests", icon: "microscope" },
    { to: "/admin/billing", label: "Billing", icon: "dollar-sign" },
    { to: "/admin/patients", label: "Patients", icon: "user" },
  ];

  return (
    <div className="min-h-screen bg-gray-900 text-gray-100 md:flex">
      <Sidebar title="Admin" items={items} />
      <main className="flex-1 p-4 md:p-6">
        <Outlet />
      </main>
    </div>
  );
}
