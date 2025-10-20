import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Roles, useAuth } from "../auth/AuthContext.jsx";

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const onSubmit = async (e) => {
    e.preventDefault();
    setError("");
    if (!username || !password) return setError("Enter username and password");
    setLoading(true);
    try {
      const u = await login(username, password);
      if (u.role === Roles.ADMIN)
        navigate("/admin/dashboard", { replace: true });
      else if (u.role === Roles.DOCTOR)
        navigate("/doctor/dashboard", { replace: true });
      else navigate("/staff/dashboard", { replace: true });
    } catch (err) {
      setError(err?.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-gray-100 flex items-center justify-center p-4">
      <form
        onSubmit={onSubmit}
        className="w-full max-w-md bg-gray-800 border border-gray-700 rounded-lg p-6"
      >
        <h1 className="text-2xl font-semibold mb-4">Login</h1>
        <label className="block mb-2 text-sm text-gray-300">Username</label>
        <input
          className="w-full mb-4 px-3 py-2 bg-gray-900 border border-gray-700 rounded"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          placeholder="Enter name"
        />
        <label className="block mb-2 text-sm text-gray-300">Password</label>
        <input
          type="password"
          className="w-full mb-4 px-3 py-2 bg-gray-900 border border-gray-700 rounded"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="Enter password"
        />
        {error && <div className="mb-3 text-red-400 text-sm">{error}</div>}
        <button
          disabled={loading}
          className="w-full bg-blue-600 hover:bg-blue-700 disabled:opacity-60 px-4 py-2 rounded font-medium"
        >
          {loading ? "Logging in..." : "Login"}
        </button>
      </form>
    </div>
  );
}
