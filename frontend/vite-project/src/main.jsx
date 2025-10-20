import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "./index.css";
import App from "./App.jsx";
import DoctorsListPage from "./DoctorsListPage.jsx";
import RegisterDoctor from "./RegisterDoctor.jsx";
import Login from "./pages/Login.jsx";
import { AuthProvider } from "./auth/AuthContext.jsx";
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
import { Roles } from "./auth/AuthContext.jsx";

const router = createBrowserRouter([
  { path: "/", element: <App /> },
  { path: "/doctors", element: <DoctorsListPage /> },
  { path: "/login", element: <Login /> },
  { path: "/register-doctor", element: <RegisterDoctor /> },
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
        ],
      },
    ],
  },
]);

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  </StrictMode>
);
