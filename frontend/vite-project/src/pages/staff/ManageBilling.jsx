import { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import Modal from "../../components/ui/Modal.jsx";
import { apiGet, apiPost } from "../../api.js";

export default function ManageBilling() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const [prescriptions, setPrescriptions] = useState([]);
  const [prescriptionModalOpen, setPrescriptionModalOpen] = useState(false);
  const [inpatientModalOpen, setInpatientModalOpen] = useState(false);
  const [prescriptionId, setPrescriptionId] = useState("");
  const [patientId, setPatientId] = useState("");
  const [viewPrescriptionsModalOpen, setViewPrescriptionsModalOpen] = useState(false);

  const columns = [
    { 
      key: "billingId", 
      header: "Billing ID",
      render: (val) => val || "N/A"
    },
    { 
      key: "patient", 
      header: "Patient",
      render: (val) => val?.name || "N/A"
    },
    { 
      key: "totalAmount", 
      header: "Total Amount",
      render: (val) => val ? `$${val.toFixed(2)}` : "$0.00"
    },
    { 
      key: "status", 
      header: "Status",
      render: (val) => val || "PENDING"
    },
  ];

  const prescriptionColumns = [
    { 
      key: "prescriptionId", 
      header: "Prescription ID"
    },
    { 
      key: "patient", 
      header: "Patient",
      render: (val) => val?.name || "N/A"
    },
    { 
      key: "doctor", 
      header: "Doctor",
      render: (val) => val?.name || "N/A"
    },
    { 
      key: "dateIssued", 
      header: "Date Issued"
    },
  ];

  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiGet("/api/staff/billing/all");
      setData(res || []);
    } catch (e) {
      setError(e?.message || "Failed to load bills");
    } finally {
      setLoading(false);
    }
  };

  const loadPrescriptions = async () => {
    try {
      const res = await apiGet("/api/staff/prescriptions/all");
      setPrescriptions(res || []);
    } catch (e) {
      console.error("Failed to load prescriptions:", e);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleGeneratePrescriptionBill = () => {
    setPrescriptionId("");
    setPrescriptionModalOpen(true);
  };

  const handleGenerateInpatientBill = () => {
    setPatientId("");
    setInpatientModalOpen(true);
  };

  const handleViewPrescriptions = () => {
    loadPrescriptions();
    setViewPrescriptionsModalOpen(true);
  };

  const handlePrescriptionBillSubmit = async (e) => {
    e.preventDefault();
    try {
      await apiPost(`/api/staff/billing/generateforprescription/${prescriptionId}`);
      setPrescriptionModalOpen(false);
      load();
    } catch (e) {
      alert(e?.message || "Failed to generate bill");
    }
  };

  const handleInpatientBillSubmit = async (e) => {
    e.preventDefault();
    try {
      await apiPost(`/api/staff/billing/generateforinpatient/${patientId}`);
      setInpatientModalOpen(false);
      load();
    } catch (e) {
      alert(e?.message || "Failed to generate bill");
    }
  };

  return (
    <Card
      title="Manage Billing"
      actions={
        <div className="flex gap-2">
          <button
            onClick={handleViewPrescriptions}
            className="px-3 py-1 bg-purple-600 text-white rounded hover:bg-purple-700"
          >
            View Prescriptions
          </button>
          <button
            onClick={handleGeneratePrescriptionBill}
            className="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700"
          >
            Generate Prescription Bill
          </button>
          <button
            onClick={handleGenerateInpatientBill}
            className="px-3 py-1 bg-green-600 text-white rounded hover:bg-green-700"
          >
            Generate Inpatient Bill
          </button>
        </div>
      }
    >
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}

      <Table columns={columns} data={data} />

      <Modal
        title="Generate Prescription Bill"
        open={prescriptionModalOpen}
        onClose={() => setPrescriptionModalOpen(false)}
        footer={
          <>
            <button
              onClick={() => setPrescriptionModalOpen(false)}
              className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700"
            >
              Cancel
            </button>
            <button
              onClick={handlePrescriptionBillSubmit}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              Generate
            </button>
          </>
        }
      >
        <form onSubmit={handlePrescriptionBillSubmit} className="space-y-3">
          <div>
            <label className="block text-sm mb-1">Prescription ID</label>
            <input
              type="number"
              value={prescriptionId}
              onChange={(e) => setPrescriptionId(e.target.value)}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
              min="1"
            />
          </div>
        </form>
      </Modal>

      <Modal
        title="Generate Inpatient Bill"
        open={inpatientModalOpen}
        onClose={() => setInpatientModalOpen(false)}
        footer={
          <>
            <button
              onClick={() => setInpatientModalOpen(false)}
              className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700"
            >
              Cancel
            </button>
            <button
              onClick={handleInpatientBillSubmit}
              className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
            >
              Generate
            </button>
          </>
        }
      >
        <form onSubmit={handleInpatientBillSubmit} className="space-y-3">
          <div>
            <label className="block text-sm mb-1">Patient ID</label>
            <input
              type="number"
              value={patientId}
              onChange={(e) => setPatientId(e.target.value)}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
              min="1"
            />
          </div>
        </form>
      </Modal>

      <Modal
        title="View Prescriptions"
        open={viewPrescriptionsModalOpen}
        onClose={() => setViewPrescriptionsModalOpen(false)}
        footer={
          <button
            onClick={() => setViewPrescriptionsModalOpen(false)}
            className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700"
          >
            Close
          </button>
        }
      >
        <Table columns={prescriptionColumns} data={prescriptions} />
      </Modal>
    </Card>
  );
}
