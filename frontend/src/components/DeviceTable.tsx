import { useEffect, useState } from "react";
import api from "../api/axios";
import { Device } from "../data/devices";
import ToggleSwitch from "./ToggleSwitch";
import SchedulerModal from "./SchedulerModal";
import AddDeviceModal from "./AddDeviceModal";
import { TrashIcon, PlusIcon } from "@heroicons/react/24/outline";

export default function DeviceTable() {
  const [devices, setDevices] = useState<Device[]>([]);
  const [selectedDeviceId, setSelectedDeviceId] = useState<number | null>(null);
  const [showAddModal, setShowAddModal] = useState(false);
  const [loading, setLoading] = useState(false);

  const loadDevices = async () => {
    setLoading(true);
    try {
      const res = await api.get<Device[]>("/api/devices");
      setDevices(res.data);
    } catch (err) {
      console.error(err);
      alert("Không thể tải thiết bị!");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDevices();
  }, []);

  const toggleDevice = async (d: Device) => {
    try {
      await api.get(`/api/devices/${d.id}/toggle`);
      setDevices((prev) =>
        prev.map((x) =>
          x.id === d.id
            ? { ...x, status: x.status === "ON" ? "OFF" : "ON" }
            : x
        )
      );
    } catch (err: any) {
      console.error(err.response?.data || err);
      alert("Toggle thiết bị thất bại: " + (err.response?.data?.message || err.message));
    }
  };

  const removeDevice = async (id: number) => {
    if (!confirm("Bạn có chắc muốn xóa thiết bị này không?")) return;
    try {
      await api.delete(`/api/devices/${id}`);
      await loadDevices();
    } catch (err: any) {
      console.error(err.response?.data || err);
      alert("Xóa thiết bị thất bại: " + (err.response?.data?.message || err.message));
    }
  };

  return (
    <div className="mb-10">
      {/* Header với nút thêm thiết bị */}
      <div className="flex justify-between items-center mb-3">
        <h2 className="text-xl font-semibold">🔌 Thiết bị</h2>
        <button
          onClick={() => setShowAddModal(true)}
          className="flex items-center gap-1 bg-blue-600 text-white px-4 py-1 rounded hover:bg-blue-700 transition"
        >
          <PlusIcon className="w-5 h-5" />
          Thêm thiết bị mới
        </button>
      </div>

      {/* Bảng thiết bị */}
      <div className="bg-white text-black rounded-xl overflow-hidden shadow-lg">
        <table className="w-full text-center">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-3">ID</th>
              <th>Tên thiết bị</th>
              <th>Trạng thái</th>
              <th>Toggle</th>
              <th>Lập lịch</th>
              <th>Xóa</th>
            </tr>
          </thead>
          <tbody>
            {devices.map((d) => (
              <tr key={d.id} className="border-t">
                <td className="p-3">{d.id}</td>
                <td>{d.deviceName}</td>
                <td
                  className={
                    d.status === "ON"
                      ? "text-green-600 font-semibold"
                      : "text-red-600 font-semibold"
                  }
                >
                  {d.status}
                </td>
                <td className="flex justify-center py-2">
                  <ToggleSwitch
                    checked={d.status === "ON"}
                    onChange={() => toggleDevice(d)}
                  />
                </td>
                <td>
                  <button
                    onClick={() => setSelectedDeviceId(d.id)}
                    className="text-2xl font-bold text-blue-600"
                  >
                    ＋
                  </button>
                </td>
                <td className="flex justify-center py-2">
                  <button
                    onClick={() => removeDevice(d.id)}
                    className="text-red-600 hover:text-red-800"
                    title="Xóa thiết bị"
                  >
                    <TrashIcon className="w-6 h-6" />
                  </button>
                </td>
              </tr>
            ))}
            {devices.length === 0 && (
              <tr>
                <td colSpan={6} className="py-6 opacity-60">
                  Chưa có thiết bị nào
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Modal lập lịch */}
      {selectedDeviceId && (
        <SchedulerModal
          deviceId={selectedDeviceId}
          onClose={() => setSelectedDeviceId(null)}
        />
      )}

      {/* Modal thêm thiết bị */}
      {showAddModal && (
        <AddDeviceModal
          onClose={() => setShowAddModal(false)}
          onAddSuccess={loadDevices}
        />
      )}
    </div>
  );
}
