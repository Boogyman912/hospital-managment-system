import { Outlet } from "react-router-dom";
import Sidebar from "../components/dashboard/Sidebar.jsx";

export default function StaffLayout() {
  const items = [
    { to: "/staff/dashboard", label: "Overview", icon: "home" },
    { to: "/staff/rooms", label: "Manage Rooms", icon: "clipboard" },
    { to: "/staff/inpatients", label: "Inpatients", icon: "users" },
    { to: "/staff/inventory", label: "Inventory", icon: "package" },
    { to: "/staff/labtests", label: "Lab Tests", icon: "microscope" },
    { to: "/staff/billing", label: "Billing", icon: "dollar-sign" },
    { to: "/staff/patients", label: "Patients", icon: "user" },
  ];

  return (
    <div className="min-h-screen bg-gray-900 text-gray-100 md:flex">
      <Sidebar title="Staff" items={items} />
      <main className="flex-1 p-4 md:p-6">
        <Outlet />
      </main>
    </div>
  );
}
