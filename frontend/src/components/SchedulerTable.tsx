import { useEffect, useMemo, useState } from "react";
import api from "../api/axios";
import { TrashIcon } from "@heroicons/react/24/outline";

type Scheduler = {
  id: number;
  deviceId: number;
  repeatType: string;
  onTime: string;
  offTime: string;
};

type SortField = "id" | "onTime" | "offTime";
type SortDirection = "asc" | "desc";

export default function SchedulerTable() {
  const [schedulers, setSchedulers] = useState<Scheduler[]>([]);
  const [sortField, setSortField] = useState<SortField>("id");
  const [sortDirection, setSortDirection] = useState<SortDirection>("desc");
  const [currentPage, setCurrentPage] = useState(1);
  const rowsPerPage = 5;

  const load = async () => {
    const res = await api.get<Scheduler[]>("/api/schedulers");
    setSchedulers(res.data);
    setCurrentPage(1); // reset page
  };

  const remove = async (id: number) => {
    if (!confirm("Bạn có chắc muốn xóa lịch này không?")) return;

    await api.delete(`/api/schedulers/${id}`);
    setSchedulers((prev) => prev.filter((s) => s.id !== id));
  };

  const changeSort = (field: SortField) => {
    if (field === sortField) {
      setSortDirection((d) => (d === "asc" ? "desc" : "asc"));
    } else {
      setSortField(field);
      setSortDirection("desc");
    }
  };

  const sortedSchedulers = useMemo(() => {
    return [...schedulers].sort((a, b) => {
      let va: number | string = a[sortField];
      let vb: number | string = b[sortField];

      if (sortField !== "id") {
        va = new Date(va).getTime();
        vb = new Date(vb).getTime();
      }

      return sortDirection === "asc"
        ? Number(va) - Number(vb)
        : Number(vb) - Number(va);
    });
  }, [schedulers, sortField, sortDirection]);

  // --- PHÂN TRANG ---
  const totalPages = Math.ceil(sortedSchedulers.length / rowsPerPage);
  const paginatedSchedulers = sortedSchedulers.slice(
    (currentPage - 1) * rowsPerPage,
    currentPage * rowsPerPage
  );

  const Arrow = ({ field }: { field: SortField }) =>
    field === sortField ? (
      <span className="ml-1">{sortDirection === "asc" ? "↑" : "↓"}</span>
    ) : null;

  useEffect(() => {
    load();
  }, []);

  return (
    <section>
      <h2 className="text-xl font-semibold mb-4">📅 Lịch của bạn</h2>

      <div className="bg-white text-black rounded-xl overflow-hidden">
        <table className="w-full text-center">
          <thead className="bg-gray-100">
            <tr>
              <th
                className="cursor-pointer select-none"
                onClick={() => changeSort("id")}
              >
                ID <Arrow field="id" />
              </th>
              <th>ID thiết bị</th>
              <th>Repeat</th>
              <th
                className="cursor-pointer select-none"
                onClick={() => changeSort("onTime")}
              >
                Bật <Arrow field="onTime" />
              </th>
              <th
                className="cursor-pointer select-none"
                onClick={() => changeSort("offTime")}
              >
                Tắt <Arrow field="offTime" />
              </th>
              <th>Xóa</th>
            </tr>
          </thead>

          <tbody>
            {paginatedSchedulers.map((s) => (
              <tr key={s.id} className="border-t">
                <td>{s.id}</td>
                <td>{s.deviceId}</td>
                <td>{s.repeatType}</td>
                <td>{new Date(s.onTime).toLocaleString()}</td>
                <td>{new Date(s.offTime).toLocaleString()}</td>
                <td className="flex justify-center py-2">
                  <button
                    onClick={() => remove(s.id)}
                    className="text-red-600 hover:text-red-800"
                    title="Xóa lịch"
                  >
                    <TrashIcon className="w-6 h-6" />
                  </button>
                </td>
              </tr>
            ))}

            {paginatedSchedulers.length === 0 && (
              <tr>
                <td colSpan={6} className="py-6 opacity-60">
                  Chưa có lịch nào
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
