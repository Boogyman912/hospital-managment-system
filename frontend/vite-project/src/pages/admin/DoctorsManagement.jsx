import React, { useState, useEffect } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import Modal from "../../components/ui/Modal.jsx";
import { apiGet, apiPost, apiDelete } from "../../api.js";

export default function DoctorsManagement() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [doctors, setDoctors] = useState([]); // State for doctor list
  const [open, setOpen] = useState(false); // Register modal
  const [viewOpen, setViewOpen] = useState(false); // View modal
  const [deleteOpen, setDeleteOpen] = useState(false); // Delete modal
  const [selectedDoctor, setSelectedDoctor] = useState(null); // For View/Delete
  const [form, setForm] = useState({
    // State for new doctor form
    username: "",
    password: "",
    name: "",
    specialization: "",
    email: "",
    phone_number: "",
    active: true,
  });

  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const data = await apiGet("/api/home/doctors"); // Fetch real data
      setDoctors(Array.isArray(data) ? data : []);
    } catch (e) {
      setError(e?.message || "Failed to load doctors");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleRegisterSave = async () => {
    try {
      // Construct the nested payload
      const payload = {
        user: {
          username: form.username,
          password: form.password,
        },
        doctor: {
          name: form.name,
          specialization: form.specialization,
          email: form.email,
          phone_number: form.phone_number,
          active: form.active,
        },
      };
      await apiPost("/api/admin/register-doctor", payload);
      setOpen(false); // Close modal
      setForm({
        // Reset form
        username: "",
        password: "",
        name: "",
        specialization: "",
        email: "",
        phone_number: "",
        active: true,
      });
      load(); // Refresh table
    } catch (e) {
      alert(e?.message || "Failed to register doctor");
    }
  };

  const handleDelete = async () => {
    if (!selectedDoctor) return;
    try {
      // Use doctor_id from the row object (which comes from the GET /api/home/doctors response)
      const doctorId = selectedDoctor.doctor_id || selectedDoctor.doctorId;
      await apiDelete(`/api/admin/delete-doctor/${doctorId}`);
      load();
      setDeleteOpen(false);
      setSelectedDoctor(null);
    } catch (e) {
      alert(e?.message || "Failed to delete doctor");
      setDeleteOpen(false);
    }
  };

  const columns = [
    { key: "name", header: "Name" },
    { key: "specialization", header: "Specialization" },
    { key: "email", header: "Email" },
    { key: "phone_number", header: "Phone" },
    {
      key: "active",
      header: "Active Status",
      render: (v) => (v ? "Active" : "Inactive"),
    },
  ];

  return (
    <div className="space-y-6">
      <Card
        title="Doctors"
        actions={
          <button
            onClick={() => setOpen(true)}
            className="bg-blue-600 hover:bg-blue-700 px-3 py-2 rounded transform transition-colors duration-150 active:scale-95"
          >
            Register Doctor
          </button>
        }
      >
        {loading && (
          <div className="text-sm text-gray-400 mb-2">Loading...</div>
        )}
        {error && <div className="text-sm text-red-400 mb-2">{error}</div>}
        <Table
          columns={columns}
          data={doctors} // Use data from state
          renderActions={(row) => (
            <div className="flex gap-2">
              <button
                onClick={() => {
                  setSelectedDoctor(row);
                  setViewOpen(true);
                }}
                className="px-2 py-1 bg-gray-700 hover:bg-gray-600 rounded transform transition-colors duration-150 active:scale-95"
              >
                View
              </button>
              {/* Edit button removed as per request */}
              <button
                onClick={() => {
                  setSelectedDoctor(row);
                  setDeleteOpen(true);
                }}
                className="px-2 py-1 bg-red-700 hover:bg-red-800 rounded transform transition-colors duration-150 active:scale-95"
              >
                Delete
              </button>
            </div>
          )}
        />
      </Card>
      {/* Register Doctor Modal */}
      <Modal
        title="Register Doctor"
        open={open}
        onClose={() => setOpen(false)}
        footer={
          <>
            <button
              onClick={() => setOpen(false)}
              className="px-3 py-2 bg-gray-700 hover:bg-gray-600 rounded transform transition-colors duration-150 active:scale-95"
            >
              Cancel
            </button>
            <button
              onClick={handleRegisterSave}
              className="px-3 py-2 bg-blue-600 hover:bg-blue-700 rounded transform transition-colors duration-150 active:scale-95"
            >
              Save
            </button>
          </>
        }
      >
        <form className="space-y-3">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            <input
              className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
              placeholder="Name"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
            />
            <input
              className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
              placeholder="Specialization"
              value={form.specialization}
              onChange={(e) =>
                setForm({ ...form, specialization: e.target.value })
              }
            />
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            <input
              className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
              placeholder="Email"
              type="email"
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
            />
            <input
              className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
              placeholder="Phone Number"
              value={form.phone_number}
              onChange={(e) =>
                setForm({ ...form, phone_number: e.target.value })
              }
            />
          </div>
          <div className="border-t border-gray-700 pt-3">
            <input
              className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
              placeholder="Username"
              value={form.username}
              onChange={(e) => setForm({ ...form, username: e.target.value })}
            />
          </div>
          <div>
            <input
              className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
              placeholder="Password"
              type="password"
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
            />
          </div>
          <label className="flex items-center gap-2 text-sm text-gray-300">
            <input
              type="checkbox"
              checked={form.active}
              onChange={(e) => setForm({ ...form, active: e.target.checked })}
            />
            Active
          </label>
        </form>
      </Modal>
      {/* View Doctor Modal */}
      <Modal
        title="Doctor Details"
        open={viewOpen}
        onClose={() => setViewOpen(false)}
        footer={
          <button
            onClick={() => setViewOpen(false)}
            className="px-3 py-2 bg-gray-700 hover:bg-gray-600 rounded transform transition-colors duration-150 active:scale-95"
          >
            Close
          </button>
        }
      >
        {selectedDoctor && (
          <div className="space-y-3">
            <div>
              <label className="text-sm text-gray-400">Name</label>
              <p className="text-gray-100">{selectedDoctor.name}</p>
            </div>
            <div>
              <label className="text-sm text-gray-400">Specialization</label>
              <p className="text-gray-100">{selectedDoctor.specialization}</p>
            </div>
            <div>
              <label className="text-sm text-gray-400">Email</label>
              <p className="text-gray-100">{selectedDoctor.email}</p>
            </div>
            <div>
              <label className="text-sm text-gray-400">Phone Number</label>
              <p className="text-gray-100">{selectedDoctor.phone_number}</p>
            </div>
            <div>
              <label className="text-sm text-gray-400">Status</label>
              <p className="text-gray-100">
                {selectedDoctor.active ? "Active" : "Inactive"}
              </p>
            </div>
          </div>
        )}
      </Modal>
      {/* Delete Confirmation Modal */}
      <Modal
        title="Confirm Deletion"
        open={deleteOpen}
        onClose={() => setDeleteOpen(false)}
        footer={
          <>
            <button
              onClick={() => setDeleteOpen(false)}
              className="px-3 py-2 bg-gray-700 hover:bg-gray-600 rounded transform transition-colors duration-150 active:scale-95"
            >
              Cancel
            </button>
            <button
              onClick={handleDelete}
              className="px-3 py-2 bg-red-700 hover:bg-red-800 rounded transform transition-colors duration-150 active:scale-95"
            >
              Confirm Delete
            </button>
          </>
        }
      >
        <p>
          Are you sure you want to delete this doctor:{" "}
          <strong className="text-white">{selectedDoctor?.name}</strong>?
        </p>
        <p className="text-sm text-gray-400 mt-2">
          This action cannot be undone.
        </p>
      </Modal>
    </div>
  );
}
