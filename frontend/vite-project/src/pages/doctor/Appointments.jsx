import { useEffect, useState, useCallback, useMemo, useRef } from "react";
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
  const [inventory, setInventory] = useState([]);
  const [loadingInventory, setLoadingInventory] = useState(false);
  const [inventoryError, setInventoryError] = useState("");
  const [autocompleteStates, setAutocompleteStates] = useState({});

  // Refs for cleanup
  const isMountedRef = useRef(true);
  const timeoutRef = useRef(null);

  // Memoize today's date to avoid recalculating on every render
  const today = useMemo(() => new Date().toLocaleDateString("en-CA"), []);

  // Cleanup on unmount
  useEffect(() => {
    return () => {
      isMountedRef.current = false;
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, []);

  const handlePrescriptionSubmit = useCallback(async () => {
    setPrescError("");
    setPrescSuccess("");
    if (!prescFor.appointmentId) {
      setPrescError("Missing appointment details.");
      return;
    }
    // Validate patientId and doctorId
    if (!prescFor.patientId || !prescFor.doctorId) {
      setPrescError("Missing patient or doctor information.");
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
      timeoutRef.current = setTimeout(() => {
        if (isMountedRef.current) {
          setPrescOpen(false);
        }
      }, 800);
    } catch (e) {
      setPrescError(e?.message || "Failed to submit prescription");
    } finally {
      setPrescSubmitting(false);
    }
  }, [prescFor, medications, labTests, instructions]);
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
  const load = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiGet("/api/doctor/appointments");
      const list = Array.isArray(res)
        ? res
        : Array.isArray(res?.data)
        ? res.data
        : [];
      setData(list);
    } catch {
      setError("Failed to load appointments. Please try again later.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  // Load inventory when prescription modal opens
  const loadInventory = useCallback(async () => {
    setLoadingInventory(true);
    setInventoryError("");
    try {
      const res = await apiGet("/api/staff/inventory/all");
      setInventory(Array.isArray(res) ? res : []);
    } catch (e) {
      console.error("Failed to load inventory:", e);
      setInventoryError("Failed to load medication inventory. Some autocomplete features may not work.");
      setInventory([]);
    } finally {
      setLoadingInventory(false);
    }
  }, []);

  // Get unique item names from inventory
  const uniqueItemNames = useMemo(() => {
    const items = new Set();
    inventory.forEach((item) => {
      if (item.itemName) {
        items.add(item.itemName);
      }
    });
    return Array.from(items).sort();
  }, [inventory]);

  // Get brands for a specific item name
  const getBrandsForItem = useCallback(
    (itemName) => {
      if (!itemName) return [];
      const brands = new Set();
      inventory
        .filter((item) => item.itemName === itemName && item.brandName)
        .forEach((item) => {
          brands.add(item.brandName);
        });
      return Array.from(brands).sort();
    },
    [inventory]
  );

  // Filter items based on search query
  const filterItems = useCallback(
    (query, showAll = false) => {
      if (showAll || !query) {
        return uniqueItemNames.slice(0, 10); // Show first 10 when focused/empty
      }
      const lowerQuery = query.toLowerCase();
      return uniqueItemNames.filter((item) =>
        item.toLowerCase().includes(lowerQuery)
      );
    },
    [uniqueItemNames]
  );

  // Filter brands based on search query and item name
  const filterBrands = useCallback(
    (query, itemName, showAll = false) => {
      if (!itemName) return [];
      const brands = getBrandsForItem(itemName);
      if (showAll || !query) {
        return brands.slice(0, 10); // Show first 10 when focused/empty
      }
      const lowerQuery = query.toLowerCase();
      return brands.filter((brand) =>
        brand.toLowerCase().includes(lowerQuery)
      );
    },
    [getBrandsForItem]
  );

  // Remove medication with autocomplete cleanup
  const removeMedication = useCallback((idx) => {
    setMedications((arr) => arr.filter((_, i) => i !== idx));
    // Clean up all autocomplete states since indices will shift
    setAutocompleteStates({});
  }, []);

  // Remove lab test with autocomplete cleanup
  const removeLabTest = useCallback((idx) => {
    setLabTests((arr) => arr.filter((_, i) => i !== idx));
    // Clean up all autocomplete states since indices will shift
    setAutocompleteStates({});
  }, []);
  return (
    <Card title="Appointments">
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}
      <Table
        columns={columns}
        data={data}
        renderActions={(row) => {
          const isToday = row?.slot?.date === today;
          const isBooked = row?.appointmentStatus === "BOOKED";
          const hasPrescription = row?.prescription != null;
          return isBooked ? (
            <>
              <button
                onClick={() => {
                  setSelected(row);
                  setViewOpen(true);
                }}
                className="px-2 py-1 bg-gray-700 rounded"
                aria-label="View appointment details"
              >
                View
              </button>
              {isToday && !hasPrescription && (
                <button
                  onClick={async () => {
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
                    setAutocompleteStates({});
                    setPrescFor({
                      appointmentId: row.appointmentId,
                      patientId: row.patient?.patientId ?? null,
                      doctorId: row.doctor?.doctor_id ?? null,
                    });
                    setPrescOpen(true);
                    await loadInventory();
                  }}
                  className="px-2 py-1 bg-blue-700 rounded"
                  aria-label="Add prescription for this appointment"
                >
                  Add Prescription
                </button>
              )}
            </>
          ) : (
            <div className="text-gray-400">
              {hasPrescription ? "Prescription already added." : "Not available for prescription."}
            </div>
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
              aria-label="Cancel prescription form"
            >
              Cancel
            </button>
            <button
              onClick={handlePrescriptionSubmit}
              className="px-3 py-2 bg-blue-700 rounded disabled:opacity-50"
              disabled={prescSubmitting}
              aria-label="Save prescription"
            >
              {prescSubmitting ? "Saving..." : "Save"}
            </button>
          </>
        }
      >
        <div className="space-y-4">
          {prescError && (
            <div className="text-sm text-red-400" role="alert">{prescError}</div>
          )}
          {prescSuccess && (
            <div className="text-sm text-green-400" role="status">{prescSuccess}</div>
          )}
          {inventoryError && (
            <div className="text-sm text-yellow-400" role="alert">{inventoryError}</div>
          )}

          {/* Medications */}
          <div>
            <div className="font-semibold mb-2">Medications</div>
            {loadingInventory && (
              <div className="text-sm text-gray-400 mb-2">
                Loading inventory...
              </div>
            )}
            <div className="space-y-3">
              {medications.map((m, idx) => {
                const itemKey = `item-${idx}`;
                const brandKey = `brand-${idx}`;
                const itemState = autocompleteStates[itemKey] || {
                  show: false,
                  query: "",
                };
                const brandState = autocompleteStates[brandKey] || {
                  show: false,
                  query: "",
                };
                const filteredItems = filterItems(
                  itemState.query,
                  itemState.show && !itemState.query
                );
                const filteredBrands = filterBrands(
                  brandState.query,
                  m.itemName,
                  brandState.show && !brandState.query && m.itemName
                );

                return (
                  <div
                    key={idx}
                    className="grid md:grid-cols-5 gap-2 items-start"
                  >
                    {/* Item Name Autocomplete */}
                    <div className="relative">
                      <input
                        placeholder="Item Name"
                        className="bg-gray-900 border border-gray-700 rounded px-3 py-2 w-full"
                        value={m.itemName}
                        onChange={(e) => {
                          const newItemName = e.target.value;
                          const copy = medications.slice();
                          const oldItemName = m.itemName;
                          copy[idx] = { ...copy[idx], itemName: newItemName };
                          // Clear brand name when item changes
                          if (newItemName !== oldItemName) {
                            copy[idx].brandName = "";
                          }
                          setMedications(copy);
                          setAutocompleteStates((prev) => ({
                            ...prev,
                            [itemKey]: {
                              show: true,
                              query: newItemName,
                            },
                            [brandKey]: { show: false, query: "" },
                          }));
                        }}
                        onFocus={() => {
                          setAutocompleteStates((prev) => ({
                            ...prev,
                            [itemKey]: {
                              show: true,
                              query: m.itemName || "",
                            },
                          }));
                        }}
                        onBlur={() => {
                          // Delay to allow click on dropdown item
                          setTimeout(() => {
                            setAutocompleteStates((prev) => ({
                              ...prev,
                              [itemKey]: { ...prev[itemKey], show: false },
                            }));
                          }, 200);
                        }}
                        aria-label="Medication item name"
                        aria-autocomplete="list"
                        aria-expanded={itemState.show}
                        aria-controls={`item-autocomplete-${idx}`}
                      />
                      {itemState.show && filteredItems.length > 0 && (
                          <div 
                            id={`item-autocomplete-${idx}`}
                            role="listbox"
                            aria-label="Medication item suggestions"
                            className="absolute z-10 w-full mt-1 bg-gray-800 border border-gray-700 rounded shadow-lg max-h-48 overflow-y-auto"
                          >
                            {filteredItems.map((item, i) => (
                              <div
                                key={i}
                                role="option"
                                aria-selected={m.itemName === item}
                                className="px-3 py-2 hover:bg-gray-700 cursor-pointer text-sm"
                                onClick={() => {
                                  const copy = medications.slice();
                                  copy[idx] = {
                                    ...copy[idx],
                                    itemName: item,
                                    brandName: "",
                                  };
                                  setMedications(copy);
                                  setAutocompleteStates((prev) => ({
                                    ...prev,
                                    [itemKey]: { show: false, query: item },
                                    [brandKey]: { show: false, query: "" },
                                  }));
                                }}
                              >
                                {item}
                              </div>
                            ))}
                          </div>
                        )}
                    </div>

                    {/* Brand Name Autocomplete */}
                    <div className="relative">
                      <input
                        placeholder="Brand Name"
                        className="bg-gray-900 border border-gray-700 rounded px-3 py-2 w-full disabled:opacity-50 disabled:cursor-not-allowed"
                        value={m.brandName}
                        disabled={!m.itemName}
                        onChange={(e) => {
                          const copy = medications.slice();
                          copy[idx] = {
                            ...copy[idx],
                            brandName: e.target.value,
                          };
                          setMedications(copy);
                          setAutocompleteStates((prev) => ({
                            ...prev,
                            [brandKey]: {
                              show: true,
                              query: e.target.value,
                            },
                          }));
                        }}
                        onFocus={() => {
                          if (m.itemName) {
                            setAutocompleteStates((prev) => ({
                              ...prev,
                              [brandKey]: {
                                show: true,
                                query: m.brandName || "",
                              },
                            }));
                          }
                        }}
                        onBlur={() => {
                          setTimeout(() => {
                            setAutocompleteStates((prev) => ({
                              ...prev,
                              [brandKey]: { ...prev[brandKey], show: false },
                            }));
                          }, 200);
                        }}
                        aria-label="Medication brand name"
                        aria-autocomplete="list"
                        aria-expanded={brandState.show}
                        aria-controls={`brand-autocomplete-${idx}`}
                      />
                      {brandState.show &&
                        filteredBrands.length > 0 &&
                        m.itemName && (
                          <div 
                            id={`brand-autocomplete-${idx}`}
                            role="listbox"
                            aria-label="Medication brand suggestions"
                            className="absolute z-10 w-full mt-1 bg-gray-800 border border-gray-700 rounded shadow-lg max-h-48 overflow-y-auto"
                          >
                            {filteredBrands.map((brand, i) => (
                              <div
                                key={i}
                                role="option"
                                aria-selected={m.brandName === brand}
                                className="px-3 py-2 hover:bg-gray-700 cursor-pointer text-sm"
                                onClick={() => {
                                  const copy = medications.slice();
                                  copy[idx] = {
                                    ...copy[idx],
                                    brandName: brand,
                                  };
                                  setMedications(copy);
                                  setAutocompleteStates((prev) => ({
                                    ...prev,
                                    [brandKey]: { show: false, query: brand },
                                  }));
                                }}
                              >
                                {brand}
                              </div>
                            ))}
                          </div>
                        )}
                      {!m.itemName && (
                        <div className="absolute right-3 top-1/2 -translate-y-1/2 pointer-events-none">
                          <span className="text-xs text-gray-500">
                            Select item first
                          </span>
                        </div>
                      )}
                    </div>

                    <input
                      placeholder="Quantity"
                      className="bg-gray-900 border border-gray-700 rounded px-3 py-2"
                      value={m.quantity}
                      onChange={(e) => {
                        const value = e.target.value;
                        // Allow empty string or positive integers only
                        if (value === "" || /^[1-9]\d*$/.test(value)) {
                          const copy = medications.slice();
                          copy[idx] = { ...copy[idx], quantity: value };
                          setMedications(copy);
                        }
                      }}
                      type="text"
                      inputMode="numeric"
                      pattern="[1-9][0-9]*"
                      aria-label="Medication quantity"
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
                      aria-label="Medication instructions"
                    />
                    <div className="flex">
                      {medications.length > 1 && (
                        <button
                          type="button"
                          onClick={() => removeMedication(idx)}
                          className="px-2 py-2 bg-gray-700 rounded"
                          aria-label={`Remove medication ${idx + 1}`}
                        >
                          Remove
                        </button>
                      )}
                    </div>
                  </div>
                );
              })}
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
                aria-label="Add another medication"
              >
                Add Medication
              </button>
              {medications.length > 1 && (
                <button
                  onClick={() => removeMedication(medications.length - 1)}
                  className="px-2 py-1 bg-gray-700 rounded"
                  aria-label="Remove last medication"
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
                    aria-label="Lab test name"
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
                    aria-label="Lab test type"
                  />
                  <div className="flex">
                    {labTests.length > 1 && (
                      <button
                        type="button"
                        onClick={() => removeLabTest(idx)}
                        className="px-2 py-2 bg-gray-700 rounded"
                        aria-label={`Remove lab test ${idx + 1}`}
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
                aria-label="Add another lab test"
              >
                Add Lab Test
              </button>
              {labTests.length > 1 && (
                <button
                  onClick={() => removeLabTest(labTests.length - 1)}
                  className="px-2 py-1 bg-gray-700 rounded"
                  aria-label="Remove last lab test"
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
              aria-label="General prescription instructions"
            />
          </div>
        </div>
      </Modal>
    </Card>
  );
}
