import { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import { apiGet } from "../../api.js";

export default function Prescriptions() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  // Removed states: editId, instructions
  // Removed Table component and apiPost, apiPatch, apiDelete

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

      {/* New Modular Display */}
      <div className="space-y-4">
        {data.length === 0 && !loading && (
          <p className="text-gray-400 text-center py-4">
            No prescriptions found.
          </p>
        )}
        {data.map((presc) => (
          // Each prescription is its own module/card
          <div
            key={presc.prescriptionId}
            className="bg-gray-800 border border-gray-700 rounded-lg p-4"
          >
            {/* Header: Prescription ID and Date */}
            <div className="flex flex-col sm:flex-row justify-between sm:items-center mb-3 pb-3 border-b border-gray-700">
              <h3 className="font-semibold text-lg text-white">
                Prescription #{presc.prescriptionId}
              </h3>
              <span className="text-sm text-gray-400">
                Issued: {presc.dateIssued || "N/A"}
              </span>
            </div>

            {/* Patient Info */}
            <div className="mb-4">
              <div className="text-sm font-medium text-gray-400 mb-1">
                Patient
              </div>
              <div className="text-md text-gray-100">
                {presc.patient?.name || "Patient data not available"}
              </div>
              {presc.patient?.phoneNumber && (
                <div className="text-sm text-gray-400">
                  {presc.patient.phoneNumber}
                </div>
              )}
            </div>

            {/* Medications */}
            <div className="mb-4">
              <h4 className="font-semibold text-md text-white mb-2">
                Medications
              </h4>
              {presc.medications && presc.medications.length > 0 ? (
                <ul className="list-disc list-inside space-y-2 pl-2 text-sm text-gray-300">
                  {presc.medications.map((med, index) => (
                    <li key={index}>
                      <strong>{med.itemName || "N/A"}</strong> (
                      {med.brandName || "N/A"}) - Qty: {med.quantity || "N/A"}
                      <div className="pl-5 text-xs text-gray-400">
                        {med.medication_instructions || "No instructions"}
                      </div>
                    </li>
                  ))}
                </ul>
              ) : (
                <p className="text-sm text-gray-400">No medications listed.</p>
              )}
            </div>

            {/* Lab Tests */}
            <div className="mb-4">
              <h4 className="font-semibold text-md text-white mb-2">
                Lab Tests
              </h4>
              {presc.labTests && presc.labTests.length > 0 ? (
                <ul className="list-disc list-inside space-y-1 pl-2 text-sm text-gray-300">
                  {presc.labTests.map((test, index) => (
                    <li key={index}>
                      <strong>{test.testName || "N/A"}</strong> (
                      {test.testType || "N/A"})
                    </li>
                  ))}
                </ul>
              ) : (
                <p className="text-sm text-gray-400">No lab tests listed.</p>
              )}
            </div>

            {/* General Instructions */}
            <div>
              <h4 className="font-semibold text-md text-white mb-2">
                General Instructions
              </h4>
              <p className="text-sm text-gray-300 bg-gray-900 p-3 rounded">
                {presc.instructions || "No general instructions provided."}
              </p>
            </div>
          </div>
        ))}
      </div>
    </Card>
  );
}
