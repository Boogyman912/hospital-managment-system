import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./AuthContext.jsx";
import { Roles } from "./roles.js";

export default function ProtectedRoute({
  allow = [Roles.ADMIN, Roles.DOCTOR, Roles.STAFF],
}) {
  const { user, isReady } = useAuth();
  if (!isReady) return null; // wait until auth rehydration
  if (!user) return <Navigate to="/login" replace />;
  if (!allow.includes(user.role)) return <Navigate to="/dashboard" replace />;
  return <Outlet />;
}
