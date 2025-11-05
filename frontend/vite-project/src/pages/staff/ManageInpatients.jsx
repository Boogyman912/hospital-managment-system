import { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import Modal from "../../components/ui/Modal.jsx";
import { apiGet, apiPost } from "../../api.js";

export default function ManageInpatients() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const [admitModalOpen, setAdmitModalOpen] = useState(false);
  const [updateRoomModalOpen, setUpdateRoomModalOpen] = useState(false);
  const [selectedInpatient, setSelectedInpatient] = useState(null);
  const [rooms, setRooms] = useState([]);
  const [formData, setFormData] = useState({
    patient: { patientId: "" },
    room: { roomId: "" },
    admissionDate: new Date().toISOString().split("T")[0],
  });

  const columns = [
    { 
      key: "patient", 
      header: "Patient",
      render: (val) => val?.name || "N/A"
    },
    { 
      key: "room", 
      header: "Room",
      render: (val) => val?.roomNumber || "N/A"
    },
    { 
      key: "admissionDate", 
      header: "Admission Date",
      render: (val) => val || "N/A"
    },
    { 
      key: "dischargeDate", 
      header: "Discharge Date",
      render: (val) => val || "Not Discharged"
    },
    { 
      key: "isBilled", 
      header: "Billed",
      render: (val) => val ? "Yes" : "No"
    },
  ];

  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiGet("/api/staff/inpatients/all");
      setData(res || []);
    } catch (e) {
      setError(e?.message || "Failed to load inpatients");
    } finally {
      setLoading(false);
    }
  };

  const loadPatientsAndRooms = async () => {
    try {
      // Get available rooms
      const roomsRes = await apiGet("/api/staff/rooms/status/AVAILABLE");
      setRooms(roomsRes || []);
    } catch (e) {
      console.error("Failed to load rooms:", e);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleAdmit = () => {
    loadPatientsAndRooms();
    setFormData({
      patient: { patientId: "" },
      room: { roomId: "" },
      admissionDate: new Date().toISOString().split("T")[0],
    });
    setAdmitModalOpen(true);
  };

  const handleDischarge = async (inpatient) => {
    if (!confirm(`Discharge patient ${inpatient.patient?.name}?`)) return;
    try {
      await apiPost(`/api/staff/inpatients/discharge_in_patient/${inpatient.inpatientId}`);
      load();
    } catch (e) {
      alert(e?.message || "Failed to discharge patient");
    }
  };

  const handleUpdateRoom = (inpatient) => {
    loadPatientsAndRooms();
    setSelectedInpatient(inpatient);
    setFormData({
      room: { roomId: "" },
    });
    setUpdateRoomModalOpen(true);
  };

  const handleAdmitSubmit = async (e) => {
    e.preventDefault();
    try {
      await apiPost("/api/staff/inpatients/admit", formData);
      setAdmitModalOpen(false);
      load();
    } catch (e) {
      alert(e?.message || "Failed to admit patient");
    }
  };

  const handleUpdateRoomSubmit = async (e) => {
    e.preventDefault();
    try {
      await apiPost(`/api/staff/inpatients/updateRoom/${selectedInpatient.inpatientId}`, formData);
      setUpdateRoomModalOpen(false);
      load();
    } catch (e) {
      alert(e?.message || "Failed to update room");
    }
  };

  return (
    <Card
      title="Manage Inpatients"
      actions={
        <button
          onClick={handleAdmit}
          className="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          Admit Patient
        </button>
      }
    >
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}

      <Table
        columns={columns}
        data={data}
        renderActions={(row) => (
          <>
            {!row.dischargeDate && (
              <>
                <button
                  onClick={() => handleUpdateRoom(row)}
                  className="px-2 py-1 bg-yellow-600 text-white text-xs rounded hover:bg-yellow-700"
                >
                  Update Room
                </button>
                <button
                  onClick={() => handleDischarge(row)}
                  className="px-2 py-1 bg-green-600 text-white text-xs rounded hover:bg-green-700"
                >
                  Discharge
                </button>
              </>
            )}
          </>
        )}
      />

      <Modal
        title="Admit Patient"
        open={admitModalOpen}
        onClose={() => setAdmitModalOpen(false)}
        footer={
          <>
            <button
              onClick={() => setAdmitModalOpen(false)}
              className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700"
            >
              Cancel
            </button>
            <button
              onClick={handleAdmitSubmit}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              Admit
            </button>
          </>
        }
      >
        <form onSubmit={handleAdmitSubmit} className="space-y-3">
          <div>
            <label className="block text-sm mb-1">Patient ID</label>
            <input
              type="number"
              value={formData.patient.patientId}
              onChange={(e) => setFormData({ ...formData, patient: { patientId: e.target.value } })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
            />
          </div>
          <div>
            <label className="block text-sm mb-1">Room</label>
            <select
              value={formData.room.roomId}
              onChange={(e) => setFormData({ ...formData, room: { roomId: e.target.value } })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
            >
              <option value="">Select Room</option>
              {rooms.map((room) => (
                <option key={room.roomId} value={room.roomId}>
                  {room.roomNumber} - {room.type}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm mb-1">Admission Date</label>
            <input
              type="date"
              value={formData.admissionDate}
              onChange={(e) => setFormData({ ...formData, admissionDate: e.target.value })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
            />
          </div>
        </form>
      </Modal>

      <Modal
        title="Update Room"
        open={updateRoomModalOpen}
        onClose={() => setUpdateRoomModalOpen(false)}
        footer={
          <>
            <button
              onClick={() => setUpdateRoomModalOpen(false)}
              className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700"
            >
              Cancel
            </button>
            <button
              onClick={handleUpdateRoomSubmit}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              Update
            </button>
          </>
        }
      >
        <form onSubmit={handleUpdateRoomSubmit} className="space-y-3">
          <div>
            <label className="block text-sm mb-1">New Room</label>
            <select
              value={formData.room?.roomId || ""}
              onChange={(e) => setFormData({ ...formData, room: { roomId: e.target.value } })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
            >
              <option value="">Select Room</option>
              {rooms.map((room) => (
                <option key={room.roomId} value={room.roomId}>
                  {room.roomNumber} - {room.type}
                </option>
              ))}
            </select>
          </div>
        </form>
      </Modal>
    </Card>
  );
}
