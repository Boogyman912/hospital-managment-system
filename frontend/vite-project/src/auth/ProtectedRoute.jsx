import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { Roles, useAuth } from "./AuthContext.jsx";

export default function ProtectedRoute({
  allow = [Roles.ADMIN, Roles.DOCTOR, Roles.STAFF],
}) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (!allow.includes(user.role)) return <Navigate to="/dashboard" replace />;
  return <Outlet />;
}
