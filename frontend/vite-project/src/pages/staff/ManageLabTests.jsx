import { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import Modal from "../../components/ui/Modal.jsx";
import { apiGet, apiPost, apiDelete } from "../../api.js";

export default function ManageLabTests() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    testName: "",
    testType: "",
    testCost: 0,
  });

  const columns = [
    { key: "testName", header: "Test Name" },
    { key: "testType", header: "Test Type" },
    { 
      key: "testCost", 
      header: "Test Cost",
      render: (val) => val ? `$${val.toFixed(2)}` : "$0.00"
    },
  ];

  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiGet("/api/staff/labtest/");
      setData(res || []);
    } catch (e) {
      setError(e?.message || "Failed to load lab tests");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleCreate = () => {
    setFormData({ testName: "", testType: "", testCost: 0 });
    setModalOpen(true);
  };

  const handleDelete = async (test) => {
    if (!confirm(`Delete test ${test.testName}?`)) return;
    try {
      await apiDelete(`/api/staff/labtest/delete/${test.testId}`);
      load();
    } catch (e) {
      alert(e?.message || "Failed to delete test");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await apiPost("/api/staff/labtest/create", formData);
      setModalOpen(false);
      load();
    } catch (e) {
      alert(e?.message || "Failed to create test");
    }
  };

  return (
    <Card
      title="Manage Lab Tests"
      actions={
        <button
          onClick={handleCreate}
          className="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          Create Test
        </button>
      }
    >
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}

      <Table
        columns={columns}
        data={data}
        renderActions={(row) => (
          <button
            onClick={() => handleDelete(row)}
            className="px-2 py-1 bg-red-600 text-white text-xs rounded hover:bg-red-700"
          >
            Delete
          </button>
        )}
      />

      <Modal
        title="Create Lab Test"
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
              Create
            </button>
          </>
        }
      >
        <form onSubmit={handleSubmit} className="space-y-3">
          <div>
            <label className="block text-sm mb-1">Test Name</label>
            <input
              type="text"
              value={formData.testName}
              onChange={(e) => setFormData({ ...formData, testName: e.target.value })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
            />
          </div>
          <div>
            <label className="block text-sm mb-1">Test Type</label>
            <input
              type="text"
              value={formData.testType}
              onChange={(e) => setFormData({ ...formData, testType: e.target.value })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
            />
          </div>
          <div>
            <label className="block text-sm mb-1">Test Cost</label>
            <input
              type="number"
              step="0.01"
              value={formData.testCost}
              onChange={(e) => setFormData({ ...formData, testCost: parseFloat(e.target.value) || 0 })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
              min="0"
            />
          </div>
        </form>
      </Modal>
    </Card>
  );
}
