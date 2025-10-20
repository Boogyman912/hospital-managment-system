import React from "react";
import { Navigate } from "react-router-dom";
import { Roles, useAuth } from "./AuthContext.jsx";

export default function RoleRedirect() {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (user.role === Roles.ADMIN)
    return <Navigate to="/admin/dashboard" replace />;
  if (user.role === Roles.DOCTOR)
    return <Navigate to="/doctor/dashboard" replace />;
  if (user.role === Roles.STAFF)
    return <Navigate to="/staff/dashboard" replace />;
  return <Navigate to="/login" replace />;
}
