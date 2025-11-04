import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext.jsx";
import { Roles } from "./roles.js";

export default function RoleRedirect() {
  const { user, isReady } = useAuth();
  if (!isReady) return null;
  if (!user) return <Navigate to="/login" replace />;
  if (user.role === Roles.ADMIN)
    return <Navigate to="/admin/dashboard" replace />;
  if (user.role === Roles.DOCTOR)
    return <Navigate to="/doctor/dashboard" replace />;
  if (user.role === Roles.STAFF)
    return <Navigate to="/staff/dashboard" replace />;
  return <Navigate to="/login" replace />;
}
