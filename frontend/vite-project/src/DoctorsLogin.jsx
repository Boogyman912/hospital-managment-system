import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Header from "./components/Header.jsx";
import Footer from "./components/Footer.jsx";

export default function DoctorsLogin() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setSubmitting(true);
    try {
      // Replace with your backend auth endpoint
      const response = await fetch("/api/auth/doctor/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });
      if (!response.ok) {
        throw new Error("Invalid credentials");
      }
      // const data = await response.json()
      navigate("/doctors");
    } catch (e) {
      setError(e.message || "Login failed");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="bg-gray-900 min-h-screen text-white flex flex-col">
      <Header />

      <main className="px-8 md:px-16 py-12 flex-1">
        <div className="container mx-auto max-w-md">
          <h2 className="text-2xl font-bold mb-6">Doctors Login</h2>
          <p className="text-xs text-gray-500 mb-4">
            Development only:{" "}
            <Link to="/register-doctor" className="text-blue-400 underline">
              Register a new doctor
            </Link>
          </p>
          <form className="space-y-4" onSubmit={handleSubmit}>
            <div>
              <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                required
              />
            </div>
            <div>
              <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                required
              />
            </div>
            {error && <p className="text-red-400 text-sm">{error}</p>}
            <button
              type="submit"
              disabled={submitting}
              className="w-full bg-blue-600 hover:bg-blue-700 disabled:opacity-60 text-white font-bold py-3 px-4 rounded-lg transition-colors"
            >
              {submitting ? "Logging in..." : "Login"}
            </button>
          </form>
        </div>
      </main>
      <Footer />
    </div>
  );
}
