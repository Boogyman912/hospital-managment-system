import { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import Modal from "../../components/ui/Modal.jsx";
import { apiGet, apiPost, apiPatch, apiDelete } from "../../api.js";

export default function Rooms() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const [filteredData, setFilteredData] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [editingRoom, setEditingRoom] = useState(null);
  const [formData, setFormData] = useState({
    roomNumber: "",
    type: "GENERAL",
    status: "AVAILABLE",
  });
  const [statusFilter, setStatusFilter] = useState("");
  const [typeFilter, setTypeFilter] = useState("");

  const columns = [
    { key: "roomNumber", header: "Room Number" },
    { 
      key: "type", 
      header: "Type",
      render: (val) => val || "N/A"
    },
    { 
      key: "status", 
      header: "Status",
      render: (val) => val || "N/A"
    },
    { 
      key: "pricePerDay", 
      header: "Price Per Day",
      render: (val) => val ? `$${val.toFixed(2)}` : "N/A"
    },
  ];

  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiGet("/api/staff/rooms/all");
      setData(res || []);
      setFilteredData(res || []);
    } catch (e) {
      setError(e?.message || "Failed to load rooms");
    } finally {
      setLoading(false);
    }
  };

  const loadFiltered = async () => {
    setLoading(true);
    setError("");
    try {
      let endpoint = "/api/staff/rooms/all";
      if (statusFilter && typeFilter) {
        endpoint = `/api/staff/rooms/type/${typeFilter}/status/${statusFilter}`;
      } else if (statusFilter) {
        endpoint = `/api/staff/rooms/status/${statusFilter}`;
      } else if (typeFilter) {
        endpoint = `/api/staff/rooms/type/${typeFilter}`;
      }
      const res = await apiGet(endpoint);
      setFilteredData(res || []);
    } catch (e) {
      setError(e?.message || "Failed to load rooms");
      setFilteredData([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  useEffect(() => {
    if (statusFilter || typeFilter) {
      loadFiltered();
    } else {
      setFilteredData(data);
    }
  }, [statusFilter, typeFilter]);

  const handleCreate = () => {
    setEditingRoom(null);
    setFormData({ roomNumber: "", type: "GENERAL", status: "AVAILABLE" });
    setModalOpen(true);
  };

  const handleEdit = (room) => {
    setEditingRoom(room);
    setFormData({
      roomNumber: room.roomNumber,
      type: room.type,
      status: room.status,
    });
    setModalOpen(true);
  };

  const handleDelete = async (room) => {
    if (!confirm(`Delete room ${room.roomNumber}?`)) return;
    try {
      await apiDelete(`/api/staff/rooms/delete/${room.roomId}`);
      load();
    } catch (e) {
      alert(e?.message || "Failed to delete room");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingRoom) {
        await apiPatch(`/api/staff/rooms/update/${editingRoom.roomId}`, formData);
      } else {
        await apiPost("/api/staff/rooms/create", formData);
      }
      setModalOpen(false);
      load();
    } catch (e) {
      alert(e?.message || "Failed to save room");
    }
  };

  const handleClearFilters = () => {
    setStatusFilter("");
    setTypeFilter("");
    setFilteredData(data);
  };

  return (
    <Card
      title="Manage Rooms"
      actions={
        <button
          onClick={handleCreate}
          className="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          Create Room
        </button>
      }
    >
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}
      
      <div className="flex gap-2 mb-4">
        <select
          value={typeFilter}
          onChange={(e) => setTypeFilter(e.target.value)}
          className="px-3 py-1 bg-gray-700 border border-gray-600 rounded text-gray-200"
        >
          <option value="">All Types</option>
          <option value="GENERAL">GENERAL</option>
          <option value="ICU">ICU</option>
          <option value="PRIVATE">PRIVATE</option>
        </select>
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
          className="px-3 py-1 bg-gray-700 border border-gray-600 rounded text-gray-200"
        >
          <option value="">All Status</option>
          <option value="AVAILABLE">AVAILABLE</option>
          <option value="OCCUPIED">OCCUPIED</option>
        </select>
        {(statusFilter || typeFilter) && (
          <button
            onClick={handleClearFilters}
            className="px-3 py-1 bg-gray-600 text-white rounded hover:bg-gray-700"
          >
            Clear Filters
          </button>
        )}
      </div>

      <Table
        columns={columns}
        data={filteredData}
        renderActions={(row) => (
          <>
            <button
              onClick={() => handleEdit(row)}
              className="px-2 py-1 bg-yellow-600 text-white text-xs rounded hover:bg-yellow-700"
            >
              Edit
            </button>
            <button
              onClick={() => handleDelete(row)}
              className="px-2 py-1 bg-red-600 text-white text-xs rounded hover:bg-red-700"
            >
              Delete
            </button>
          </>
        )}
      />

      <Modal
        title={editingRoom ? "Edit Room" : "Create Room"}
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        footer={
          <>
            <button
              onClick={() => setModalOpen(false)}
              className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700"
            >
              Cancel
            </button>
            <button
              onClick={handleSubmit}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              {editingRoom ? "Update" : "Create"}
            </button>
          </>
        }
      >
        <form onSubmit={handleSubmit} className="space-y-3">
          <div>
            <label className="block text-sm mb-1">Room Number</label>
            <input
              type="text"
              value={formData.roomNumber}
              onChange={(e) => setFormData({ ...formData, roomNumber: e.target.value })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
            />
          </div>
          <div>
            <label className="block text-sm mb-1">Type</label>
            <select
              value={formData.type}
              onChange={(e) => setFormData({ ...formData, type: e.target.value })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
            >
              <option value="GENERAL">GENERAL</option>
              <option value="ICU">ICU</option>
              <option value="PRIVATE">PRIVATE</option>
            </select>
          </div>
          <div>
            <label className="block text-sm mb-1">Status</label>
            <select
              value={formData.status}
              onChange={(e) => setFormData({ ...formData, status: e.target.value })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
            >
              <option value="AVAILABLE">AVAILABLE</option>
              <option value="OCCUPIED">OCCUPIED</option>
            </select>
          </div>
        </form>
      </Modal>
    </Card>
  );
}
