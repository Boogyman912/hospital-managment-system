import React, { useState } from "react";
import Header from "./components/Header.jsx";
import Footer from "./components/Footer.jsx";
import { apiPost } from "./api.js";

export default function RegisterDoctor() {
  const [form, setForm] = useState({
    name: "",
    specialization: "",
    username: "",
    hashedpassword: "",
    email: "",
    phone_number: "",
    active: true,
  });
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  function handleChange(e) {
    const { name, type, checked, value } = e.target;
    setForm((f) => ({ ...f, [name]: type === "checkbox" ? checked : value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSubmitting(true);
    setError("");
    setSuccess("");
    try {
      await apiPost("/api/admin/register-doctor", form);
      setSuccess("Doctor registered successfully");
      setForm({
        name: "",
        specialization: "",
        username: "",
        hashedpassword: "",
        email: "",
        phone_number: "",
        active: true,
      });
    } catch (err) {
      setError(err.message || "Failed to register doctor");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="bg-gray-900 min-h-screen text-white flex flex-col">
      <Header />
      <main className="px-8 md:px-16 py-12 flex-1">
        <div className="container mx-auto max-w-2xl">
          <h1 className="text-3xl font-bold mb-6">Register New Doctor</h1>
          {success && <p className="text-green-400 mb-4">{success}</p>}
          {error && <p className="text-red-400 mb-4">{error}</p>}
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm mb-1">Name</label>
              <input
                name="name"
                value={form.name}
                onChange={handleChange}
                required
                className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3"
              />
            </div>
            <div>
              <label className="block text-sm mb-1">Specialization</label>
              <input
                name="specialization"
                value={form.specialization}
                onChange={handleChange}
                required
                className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3"
              />
            </div>
            <div className="grid md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm mb-1">Username</label>
                <input
                  name="username"
                  value={form.username}
                  onChange={handleChange}
                  required
                  className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3"
                />
              </div>
              <div>
                <label className="block text-sm mb-1">Hashed Password</label>
                <input
                  name="hashedpassword"
                  value={form.hashedpassword}
                  onChange={handleChange}
                  required
                  className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3"
                />
              </div>
            </div>
            <div className="grid md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm mb-1">Email</label>
                <input
                  type="email"
                  name="email"
                  value={form.email}
                  onChange={handleChange}
                  className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3"
                />
              </div>
              <div>
                <label className="block text-sm mb-1">Phone Number</label>
                <input
                  name="phone_number"
                  value={form.phone_number}
                  onChange={handleChange}
                  className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-3"
                />
              </div>
            </div>
            <div className="flex items-center gap-2">
              <input
                id="active"
                type="checkbox"
                name="active"
                checked={form.active}
                onChange={handleChange}
                className="h-4 w-4"
              />
              <label htmlFor="active" className="text-sm">
                Active
              </label>
            </div>
            <div className="flex justify-end">
              <button
                type="submit"
                disabled={submitting}
                className="bg-blue-600 hover:bg-blue-700 disabled:opacity-60 px-6 py-3 rounded-lg"
              >
                {submitting ? "Submitting..." : "Register Doctor"}
              </button>
            </div>
          </form>
        </div>
      </main>
      <Footer />
    </div>
  );
}
