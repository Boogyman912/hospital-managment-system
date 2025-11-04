import { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import { apiDelete, apiGet } from "../../api.js";

export default function Feedbacks() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const columns = [
    { key: "patient", header: "Patient" },
    { key: "doctor", header: "Doctor" },
    { key: "rating", header: "Rating" },
    { key: "comment", header: "Comment" },
    { key: "date", header: "Date" },
  ];
  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiGet("/api/admin/feedbacks");
      setData(res || []);
    } catch (e) {
      setError(e?.message || "Failed to load feedbacks");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  return (
    <Card title="Feedbacks">
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}
      <Table
        columns={columns}
        data={data}
        renderActions={(row) => (
          <button
            onClick={async () => {
              try {
                await apiDelete(`/api/admin/delete/feedback/${row.feedbackId}`);
                load();
              } catch (e) {
                alert(e?.message || "Delete failed");
              }
            }}
            className="px-2 py-1 bg-red-700 rounded"
          >
            Delete
          </button>
        )}
      />
    </Card>
  );
}
