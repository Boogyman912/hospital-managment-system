import React, { useState } from "react";
import Card from "../../components/ui/Card.jsx";
import { apiDelete, apiPost } from "../../api.js";

export default function Slots() {
  const [slots, setSlots] = useState([]);
  const [form, setForm] = useState({
    doctor_id: "",
    date: "",
    timeSlots: "",
    isOnline: true,
  });

  const createSlot = async (e) => {
    e.preventDefault();
    if (!form.date || !form.timeSlots || !form.doctor_id) return;
    try {
      const res = await apiPost("/api/doctor/create-slots", {
        doctor_id: Number(form.doctor_id),
        date: form.date,
        timeSlots: form.timeSlots
          .split(",")
          .map((s) => s.trim())
          .filter(Boolean),
        isOnline: !!form.isOnline,
      });
      alert(`Created ${res?.count ?? 0} slots`);
      setSlots((s) => [
        ...s,
        ...form.timeSlots.split(",").map((t, i) => ({
          id: Date.now() + i,
          date: form.date,
          time: t.trim(),
          mode: form.isOnline ? "Online" : "Offline",
        })),
      ]);
      setForm({ doctor_id: "", date: "", timeSlots: "", isOnline: true });
    } catch (e2) {
      alert(e2?.message || "Failed to create slots");
    }
  };

  const deleteSlot = async (id) => {
    try {
      await apiDelete(`/api/doctor/delete-slot/${id}`);
      setSlots((s) => s.filter((x) => x.id !== id));
    } catch (e) {
      alert(e?.message || "Failed to delete slot");
    }
  };

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <Card title="Create Slots">
        <form onSubmit={createSlot} className="space-y-3">
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Doctor ID"
            value={form.doctor_id}
            onChange={(e) => setForm({ ...form, doctor_id: e.target.value })}
          />
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            type="date"
            value={form.date}
            onChange={(e) => setForm({ ...form, date: e.target.value })}
          />
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Time slots comma separated e.g. 10:00,10:30"
            value={form.timeSlots}
            onChange={(e) => setForm({ ...form, timeSlots: e.target.value })}
          />
          <label className="flex items-center gap-2 text-sm text-gray-300">
            <input
              type="checkbox"
              checked={form.isOnline}
              onChange={(e) => setForm({ ...form, isOnline: e.target.checked })}
            />{" "}
            Online
          </label>
          <button className="bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded">
            Create Slot
          </button>
        </form>
      </Card>
      <Card title="Available Slots">
        <div className="space-y-2">
          {slots.map((s) => (
            <div
              key={s.id}
              className="flex items-center justify-between bg-gray-800 p-3 rounded border border-gray-700"
            >
              <div>
                <div className="font-medium">
                  {s.date} · {s.time}
                </div>
                <div className="text-sm text-gray-400">{s.mode}</div>
              </div>
              <button
                onClick={() => deleteSlot(s.id)}
                className="px-2 py-1 bg-red-700 rounded"
              >
                Delete
              </button>
            </div>
          ))}
        </div>
      </Card>
    </div>
  );
}
