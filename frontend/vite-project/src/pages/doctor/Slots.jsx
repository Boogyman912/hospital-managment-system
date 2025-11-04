import React, { useState, useEffect } from "react";
import Card from "../../components/ui/Card.jsx";
import { apiDelete, apiPost, apiGet } from "../../api.js";

export default function Slots() {
  const [slots, setSlots] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [form, setForm] = useState({
    date: "",
    timeSlots: "",
    isOnline: true,
  });

  useEffect(() => {
    loadSlots();
  }, []);

  const loadSlots = async () => {
    try {
      setLoading(true);
      setError("");
      const data = await apiGet("/api/doctor/slots");

      // Ensure data is an array
      if (Array.isArray(data)) {
        setSlots(data);
      } else if (data && Array.isArray(data.slots)) {
        setSlots(data.slots);
      } else if (data && Array.isArray(data.data)) {
        setSlots(data.data);
      } else {
        setSlots([]);
      }
    } catch {
      // rollback: keep current slots and show friendly error
      setError("Failed to load slots. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  const createSlot = async (e) => {
    e.preventDefault();

    // Validate form
    if (!form.date || !form.timeSlots) {
      setError("Please fill in all required fields");
      return;
    }

    // Validate date is not in the past
    const selectedDate = new Date(form.date);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (selectedDate < today) {
      setError("Cannot create slots for past dates");
      return;
    }

    try {
      setLoading(true);
      setError("");
      setSuccess("");

      const timeSlotsArray = form.timeSlots
        .split(",")
        .map((s) => s.trim())
        .filter(Boolean);

      if (timeSlotsArray.length === 0) {
        setError("Please enter at least one time slot");
        return;
      }

      const res = await apiPost("/api/doctor/create-slots", {
        date: form.date,
        timeSlots: timeSlotsArray,
        isOnline: !!form.isOnline,
      });

      setSuccess(
        `Created ${res?.count ?? timeSlotsArray.length} slots successfully`
      );
      setForm({ date: "", timeSlots: "", isOnline: true });
      loadSlots(); // Reload slots from backend
    } catch (err) {
      setError(err?.message || "Failed to create slots");
    } finally {
      setLoading(false);
    }
  };

  const deleteSlot = async (id) => {
    if (!id) {
      setError("Invalid slot ID");
      return;
    }

    if (!window.confirm("Are you sure you want to delete this slot?")) {
      return;
    }

    try {
      setLoading(true);
      setError("");
      setSuccess("");

      await apiDelete(`/api/doctor/delete-slot/${id}`);
      setSuccess("Slot deleted successfully");
      loadSlots(); // Reload slots from backend
    } catch (err) {
      setError(err?.message || "Failed to delete slot");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Error and Success Messages */}
      {error && (
        <div className="bg-red-800 border border-red-600 text-red-200 px-4 py-3 rounded">
          {error}
        </div>
      )}
      {success && (
        <div className="bg-green-800 border border-green-600 text-green-200 px-4 py-3 rounded">
          {success}
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Card title="Create Slots">
          <form onSubmit={createSlot} className="space-y-3">
            <input
              className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
              type="date"
              value={form.date}
              onChange={(e) => setForm({ ...form, date: e.target.value })}
              min={new Date().toISOString().split("T")[0]}
              required
            />
            <input
              className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
              placeholder="Time slots comma separated e.g. 10:00,10:30"
              value={form.timeSlots}
              onChange={(e) => setForm({ ...form, timeSlots: e.target.value })}
              required
            />
            <label className="flex items-center gap-2 text-sm text-gray-300">
              <input
                type="checkbox"
                checked={form.isOnline}
                onChange={(e) =>
                  setForm({ ...form, isOnline: e.target.checked })
                }
              />{" "}
              Online
            </label>
            <button
              type="submit"
              disabled={loading}
              className="bg-blue-600 hover:bg-blue-700 disabled:opacity-50 px-4 py-2 rounded"
            >
              {loading ? "Creating..." : "Create Slots"}
            </button>
          </form>
        </Card>
        <Card title="Available Slots">
          {loading && (
            <div className="text-sm text-gray-400 mb-2">Loading slots...</div>
          )}
          <div className="space-y-2">
            {!Array.isArray(slots) || slots.length === 0 ? (
              <div className="text-center text-gray-400 py-4">
                No slots available. Create your first slot to get started.
              </div>
            ) : (
              slots.map((s) => {
                const slotDate = s.date || s.startTime?.split("T")[0];
                const slotTime =
                  s.time || s.startTime?.split("T")[1]?.substring(0, 5);
                const slotMode = s.mode || (s.isOnline ? "Online" : "Offline");
                const slotId = s.slotId || s.id;

                return (
                  <div
                    key={slotId}
                    className="flex items-center justify-between bg-gray-800 p-3 rounded border border-gray-700 hover:bg-gray-750 transition-colors"
                  >
                    <div className="flex-1">
                      <div className="font-medium text-white">
                        {slotDate} · {slotTime}
                      </div>
                      <div className="text-sm text-gray-400">{slotMode}</div>
                      {s.status && (
                        <div className="text-xs text-blue-400 mt-1">
                          Status: {s.status}
                        </div>
                      )}
                    </div>
                    <button
                      onClick={() => deleteSlot(slotId)}
                      disabled={loading}
                      className="px-3 py-1 bg-red-700 hover:bg-red-800 disabled:opacity-50 rounded text-sm transition-colors"
                    >
                      Delete
                    </button>
                  </div>
                );
              })
            )}
          </div>
        </Card>
      </div>
    </div>
  );
}
