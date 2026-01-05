import { useState } from "react";
import api from "../api/axios";

type Props = {
  onClose: () => void;
  onAddSuccess: () => void;
};

export default function AddDeviceModal({ onClose, onAddSuccess }: Props) {
  const [deviceName, setDeviceName] = useState("");
  const [gpio, setGpio] = useState("");
  const [gardenId, setGardenId] = useState("");

  const [loading, setLoading] = useState(false);

  const submit = async () => {
    if (!deviceName || !gpio || !gardenId) {
      alert("Vui lòng nhập đủ thông tin");
      return;
    }

    setLoading(true);
    try {
      await api.post("/api/devices", {
        deviceName,
        gpio,
        gardenId,
      });
      onAddSuccess();
      onClose();
    } catch (err: any) {
      console.error(err.response?.data || err);
      alert(
        "Thêm thiết bị thất bại: " +
          (err.response?.data?.message || err.message)
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div className="bg-white text-black rounded-xl p-6 w-96 space-y-4">
        <h3 className="text-lg font-semibold">➕ Thêm thiết bị mới</h3>

        <div>
          <label className="block mb-1">Tên thiết bị</label>
          <input
            type="text"
            className="w-full border rounded p-2"
            value={deviceName}
            onChange={(e) => setDeviceName(e.target.value)}
          />
        </div>

        <div>
          <label className="block mb-1">GPIO</label>
          <input
            type="number"
            className="w-full border rounded p-2"
            value={gpio}
            onChange={(e) => setGpio(e.target.value)}
          />
        </div>

        <div>
          <label className="block mb-1">Garden ID</label>
          <input
            type="number"
            className="w-full border rounded p-2"
            value={gardenId}
            onChange={(e) => setGardenId(e.target.value)}
          />
        </div>

        <div className="flex justify-end gap-3">
          <button onClick={onClose} disabled={loading}>
            Hủy
          </button>
          <button
            onClick={submit}
            className="bg-green-600 text-white px-4 py-1 rounded"
            disabled={loading}
          >
            {loading ? "Đang thêm..." : "Lưu"}
          </button>
        </div>
      </div>
    </div>
  );
}
