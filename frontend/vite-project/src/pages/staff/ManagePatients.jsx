import { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import Modal from "../../components/ui/Modal.jsx";
import { apiGet, apiPost, apiDelete } from "../../api.js";

export default function ManagePatients() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [patients, setPatients] = useState([]);
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [emergencyContacts, setEmergencyContacts] = useState([]);
  const [detailsModalOpen, setDetailsModalOpen] = useState(false);
  const [addContactModalOpen, setAddContactModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    patient: { patientId: "" },
    contactName: "",
    relationship: "",
    phoneNumber: "",
  });

  const patientColumns = [
    { 
      key: "patient", 
      header: "Patient",
      render: (val) => val?.name || "N/A"
    },
    { 
      key: "patient", 
      header: "Phone",
      render: (val) => val?.phoneNumber || "N/A"
    },
    { 
      key: "patient", 
      header: "Email",
      render: (val) => val?.email || "N/A"
    },
  ];

  const contactColumns = [
    { key: "contactName", header: "Name" },
    { key: "relationship", header: "Relationship" },
    { key: "phoneNumber", header: "Phone Number" },
  ];

  const loadPatients = async () => {
    setLoading(true);
    setError("");
    try {
      // Get patients from inpatients list
      const inpatientsRes = await apiGet("/api/staff/inpatients/all");
      // Extract unique patients
      const uniquePatients = [];
      const patientIds = new Set();
      (inpatientsRes || []).forEach((inpatient) => {
        if (inpatient.patient && !patientIds.has(inpatient.patient.patientId)) {
          patientIds.add(inpatient.patient.patientId);
          uniquePatients.push(inpatient);
        }
      });
      setPatients(uniquePatients);
    } catch (e) {
      setError(e?.message || "Failed to load patients");
    } finally {
      setLoading(false);
    }
  };

  const loadEmergencyContacts = async (patientId) => {
    try {
      const res = await apiGet(`/api/staff/emergency-contacts/patient/${patientId}`);
      setEmergencyContacts(res || []);
    } catch (e) {
      console.error("Failed to load emergency contacts:", e);
      setEmergencyContacts([]);
    }
  };

  useEffect(() => {
    loadPatients();
  }, []);

  const handleViewDetails = (patientData) => {
    setSelectedPatient(patientData.patient);
    loadEmergencyContacts(patientData.patient.patientId);
    setDetailsModalOpen(true);
  };

  const handleAddContact = () => {
    setFormData({
      patient: { patientId: selectedPatient.patientId },
      contactName: "",
      relationship: "",
      phoneNumber: "",
    });
    setAddContactModalOpen(true);
  };

  const handleDeleteContact = async (contact) => {
    if (!confirm(`Delete contact ${contact.contactName}?`)) return;
    try {
      await apiDelete(`/api/staff/emergency-contacts/delete/${contact.contactId}`);
      loadEmergencyContacts(selectedPatient.patientId);
    } catch (e) {
      alert(e?.message || "Failed to delete contact");
    }
  };

  const handleAddContactSubmit = async (e) => {
    e.preventDefault();
    try {
      await apiPost("/api/staff/emergency-contacts/create", formData);
      setAddContactModalOpen(false);
      loadEmergencyContacts(selectedPatient.patientId);
    } catch (e) {
      alert(e?.message || "Failed to add contact");
    }
  };

  return (
    <Card title="Manage Patients">
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}

      <Table
        columns={patientColumns}
        data={patients}
        renderActions={(row) => (
          <button
            onClick={() => handleViewDetails(row)}
            className="px-2 py-1 bg-blue-600 text-white text-xs rounded hover:bg-blue-700"
          >
            View Details
          </button>
        )}
      />

      <Modal
        title={`Patient Details - ${selectedPatient?.name || ""}`}
        open={detailsModalOpen}
        onClose={() => setDetailsModalOpen(false)}
        footer={
          <button
            onClick={() => setDetailsModalOpen(false)}
            className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700"
          >
            Close
          </button>
        }
      >
        <div className="space-y-4">
          <div>
            <h4 className="font-semibold mb-2">Patient Information</h4>
            <div className="text-sm space-y-1">
              <p>Name: {selectedPatient?.name}</p>
              <p>Email: {selectedPatient?.email || "N/A"}</p>
              <p>Phone: {selectedPatient?.phoneNumber || "N/A"}</p>
              <p>Date of Birth: {selectedPatient?.dateOfBirth || "N/A"}</p>
              <p>Gender: {selectedPatient?.gender || "N/A"}</p>
            </div>
          </div>
          
          <div>
            <div className="flex items-center justify-between mb-2">
              <h4 className="font-semibold">Emergency Contacts</h4>
              <button
                onClick={handleAddContact}
                className="px-2 py-1 bg-green-600 text-white text-xs rounded hover:bg-green-700"
              >
                Add Contact
              </button>
            </div>
            <Table
              columns={contactColumns}
              data={emergencyContacts}
              renderActions={(contact) => (
                <button
                  onClick={() => handleDeleteContact(contact)}
                  className="px-2 py-1 bg-red-600 text-white text-xs rounded hover:bg-red-700"
                >
                  Delete
                </button>
              )}
            />
          </div>
        </div>
      </Modal>

      <Modal
        title="Add Emergency Contact"
        open={addContactModalOpen}
        onClose={() => setAddContactModalOpen(false)}
        footer={
          <>
            <button
              onClick={() => setAddContactModalOpen(false)}
              className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700"
            >
              Cancel
            </button>
            <button
              onClick={handleAddContactSubmit}
              className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
            >
              Add
            </button>
          </>
        }
      >
        <form onSubmit={handleAddContactSubmit} className="space-y-3">
          <div>
            <label className="block text-sm mb-1">Contact Name</label>
            <input
              type="text"
              value={formData.contactName}
              onChange={(e) => setFormData({ ...formData, contactName: e.target.value })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
            />
          </div>
          <div>
            <label className="block text-sm mb-1">Relationship</label>
            <input
              type="text"
              value={formData.relationship}
              onChange={(e) => setFormData({ ...formData, relationship: e.target.value })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
            />
          </div>
          <div>
            <label className="block text-sm mb-1">Phone Number</label>
            <input
              type="tel"
              value={formData.phoneNumber}
              onChange={(e) => setFormData({ ...formData, phoneNumber: e.target.value })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
            />
          </div>
        </form>
      </Modal>
    </Card>
  );
}
