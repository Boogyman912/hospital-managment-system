import React, { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import Modal from "../../components/ui/Modal.jsx";
import { apiGet, apiPost } from "../../api.js";

export default function StaffManagement() {
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [staff, setStaff] = useState([]);
  const [form, setForm] = useState({
    name: "",
    role: "",
    email: "",
    phoneNumber: "",
    dateOfJoining: "",
  });

  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const data = await apiGet("/staff/all");
      setStaff(data || []);
    } catch (e) {
      setError(e?.message || "Failed to load staff");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);
  const columns = [
    { key: "name", header: "Name" },
    { key: "role", header: "Role" },
    { key: "phoneNumber", header: "Phone" },
    { key: "dateOfJoining", header: "Joining Date" },
  ];

  return (
    <div className="space-y-6">
      <Card
        title="Staff"
        actions={
          <button
            onClick={() => setOpen(true)}
            className="bg-green-600 hover:bg-green-700 px-3 py-2 rounded"
          >
            Register Staff
          </button>
        }
      >
        {loading && (
          <div className="text-sm text-gray-400 mb-2">Loading...</div>
        )}
        {error && <div className="text-sm text-red-400 mb-2">{error}</div>}
        <Table
          columns={columns}
          data={staff}
          renderActions={() => (
            <>
              <button className="px-2 py-1 bg-gray-700 rounded">View</button>
              <button className="px-2 py-1 bg-gray-700 rounded">Edit</button>
              <button className="px-2 py-1 bg-red-700 rounded">Delete</button>
            </>
          )}
        />
      </Card>

      <Modal
        title="Register Staff"
        open={open}
        onClose={() => setOpen(false)}
        footer={
          <>
            <button
              onClick={() => setOpen(false)}
              className="px-3 py-2 bg-gray-700 rounded"
            >
              Cancel
            </button>
            <button
              onClick={async () => {
                try {
                  await apiPost("/api/admin/register-staff", {
                    user: {
                      username: form.email?.split("@")[0] || form.name,
                      password: "changeme",
                    },
                    staff: form,
                  });
                  setOpen(false);
                  setForm({
                    name: "",
                    role: "",
                    email: "",
                    phoneNumber: "",
                    dateOfJoining: "",
                  });
                  load();
                } catch (e) {
                  alert(e?.message || "Failed to register staff");
                }
              }}
              className="px-3 py-2 bg-green-600 rounded"
            >
              Save
            </button>
          </>
        }
      >
        <form className="space-y-3">
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Name"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
          />
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Role"
            value={form.role}
            onChange={(e) => setForm({ ...form, role: e.target.value })}
          />
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Email"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
          />
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Phone"
            value={form.phoneNumber}
            onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })}
          />
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Joining Date (YYYY-MM-DD)"
            value={form.dateOfJoining}
            onChange={(e) =>
              setForm({ ...form, dateOfJoining: e.target.value })
            }
          />
        </form>
      </Modal>
    </div>
  );
}
