import { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import Modal from "../../components/ui/Modal.jsx";
import { apiGet, apiPost, apiPut, apiDelete } from "../../api.js";

export default function StaffManagement() {
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [staff, setStaff] = useState([]);
  const [viewOpen, setViewOpen] = useState(false);
  const [editOpen, setEditOpen] = useState(false);
  const [deleteConfirmOpen, setDeleteConfirmOpen] = useState(false);
  const [selectedStaff, setSelectedStaff] = useState(null);
  const [editForm, setEditForm] = useState({
    staffId: null,
    name: "",
    role: "",
    email: "",
    phoneNumber: "",
    dateOfJoining: "",
  });
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
      const data = await apiGet("/api/admin/staff/all");
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

  const handleEditSave = async () => {
    if (!editForm.staffId) return;
    try {
      await apiPut(`/api/admin/staff/update/${editForm.staffId}`, editForm);
      setEditOpen(false);
      load();
    } catch (e) {
      alert(e?.message || "Failed to update staff");
    }
  };

  const handleDelete = async () => {
    if (!selectedStaff) return;
    try {
      await apiDelete(`/api/admin/staff/delete/${selectedStaff.staffId}`);
      load();
      setDeleteConfirmOpen(false);
      setSelectedStaff(null);
    } catch (e) {
      alert(e?.message || "Failed to delete staff");
      setDeleteConfirmOpen(false);
    }
  };

  return (
    <div className="space-y-6">
      <Card
        title="Staff"
        actions={
          <button
            onClick={() => setOpen(true)}
            // --- Added responsiveness ---
            className="bg-green-600 hover:bg-green-700 px-3 py-2 rounded transform transition-colors duration-150 active:scale-95"
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
          renderActions={(row) => (
            <div className="flex gap-2">
              <button
                onClick={() => {
                  setSelectedStaff(row);
                  setViewOpen(true);
                }}
                // --- Added responsiveness ---
                className="px-2 py-1 bg-gray-700 hover:bg-gray-600 rounded transform transition-colors duration-150 active:scale-95"
              >
                View
              </button>
              <button
                onClick={() => {
                  setSelectedStaff(row);
                  setEditForm({
                    staffId: row.staffId,
                    name: row.name,
                    role: row.role,
                    email: row.email,
                    phoneNumber: row.phoneNumber,
                    dateOfJoining: row.dateOfJoining,
                  });
                  setEditOpen(true);
                }}
                // --- Added responsiveness ---
                className="px-2 py-1 bg-gray-700 hover:bg-gray-600 rounded transform transition-colors duration-150 active:scale-95"
              >
                Edit
              </button>
              <button
                onClick={() => {
                  setSelectedStaff(row);
                  setDeleteConfirmOpen(true);
                }}
                // --- Added responsiveness ---
                className="px-2 py-1 bg-red-700 hover:bg-red-800 rounded transform transition-colors duration-150 active:scale-95"
              >
                Delete
              </button>
            </div>
          )}
        />
      </Card>

      {/* Register Staff Modal */}
      <Modal
        title="Register Staff"
        open={open}
        onClose={() => setOpen(false)}
        footer={
          <>
            <button
              onClick={() => setOpen(false)}
              // --- Added responsiveness ---
              className="px-3 py-2 bg-gray-700 hover:bg-gray-600 rounded transform transition-colors duration-150 active:scale-95"
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
              // --- Added responsiveness ---
              className="px-3 py-2 bg-green-600 hover:bg-green-700 rounded transform transition-colors duration-150 active:scale-95"
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

      {/* View Staff Modal */}
      <Modal
        title="Staff Details"
        open={viewOpen}
        onClose={() => setViewOpen(false)}
        footer={
          <button
            onClick={() => setViewOpen(false)}
            // --- Added responsiveness ---
            className="px-3 py-2 bg-gray-700 hover:bg-gray-600 rounded transform transition-colors duration-150 active:scale-95"
          >
            Close
          </button>
        }
      >
        {selectedStaff && (
          <div className="space-y-3">
            <div>
              <label className="text-sm text-gray-400">Name</label>
              <p className="text-gray-100">{selectedStaff.name}</p>
            </div>
            <div>
              <label className="text-sm text-gray-400">Role</label>
              <p className="text-gray-100">{selectedStaff.role}</p>
            </div>
            <div>
              <label className="text-sm text-gray-400">Email</label>
              <p className="text-gray-100">{selectedStaff.email}</p>
            </div>
            <div>
              <label className="text-sm text-gray-400">Phone Number</label>
              <p className="text-gray-100">{selectedStaff.phoneNumber}</p>
            </div>
            <div>
              <label className="text-sm text-gray-400">Date of Joining</label>
              <p className="text-gray-100">{selectedStaff.dateOfJoining}</p>
            </div>
          </div>
        )}
      </Modal>

      {/* Edit Staff Modal */}
      <Modal
        title="Edit Staff"
        open={editOpen}
        onClose={() => setEditOpen(false)}
        footer={
          <>
            <button
              onClick={() => setEditOpen(false)}
              // --- Added responsiveness ---
              className="px-3 py-2 bg-gray-700 hover:bg-gray-600 rounded transform transition-colors duration-150 active:scale-95"
            >
              Cancel
            </button>
            <button
              onClick={handleEditSave}
              // --- Added responsiveness ---
              className="px-3 py-2 bg-blue-600 hover:bg-blue-700 rounded transform transition-colors duration-150 active:scale-95"
            >
              Save Changes
            </button>
          </>
        }
      >
        <form className="space-y-3">
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Name"
            value={editForm.name}
            onChange={(e) => setEditForm({ ...editForm, name: e.target.value })}
          />
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Role"
            value={editForm.role}
            onChange={(e) => setEditForm({ ...editForm, role: e.target.value })}
          />
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Email"
            value={editForm.email}
            onChange={(e) =>
              setEditForm({ ...editForm, email: e.target.value })
            }
          />
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Phone"
            value={editForm.phoneNumber}
            onChange={(e) =>
              setEditForm({ ...editForm, phoneNumber: e.target.value })
            }
          />
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Joining Date (YYYY-MM-DD)"
            value={editForm.dateOfJoining}
            onChange={(e) =>
              setEditForm({ ...editForm, dateOfJoining: e.target.value })
            }
          />
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        title="Confirm Deletion"
        open={deleteConfirmOpen}
        onClose={() => setDeleteConfirmOpen(false)}
        footer={
          <>
            <button
              onClick={() => setDeleteConfirmOpen(false)}
              // --- Added responsiveness ---
              className="px-3 py-2 bg-gray-700 hover:bg-gray-600 rounded transform transition-colors duration-150 active:scale-95"
            >
              Cancel
            </button>
            <button
              onClick={handleDelete}
              // --- Added responsiveness ---
              className="px-3 py-2 bg-red-700 hover:bg-red-800 rounded transform transition-colors duration-150 active:scale-95"
            >
              Confirm Delete
            </button>
          </>
        }
      >
        <p>
          Are you sure you want to delete this staff member:{" "}
          <strong className="text-white">{selectedStaff?.name}</strong>?
        </p>
        <p className="text-sm text-gray-400 mt-2">
          This action cannot be undone.
        </p>
      </Modal>
    </div>
  );
}
