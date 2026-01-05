import { useEffect, useMemo, useState } from "react";
import api from "../api/axios";
import { DeviceLog } from "../data/deviceLogs";
import { ArrowPathIcon } from "@heroicons/react/24/outline";

type SortDirection = "asc" | "desc";

export default function DeviceLogTable() {
  const [logs, setLogs] = useState<DeviceLog[]>([]);
  const [loading, setLoading] = useState(false);
  const [sortDirection, setSortDirection] = useState<SortDirection>("desc");
  const [currentPage, setCurrentPage] = useState(1);
  const rowsPerPage = 5;

  const load = async () => {
    setLoading(true);
    const res = await api.get<DeviceLog[]>("/api/device_logs");
    setLogs(res.data);
    setLoading(false);
    setCurrentPage(1); // reset page khi load mới
  };

  const changeSort = () => {
    setSortDirection((d) => (d === "asc" ? "desc" : "asc"));
    setCurrentPage(1); // reset page khi đổi sort
  };

  const sortedLogs = useMemo(() => {
    return [...logs].sort((a, b) => {
      const ta = new Date(a.actionAt).getTime();
      const tb = new Date(b.actionAt).getTime();
      return sortDirection === "asc" ? ta - tb : tb - ta;
    });
  }, [logs, sortDirection]);

  // --- PHÂN TRANG ---
  const totalPages = Math.ceil(sortedLogs.length / rowsPerPage);
  const paginatedLogs = sortedLogs.slice(
    (currentPage - 1) * rowsPerPage,
    currentPage * rowsPerPage
  );

  const Arrow = () => (
    <span className="ml-1">{sortDirection === "asc" ? "↑" : "↓"}</span>
  );

  useEffect(() => {
    load();
  }, []);

  return (
    <section>
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-xl font-semibold flex items-center gap-2">
          ⚙️ Hoạt động thiết bị
        </h2>

        <button
          onClick={load}
          disabled={loading}
          className="p-2 rounded-full hover:bg-white/20 transition"
        >
          <ArrowPathIcon
            className={`w-6 h-6 ${loading ? "animate-spin" : ""}`}
          />
        </button>
      </div>

      <div className="bg-white text-black rounded-2xl overflow-hidden shadow-lg">
        <table className="w-full text-center">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-4">Thiết bị</th>
              <th>Trạng thái</th>
              <th
                className="cursor-pointer select-none"
                onClick={changeSort}
              >
                Thời điểm <Arrow />
              </th>
            </tr>
          </thead>
          <tbody>
            {paginatedLogs.map((l) => (
              <tr key={l.id} className="border-t">
                <td className="p-4">{l.deviceName}</td>
                <td
                  className={
                    l.status === "ON"
                      ? "text-green-600 font-semibold"
                      : "text-red-600 font-semibold"
                  }
                >
                  {l.status}
                </td>
                <td>{new Date(l.actionAt).toLocaleString()}</td>
              </tr>
            ))}
            {paginatedLogs.length === 0 && (
              <tr>
                <td colSpan={3} className="py-6 opacity-60">
                  Chưa có dữ liệu
                </td>
              </tr>
            )}
          </tbody>
        </table>

        {/* --- PAGINATION CONTROLS --- */}
        {totalPages > 1 && (
          <div className="flex justify-center gap-2 p-4">
            <button
              disabled={currentPage === 1}
              onClick={() => setCurrentPage((p) => p - 1)}
              className="px-3 py-1 rounded border disabled:opacity-50"
            >
              Trước
            </button>
            {Array.from({ length: totalPages }, (_, i) => (
              <button
                key={i}
                onClick={() => setCurrentPage(i + 1)}
                className={`px-3 py-1 rounded border ${
                  currentPage === i + 1 ? "bg-gray-200" : ""
                }`}
              >
                {i + 1}
              </button>
            ))}
            <button
              disabled={currentPage === totalPages}
              onClick={() => setCurrentPage((p) => p + 1)}
              className="px-3 py-1 rounded border disabled:opacity-50"
            >
              Tiếp
            </button>
          </div>
        )}
      </div>
    </section>
  );
}
