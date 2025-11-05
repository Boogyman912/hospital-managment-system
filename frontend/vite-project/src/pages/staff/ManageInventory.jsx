import { useEffect, useState } from "react";
import Card from "../../components/ui/Card.jsx";
import Table from "../../components/ui/Table.jsx";
import Modal from "../../components/ui/Modal.jsx";
import { apiGet, apiPost, apiPatch, apiDelete } from "../../api.js";

export default function ManageInventory() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [data, setData] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [restockModalOpen, setRestockModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const [restockingItem, setRestockingItem] = useState(null);
  const [formData, setFormData] = useState({
    itemName: "",
    brandName: "",
    quantity: 0,
    unitPrice: 0,
  });
  const [restockQuantity, setRestockQuantity] = useState(0);

  const columns = [
    { key: "itemName", header: "Item Name" },
    { key: "brandName", header: "Brand Name" },
    { 
      key: "quantity", 
      header: "Quantity",
      render: (val) => val || 0
    },
    { 
      key: "unitPrice", 
      header: "Unit Price",
      render: (val) => val ? `$${val.toFixed(2)}` : "$0.00"
    },
  ];

  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await apiGet("/api/staff/inventory/all");
      setData(res || []);
    } catch (e) {
      setError(e?.message || "Failed to load inventory");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleCreate = () => {
    setEditingItem(null);
    setFormData({ itemName: "", brandName: "", quantity: 0, unitPrice: 0 });
    setModalOpen(true);
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    setFormData({
      itemName: item.itemName,
      brandName: item.brandName,
      quantity: item.quantity,
      unitPrice: item.unitPrice,
    });
    setModalOpen(true);
  };

  const handleDelete = async (item) => {
    if (!confirm(`Delete ${item.itemName} (${item.brandName})?`)) return;
    try {
      await apiDelete(`/api/staff/inventory/delete/${item.itemId}`);
      load();
    } catch (e) {
      alert(e?.message || "Failed to delete item");
    }
  };

  const handleRestock = (item) => {
    setRestockingItem(item);
    setRestockQuantity(0);
    setRestockModalOpen(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingItem) {
        await apiPatch(`/api/staff/inventory/updateItem/${editingItem.itemId}`, formData);
      } else {
        await apiPost("/api/staff/inventory/create", formData);
      }
      setModalOpen(false);
      load();
    } catch (e) {
      alert(e?.message || "Failed to save item");
    }
  };

  const handleRestockSubmit = async (e) => {
    e.preventDefault();
    try {
      await apiPatch(`/api/staff/inventory/increaseQuantity/${restockingItem.itemId}`, restockQuantity);
      setRestockModalOpen(false);
      load();
    } catch (e) {
      alert(e?.message || "Failed to restock item");
    }
  };

  return (
    <Card
      title="Manage Inventory"
      actions={
        <button
          onClick={handleCreate}
          className="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          Create Item
        </button>
      }
    >
      {loading && <div className="text-sm text-gray-400 mb-2">Loading...</div>}
      {error && <div className="text-sm text-red-400 mb-2">{error}</div>}

      <Table
        columns={columns}
        data={data}
        renderActions={(row) => (
          <>
            <button
              onClick={() => handleEdit(row)}
              className="px-2 py-1 bg-yellow-600 text-white text-xs rounded hover:bg-yellow-700"
            >
              Edit
            </button>
            <button
              onClick={() => handleRestock(row)}
              className="px-2 py-1 bg-green-600 text-white text-xs rounded hover:bg-green-700"
            >
              Restock
            </button>
            <button
              onClick={() => handleDelete(row)}
              className="px-2 py-1 bg-red-600 text-white text-xs rounded hover:bg-red-700"
            >
              Delete
            </button>
          </>
        )}
      />

      <Modal
        title={editingItem ? "Edit Item" : "Create Item"}
        open={modalOpen}
        onClose={() => setModalOpen(false)}
        footer={
          <>
            <button
              onClick={() => setModalOpen(false)}
              className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700"
            >
              Cancel
            </button>
            <button
              onClick={handleSubmit}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              {editingItem ? "Update" : "Create"}
            </button>
          </>
        }
      >
        <form onSubmit={handleSubmit} className="space-y-3">
          <div>
            <label className="block text-sm mb-1">Item Name</label>
            <input
              type="text"
              value={formData.itemName}
              onChange={(e) => setFormData({ ...formData, itemName: e.target.value })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
            />
          </div>
          <div>
            <label className="block text-sm mb-1">Brand Name</label>
            <input
              type="text"
              value={formData.brandName}
              onChange={(e) => setFormData({ ...formData, brandName: e.target.value })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
            />
          </div>
          <div>
            <label className="block text-sm mb-1">Quantity</label>
            <input
              type="number"
              value={formData.quantity}
              onChange={(e) => setFormData({ ...formData, quantity: parseInt(e.target.value) || 0 })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
              min="0"
            />
          </div>
          <div>
            <label className="block text-sm mb-1">Unit Price</label>
            <input
              type="number"
              step="0.01"
              value={formData.unitPrice}
              onChange={(e) => setFormData({ ...formData, unitPrice: parseFloat(e.target.value) || 0 })}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
              min="0"
            />
          </div>
        </form>
      </Modal>

      <Modal
        title="Restock Item"
        open={restockModalOpen}
        onClose={() => setRestockModalOpen(false)}
        footer={
          <>
            <button
              onClick={() => setRestockModalOpen(false)}
              className="px-4 py-2 bg-gray-600 text-white rounded hover:bg-gray-700"
            >
              Cancel
            </button>
            <button
              onClick={handleRestockSubmit}
              className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
            >
              Restock
            </button>
          </>
        }
      >
        <form onSubmit={handleRestockSubmit} className="space-y-3">
          <p className="text-sm">
            {restockingItem && `Restocking: ${restockingItem.itemName} (${restockingItem.brandName})`}
          </p>
          <p className="text-sm">
            {restockingItem && `Current Quantity: ${restockingItem.quantity}`}
          </p>
          <div>
            <label className="block text-sm mb-1">Quantity to Add</label>
            <input
              type="number"
              value={restockQuantity}
              onChange={(e) => setRestockQuantity(parseInt(e.target.value) || 0)}
              className="w-full px-3 py-2 bg-gray-700 border border-gray-600 rounded text-gray-200"
              required
              min="1"
            />
          </div>
        </form>
      </Modal>
    </Card>
  );
}
