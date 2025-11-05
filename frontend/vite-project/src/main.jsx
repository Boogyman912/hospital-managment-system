import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "./index.css";
import App from "./App.jsx";
import DoctorsListPage from "./DoctorsListPage.jsx";
import Login from "./pages/Login.jsx";
import { AuthProvider } from "./auth/AuthContext.jsx";
import { Roles } from "./auth/roles.js";
import RoleRedirect from "./auth/RoleRedirect.jsx";
import ProtectedRoute from "./auth/ProtectedRoute.jsx";
import AdminLayout from "./layouts/AdminLayout.jsx";
import DoctorLayout from "./layouts/DoctorLayout.jsx";
import StaffLayout from "./layouts/StaffLayout.jsx";
import AdminDashboard from "./pages/admin/AdminDashboard.jsx";
import DoctorsManagement from "./pages/admin/DoctorsManagement.jsx";
import StaffManagement from "./pages/admin/StaffManagement.jsx";
import AdminAppointments from "./pages/admin/Appointments.jsx";
import AdminFeedbacks from "./pages/admin/Feedbacks.jsx";
import DoctorDashboard from "./pages/doctor/DoctorDashboard.jsx";
import DoctorAppointments from "./pages/doctor/Appointments.jsx";
import Prescriptions from "./pages/doctor/Prescriptions.jsx";
import Slots from "./pages/doctor/Slots.jsx";
import DoctorFeedbacks from "./pages/doctor/Feedbacks.jsx";
import StaffDashboard from "./pages/staff/StaffDashboard.jsx";
import Rooms from "./pages/staff/Rooms.jsx";
import ManageInpatients from "./pages/staff/ManageInpatients.jsx";
import ManageInventory from "./pages/staff/ManageInventory.jsx";
import ManageLabTests from "./pages/staff/ManageLabTests.jsx";
import ManageBilling from "./pages/staff/ManageBilling.jsx";
import ManagePatients from "./pages/staff/ManagePatients.jsx";
import PaymentPage from "./pages/PaymentPage.jsx";
import PatientAppointments from "./pages/PatientAppointments.jsx";

// Create a component that wraps the router with AuthProvider
// eslint-disable-next-line react-refresh/only-export-components
function AppWithAuth() {
  const router = createBrowserRouter([
    { path: "/", element: <App /> },
    { path: "/doctors", element: <DoctorsListPage /> },
    { path: "/login", element: <Login /> },
    { path: "/payment", element: <PaymentPage /> },
    { path: "/appointments", element: <PatientAppointments /> },
    { path: "/dashboard", element: <RoleRedirect /> },
    {
      element: <ProtectedRoute allow={[Roles.ADMIN]} />,
      children: [
        {
          path: "/admin",
          element: <AdminLayout />,
          children: [
            { path: "/admin/dashboard", element: <AdminDashboard /> },
            { path: "/admin/doctors", element: <DoctorsManagement /> },
            { path: "/admin/staff", element: <StaffManagement /> },
            { path: "/admin/appointments", element: <AdminAppointments /> },
            { path: "/admin/feedbacks", element: <AdminFeedbacks /> },
            { path: "/admin/rooms", element: <Rooms /> },
            { path: "/admin/inpatients", element: <ManageInpatients /> },
            { path: "/admin/inventory", element: <ManageInventory /> },
            { path: "/admin/labtests", element: <ManageLabTests /> },
            { path: "/admin/billing", element: <ManageBilling /> },
            { path: "/admin/patients", element: <ManagePatients /> },
          ],
        },
      ],
    },
    {
      element: <ProtectedRoute allow={[Roles.DOCTOR]} />,
      children: [
        {
          path: "/doctor",
          element: <DoctorLayout />,
          children: [
            { path: "/doctor/dashboard", element: <DoctorDashboard /> },
            { path: "/doctor/appointments", element: <DoctorAppointments /> },
            { path: "/doctor/prescriptions", element: <Prescriptions /> },
            { path: "/doctor/slots", element: <Slots /> },
            { path: "/doctor/feedbacks", element: <DoctorFeedbacks /> },
          ],
        },
      ],
    },
    {
      element: <ProtectedRoute allow={[Roles.STAFF]} />,
      children: [
        {
          path: "/staff",
          element: <StaffLayout />,
          children: [
            { path: "/staff/dashboard", element: <StaffDashboard /> },
            { path: "/staff/rooms", element: <Rooms /> },
            { path: "/staff/inpatients", element: <ManageInpatients /> },
            { path: "/staff/inventory", element: <ManageInventory /> },
            { path: "/staff/labtests", element: <ManageLabTests /> },
            { path: "/staff/billing", element: <ManageBilling /> },
            { path: "/staff/patients", element: <ManagePatients /> },
          ],
        },
      ],
    },
  ]);

  return <RouterProvider router={router} />;
}

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <AuthProvider>
      <AppWithAuth />
    </AuthProvider>
  </StrictMode>
);
