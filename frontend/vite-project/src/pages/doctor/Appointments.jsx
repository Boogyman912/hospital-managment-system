import React, { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import Modal from "../../components/ui/Modal.jsx";
import { apiPost } from "../../api.js";
import { apiGet } from "../../api.js";

export default function DoctorAppointments() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const [viewOpen, setViewOpen] = useState(false);
  const [selected, setSelected] = useState(null);
  const [prescOpen, setPrescOpen] = useState(false);
  const [prescFor, setPrescFor] = useState({
    appointmentId: null,
    patientId: null,
    doctorId: null,
  });
  const [medications, setMedications] = useState([
    { itemName: "", brandName: "", quantity: "", medication_instructions: "" },
  ]);
  const [labTests, setLabTests] = useState([{ testName: "", testType: "" }]);
  const [instructions, setInstructions] = useState("");
  const [prescSubmitting, setPrescSubmitting] = useState(false);
  const [prescError, setPrescError] = useState("");
  const [prescSuccess, setPrescSuccess] = useState("");

  const handlePrescriptionSubmit = async () => {
    setPrescError("");
    setPrescSuccess("");
    if (!prescFor.appointmentId) {
      setPrescError("Missing appointment details.");
      return;
    }
    setPrescSubmitting(true);
    try {
      const payload = {
        appointmentId: prescFor.appointmentId,
        patientId: prescFor.patientId,
        doctorId: prescFor.doctorId,
        medications: medications
          .map((m) => ({
            itemName: (m.itemName || "").trim(),
            brandName: (m.brandName || "").trim(),
            quantity: (m.quantity || "").trim(),
            medication_instructions: (m.medication_instructions || "").trim(),
          }))
          .filter(
            (m) =>
              m.itemName ||
              m.brandName ||
              m.quantity ||
              m.medication_instructions
          ),
        labTests: labTests
          .map((t) => ({
            testName: (t.testName || "").trim(),
            testType: (t.testType || "").trim(),
          }))
          .filter((t) => t.testName || t.testType),
        instructions: (instructions || "").trim(),
      };
      await apiPost(
        `/api/doctor/prescriptions/${prescFor.appointmentId}`,
        payload
      );
      setPrescSuccess("Prescription added successfully.");
      setTimeout(() => setPrescOpen(false), 800);
    } catch (e) {
      setPrescError(e?.message || "Failed to submit prescription");
    } finally {
      setPrescSubmitting(false);
    }
  };
  const columns = [
    {
      key: "slot",
      header: "Time",
      render: (slot) => (slot?.time ? slot.time : "N/A"),
    },
    {
      key: "patient",
      header: "Patient",
      render: (patient) => (patient?.name ? patient.name : "N/A"),
    },
    { key: "appointmentStatus", header: "Status" },
  ];
  const load = async () => {
    setLoading(true);
    setError("");
    const prev = data;
    try {
      const res = await apiGet("/api/doctor/appointments");
      const list = Array.isArray(res)
        ? res
        : Array.isArray(res?.data)
        ? res.data
        : [];
      setData(list);
    } catch (e) {
      setData(prev);
      setError("Failed to load appointments. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);
  return (
    <Card title="Appointments">
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}
      <Table
        columns={columns}
        data={data}
        renderActions={(row) => {
          const today = new Date().toLocaleDateString("en-CA"); // YYYY-MM-DD in local time
          const isToday = row?.slot?.date === today;
          const isBooked = row?.appointmentStatus === "BOOKED";
          return isBooked ? (
            <>
              <button
                onClick={() => {
                  setSelected(row);
                  setViewOpen(true);
                }}
                className="px-2 py-1 bg-gray-700 rounded"
              >
                View
              </button>
              {isToday && (
                <button
                  onClick={() => {
                    setPrescError("");
                    setPrescSuccess("");
                    setMedications([
                      {
                        itemName: "",
                        brandName: "",
                        quantity: "",
                        medication_instructions: "",
                      },
                    ]);
                    setLabTests([{ testName: "", testType: "" }]);
                    setInstructions("");
                    setPrescFor({
                      appointmentId: row.appointmentId,
                      patientId: row.patient?.patientId ?? null,
                      doctorId: row.doctor?.doctor_id ?? null,
                    });
                    setPrescOpen(true);
                  }}
                  className="px-2 py-1 bg-blue-700 rounded"
                >
                  Add Prescription
                </button>
              )}
            </>
          ) : (
            <div className="text-gray-400">Prescription already added.</div>
          );
        }}
      />
      {/* View Modal */}
      <Modal
        title="Appointment Details"
        open={viewOpen}
        onClose={() => setViewOpen(false)}
      >
        {selected ? (
          <div className="space-y-2 text-sm">
            <div>
              <span className="text-gray-400">Appointment ID:</span>{" "}
              <span className="text-gray-100">{selected.appointmentId}</span>
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div>
                <div className="text-gray-400">Date</div>
                <div className="text-gray-100">
                  {selected.slot?.date || "-"}
                </div>
              </div>
              <div>
                <div className="text-gray-400">Time</div>
                <div className="text-gray-100">
                  {selected.slot?.time || "-"}
                </div>
              </div>
            </div>
            <div>
              <div className="text-gray-400">Doctor</div>
              <div className="text-gray-100">
                {selected.doctor?.name || "-"}
                {selected.doctor?.specialization
                  ? ` (${selected.doctor.specialization})`
                  : ""}
              </div>
            </div>
            <div>
              <div className="text-gray-400">Patient</div>
              <div className="text-gray-100">
                {selected.patient?.name || "-"}
              </div>
              <div className="text-gray-400">
                {selected.patient?.phoneNumber || ""}
                {selected.patient?.email ? ` · ${selected.patient.email}` : ""}
              </div>
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div>
                <div className="text-gray-400">Status</div>
                <div className="text-gray-100">
                  {selected.appointmentStatus}
                </div>
              </div>
              <div>
                <div className="text-gray-400">Payment</div>
                <div className="text-gray-100">{selected.paymentStatus}</div>
              </div>
            </div>
            {selected.receipt?.pdfUrl && (
              <div>
                <a
                  className="text-blue-400 hover:underline"
                  href={selected.receipt.pdfUrl}
                  target="_blank"
                  rel="noreferrer"
                >
                  View Receipt PDF
                </a>
              </div>
            )}
          </div>
        ) : (
          <div className="text-gray-400">No appointment selected.</div>
        )}
      </Modal>

      {/* Add Prescription Modal */}
      <Modal
        title="Add Prescription"
        open={prescOpen}
        onClose={() => setPrescOpen(false)}
        footer={
          <>
            <button
              onClick={() => setPrescOpen(false)}
              className="px-3 py-2 bg-gray-700 rounded"
              disabled={prescSubmitting}
            >
              Cancel
            </button>
            <button
              onClick={handlePrescriptionSubmit}
              className="px-3 py-2 bg-blue-700 rounded disabled:opacity-50"
              disabled={prescSubmitting}
            >
              {prescSubmitting ? "Saving..." : "Save"}
            </button>
          </>
        }
      >
        <div className="space-y-4">
          {prescError && (
            <div className="text-sm text-red-400">{prescError}</div>
          )}
          {prescSuccess && (
            <div className="text-sm text-green-400">{prescSuccess}</div>
          )}

          {/* Medications */}
          <div>
            <div className="font-semibold mb-2">Medications</div>
            <div className="space-y-3">
              {medications.map((m, idx) => (
                <div
                  key={idx}
                  className="grid md:grid-cols-5 gap-2 items-start"
                >
                  <input
                    placeholder="Item Name"
                    className="bg-gray-900 border border-gray-700 rounded px-3 py-2"
                    value={m.itemName}
                    onChange={(e) => {
                      const copy = medications.slice();
                      copy[idx] = { ...copy[idx], itemName: e.target.value };
                      setMedications(copy);
                    }}
                  />
                  <input
                    placeholder="Brand Name"
                    className="bg-gray-900 border border-gray-700 rounded px-3 py-2"
                    value={m.brandName}
                    onChange={(e) => {
                      const copy = medications.slice();
                      copy[idx] = { ...copy[idx], brandName: e.target.value };
                      setMedications(copy);
                    }}
                  />
                  <input
                    placeholder="Quantity"
                    className="bg-gray-900 border border-gray-700 rounded px-3 py-2"
                    value={m.quantity}
                    onChange={(e) => {
                      const copy = medications.slice();
                      copy[idx] = { ...copy[idx], quantity: e.target.value };
                      setMedications(copy);
                    }}
                  />
                  <input
                    placeholder="Instructions"
                    className="bg-gray-900 border border-gray-700 rounded px-3 py-2"
                    value={m.medication_instructions}
                    onChange={(e) => {
                      const copy = medications.slice();
                      copy[idx] = {
                        ...copy[idx],
                        medication_instructions: e.target.value,
                      };
                      setMedications(copy);
                    }}
                  />
                  <div className="flex">
                    {medications.length > 1 && (
                      <button
                        type="button"
                        onClick={() =>
                          setMedications((arr) =>
                            arr.filter((_, i) => i !== idx)
                          )
                        }
                        className="px-2 py-2 bg-gray-700 rounded"
                      >
                        Remove
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
            <div className="mt-2 flex gap-2">
              <button
                onClick={() =>
                  setMedications((arr) => [
                    ...arr,
                    {
                      itemName: "",
                      brandName: "",
                      quantity: "",
                      medication_instructions: "",
                    },
                  ])
                }
                className="px-2 py-1 bg-gray-700 rounded"
              >
                Add Medication
              </button>
              {medications.length > 1 && (
                <button
                  onClick={() => setMedications((arr) => arr.slice(0, -1))}
                  className="px-2 py-1 bg-gray-700 rounded"
                >
                  Remove Last
                </button>
              )}
            </div>
          </div>

          {/* Lab Tests */}
          <div>
            <div className="font-semibold mb-2">Lab Tests</div>
            <div className="space-y-3">
              {labTests.map((t, idx) => (
                <div
                  key={idx}
                  className="grid md:grid-cols-3 gap-2 items-start"
                >
                  <input
                    placeholder="Test Name"
                    className="bg-gray-900 border border-gray-700 rounded px-3 py-2"
                    value={t.testName}
                    onChange={(e) => {
                      const copy = labTests.slice();
                      copy[idx] = { ...copy[idx], testName: e.target.value };
                      setLabTests(copy);
                    }}
                  />
                  <input
                    placeholder="Test Type"
                    className="bg-gray-900 border border-gray-700 rounded px-3 py-2"
                    value={t.testType}
                    onChange={(e) => {
                      const copy = labTests.slice();
                      copy[idx] = { ...copy[idx], testType: e.target.value };
                      setLabTests(copy);
                    }}
                  />
                  <div className="flex">
                    {labTests.length > 1 && (
                      <button
                        type="button"
                        onClick={() =>
                          setLabTests((arr) => arr.filter((_, i) => i !== idx))
                        }
                        className="px-2 py-2 bg-gray-700 rounded"
                      >
                        Remove
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
            <div className="mt-2 flex gap-2">
              <button
                onClick={() =>
                  setLabTests((arr) => [...arr, { testName: "", testType: "" }])
                }
                className="px-2 py-1 bg-gray-700 rounded"
              >
                Add Lab Test
              </button>
              {labTests.length > 1 && (
                <button
                  onClick={() => setLabTests((arr) => arr.slice(0, -1))}
                  className="px-2 py-1 bg-gray-700 rounded"
                >
                  Remove Last
                </button>
              )}
            </div>
          </div>

          {/* General Instructions */}
          <div>
            <div className="font-semibold mb-2">General Instructions</div>
            <textarea
              className="w-full bg-gray-900 border border-gray-700 rounded px-3 py-2 min-h-[90px]"
              value={instructions}
              onChange={(e) => setInstructions(e.target.value)}
              placeholder="Enter any additional instructions for the patient"
            />
          </div>
        </div>
      </Modal>
    </Card>
  );
}
