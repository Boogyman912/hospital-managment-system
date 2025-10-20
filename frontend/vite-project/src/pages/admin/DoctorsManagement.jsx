import React, { useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import Modal from "../../components/ui/Modal.jsx";

export default function DoctorsManagement() {
  const [open, setOpen] = useState(false);
  const doctors = [
    {
      name: "Dr. Smith",
      specialization: "Cardiology",
      email: "smith@hms.com",
      active: true,
    },
    {
      name: "Dr. Jane",
      specialization: "Neurology",
      email: "jane@hms.com",
      active: false,
    },
  ];
  const columns = [
    { key: "name", header: "Name" },
    { key: "specialization", header: "Specialization" },
    { key: "email", header: "Email" },
    {
      key: "active",
      header: "Active Status",
      render: (v) => (v ? "Active" : "Inactive"),
    },
  ];

  return (
    <div className="space-y-6">
      <Card
        title="Doctors"
        actions={
          <button
            onClick={() => setOpen(true)}
            className="bg-blue-600 hover:bg-blue-700 px-3 py-2 rounded"
          >
            Register Doctor
          </button>
        }
      >
        <Table
          columns={columns}
          data={doctors}
          renderActions={() => (
            <>
              <button className="px-2 py-1 bg-gray-700 rounded">View</button>
              <button className="px-2 py-1 bg-gray-700 rounded">Edit</button>
              <button className="px-2 py-1 bg-red-700 rounded">Delete</button>
            </>
          )}
        />
      </Card>

      <Modal
        title="Register Doctor"
        open={open}
        onClose={() => setOpen(false)}
        footer={
          <>
            <button
              onClick={() => setOpen(false)}
              className="px-3 py-2 bg-gray-700 rounded"
            >
              Cancel
            </button>
            <button className="px-3 py-2 bg-blue-600 rounded">Save</button>
          </>
        }
      >
        <form className="space-y-3">
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Name"
          />
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Specialization"
          />
          <input
            className="w-full px-3 py-2 bg-gray-900 border border-gray-700 rounded"
            placeholder="Email"
          />
        </form>
      </Modal>
    </div>
  );
}
