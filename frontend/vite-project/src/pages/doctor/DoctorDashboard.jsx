import React from "react";
import Card from "../../components/ui/Card.jsx";

export default function DoctorDashboard() {
  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
      <Card title="Today's Appointments">
        <div className="text-3xl font-bold">8</div>
      </Card>
      <Card title="Pending Prescriptions">
        <div className="text-3xl font-bold">3</div>
      </Card>
      <Card title="New Feedbacks">
        <div className="text-3xl font-bold">2</div>
      </Card>
    </div>
  );
}
