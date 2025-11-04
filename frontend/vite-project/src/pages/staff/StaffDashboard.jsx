import React from "react";
import Card from "../../components/ui/Card.jsx";

export default function StaffDashboard() {
  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
      <Card title="Assigned Rooms">
        <div className="text-3xl font-bold">4</div>
      </Card>
      <Card title="Today's Duties">
        <div className="text-3xl font-bold">2</div>
      </Card>
      <Card title="Pending Tasks">
        <div className="text-3xl font-bold">1</div>
      </Card>
    </div>
  );
}
