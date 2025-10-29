import React, { useState, useEffect } from "react";
import { apiGet, apiPost } from "./api.js";

const DoctorManagement = () => {
  const [doctors, setDoctors] = useState([]);
  const [showAddForm, setShowAddForm] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [newDoctor, setNewDoctor] = useState({
    name: "",
    specialization: "",
    username: "",
    hashedpassword: "",
    email: "",
    phone_number: "",
    active: true,
  });

  useEffect(() => {
    fetchDoctors();
  }, []);

  const fetchDoctors = async () => {
    try {
      const data = await apiGet("/api/home/doctors");
      setDoctors(data || []);
    } catch (err) {
      setError("Failed to load doctors");
    }
  };

  const handleAddDoctor = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setSuccess("");

    try {
      await apiPost("/api/admin/doctors/create", newDoctor);
      setSuccess("Doctor added successfully!");
      setNewDoctor({
        name: "",
        specialization: "",
        username: "",
        hashedpassword: "",
        email: "",
        phone_number: "",
        active: true,
      });
      setShowAddForm(false);
      fetchDoctors();
    } catch (err) {
      setError("Failed to add doctor: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setNewDoctor((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  return (
    <div className="min-h-screen bg-gray-900 py-8">
      <div className="max-w-6xl mx-auto px-4">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-3xl font-bold text-white">Doctor Management</h1>
          <button
            onClick={() => setShowAddForm(true)}
            className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-lg transition-colors"
          >
            Add New Doctor
          </button>
        </div>

        {error && (
          <div className="bg-red-800 border border-red-600 text-red-200 px-4 py-3 rounded mb-6">
            {error}
          </div>
        )}

        {success && (
          <div className="bg-green-800 border border-green-600 text-green-200 px-4 py-3 rounded mb-6">
            {success}
          </div>
        )}

        {/* Add Doctor Form */}
        {showAddForm && (
          <div className="bg-gray-800 rounded-lg p-6 mb-8">
            <h2 className="text-2xl font-bold text-white mb-6">
              Add New Doctor
            </h2>
            <form onSubmit={handleAddDoctor} className="space-y-4">
              <div className="grid md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-white mb-2">Full Name</label>
                  <input
                    type="text"
                    name="name"
                    value={newDoctor.name}
                    onChange={handleInputChange}
                    className="w-full bg-gray-700 border border-gray-600 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>
                <div>
                  <label className="block text-white mb-2">
                    Specialization
                  </label>
                  <input
                    type="text"
                    name="specialization"
                    value={newDoctor.specialization}
                    onChange={handleInputChange}
                    className="w-full bg-gray-700 border border-gray-600 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>
              </div>

              <div className="grid md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-white mb-2">Username</label>
                  <input
                    type="text"
                    name="username"
                    value={newDoctor.username}
                    onChange={handleInputChange}
                    className="w-full bg-gray-700 border border-gray-600 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>
                <div>
                  <label className="block text-white mb-2">Password</label>
                  <input
                    type="password"
                    name="hashedpassword"
                    value={newDoctor.hashedpassword}
                    onChange={handleInputChange}
                    className="w-full bg-gray-700 border border-gray-600 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>
              </div>

              <div className="grid md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-white mb-2">Email</label>
                  <input
                    type="email"
                    name="email"
                    value={newDoctor.email}
                    onChange={handleInputChange}
                    className="w-full bg-gray-700 border border-gray-600 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>
                <div>
                  <label className="block text-white mb-2">Phone Number</label>
                  <input
                    type="tel"
                    name="phone_number"
                    value={newDoctor.phone_number}
                    onChange={handleInputChange}
                    className="w-full bg-gray-700 border border-gray-600 rounded-lg px-4 py-3 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    required
                  />
                </div>
              </div>

              <div className="flex items-center space-x-4">
                <input
                  type="checkbox"
                  name="active"
                  checked={newDoctor.active}
                  onChange={handleInputChange}
                  className="w-4 h-4 text-blue-600 bg-gray-700 border-gray-600 rounded focus:ring-blue-500"
                />
                <label className="text-white">Active</label>
              </div>

              <div className="flex space-x-4">
                <button
                  type="submit"
                  disabled={loading}
                  className="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-6 rounded-lg transition-colors disabled:opacity-50"
                >
                  {loading ? "Adding..." : "Add Doctor"}
                </button>
                <button
                  type="button"
                  onClick={() => setShowAddForm(false)}
                  className="bg-gray-600 hover:bg-gray-700 text-white font-bold py-2 px-6 rounded-lg transition-colors"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {/* Doctors List */}
        <div className="bg-gray-800 rounded-lg p-6">
          <h2 className="text-2xl font-bold text-white mb-6">
            Current Doctors
          </h2>
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {doctors.map((doctor) => (
              <div
                key={doctor.doctor_id}
                className="bg-gray-700 rounded-lg p-6"
              >
                <div className="flex items-center justify-between mb-4">
                  <div className="w-12 h-12 bg-blue-600 rounded-full flex items-center justify-center">
                    <span className="text-white font-bold text-lg">
                      {doctor.name.charAt(0)}
                    </span>
                  </div>
                  <span
                    className={`px-2 py-1 rounded text-xs font-semibold ${
                      doctor.active
                        ? "bg-green-800 text-green-200"
                        : "bg-red-800 text-red-200"
                    }`}
                  >
                    {doctor.active ? "Active" : "Inactive"}
                  </span>
                </div>
                <h3 className="text-xl font-semibold text-white mb-2">
                  {doctor.name}
                </h3>
                <p className="text-blue-400 mb-2">{doctor.specialization}</p>
                <p className="text-gray-400 text-sm mb-1">{doctor.email}</p>
                <p className="text-gray-400 text-sm">{doctor.phone_number}</p>
              </div>
            ))}
          </div>

          {doctors.length === 0 && (
            <div className="text-center text-gray-400 py-8">
              No doctors found. Add your first doctor to get started.
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default DoctorManagement;
