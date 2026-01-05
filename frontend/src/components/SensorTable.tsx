import { useEffect, useState } from "react";
import api from "../api/axios";
import { SensorData } from "../data/sensorDatas";
import { ArrowPathIcon } from "@heroicons/react/24/outline";

const sensorMap: Record<string, string> = {
  TEMPERATURE: "Nhiệt độ",
  HUMIDITY: "Độ ẩm",
  LIGHT: "Ánh sáng",
  SOIL_MOISTURE: "Độ ẩm đất",
};

export default function SensorTable() {
  const [data, setData] = useState<SensorData[]>([]);
  const [loading, setLoading] = useState(false);

  const loadData = async () => {
    setLoading(true);
    const res = await api.get<SensorData[]>("/api/sensor_datas");
    setData(res.data);
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, []);

  return (
    <section>
      {/* Header */}
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-xl font-semibold flex items-center gap-2">
          🌱 Vườn của bạn
        </h2>

        <button
          onClick={loadData}
          disabled={loading}
          className="p-2 rounded-full hover:bg-white/20 transition"
        >
          <ArrowPathIcon
            className={`w-6 h-6 ${loading ? "animate-spin" : ""}`}
          />
        </button>
      </div>

      {/* Table */}
      <div className="bg-white text-black rounded-2xl overflow-hidden shadow-lg">
        <table className="w-full text-center">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-4">Tên vườn</th>
              <th>Loại thông tin</th>
              <th>Giá trị</th>
              <th>Lần cập nhật cuối</th>
            </tr>
          </thead>
          <tbody>
            {data.map((s) => (
              <tr key={s.id} className="border-t">
                <td className="p-4">{s.gardenName}</td>
                <td>{sensorMap[s.sensorType]}</td>
                <td>{s.value}</td>
                <td>{new Date(s.updatedAt).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
