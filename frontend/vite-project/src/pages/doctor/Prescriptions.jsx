import React, { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import { apiDelete, apiGet, apiPatch, apiPost } from "../../api.js";

export default function Prescriptions() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const [editId, setEditId] = useState(null);
  const [instructions, setInstructions] = useState("");
  const columns = [
    { key: "patient", header: "Patient" },
    { key: "date", header: "Date" },
    { key: "medicines", header: "Medicines" },
    { key: "tests", header: "Lab Tests" },
  ];
  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiGet("/api/doctor/prescriptions");
      setData(res || []);
    } catch (e) {
      setError(e?.message || "Failed to load prescriptions");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  return (
    <Card title="Prescriptions">
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}
      <Table
        columns={columns}
        data={data}
        renderActions={(row) => (
          <>
            <button
              onClick={async () => {
                const med = prompt("Enter medications comma separated");
                const tests = prompt("Enter lab tests comma separated");
                const instr = prompt("Enter instructions");
                if (med || tests || instr) {
                  try {
                    await apiPost(
                      `/api/doctor/prescription/${row.appointmentId}`,
                      {
                        medications: med
                          ? med.split(",").map((s) => s.trim())
                          : [],
                        labTests: tests
                          ? tests.split(",").map((s) => s.trim())
                          : [],
                        instructions: instr || "",
                      }
                    );
                    load();
                  } catch (e) {
                    alert(e?.message || "Create failed");
                  }
                }
              }}
              className="px-2 py-1 bg-blue-700 rounded"
            >
              Add
            </button>
            <button
              onClick={async () => {
                const instr = prompt(
                  "Update instructions",
                  row.instructions || ""
                );
                if (instr != null) {
                  try {
                    await apiPatch(
                      `/api/doctor/update/prescription/${row.prescriptionId}`,
                      { instructions: instr }
                    );
                    load();
                  } catch (e) {
                    alert(e?.message || "Update failed");
                  }
                }
              }}
              className="px-2 py-1 bg-gray-700 rounded"
            >
              Edit
            </button>
            <button
              onClick={async () => {
                if (confirm("Delete prescription?")) {
                  try {
                    await apiDelete(
                      `/api/doctor/delete/prescription/${row.prescriptionId}`
                    );
                    load();
                  } catch (e) {
                    alert(e?.message || "Delete failed");
                  }
                }
              }}
              className="px-2 py-1 bg-red-700 rounded"
            >
              Delete
            </button>
          </>
        )}
      />
    </Card>
  );
}
