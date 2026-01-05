import { useState } from "react";
import api from "../api/axios";

type Props = {
  deviceId: number;
  onClose: () => void;
};

export default function SchedulerModal({ deviceId, onClose }: Props) {
  const [onTime, setOnTime] = useState("");
  const [offTime, setOffTime] = useState("");

  const submit = async () => {
    await api.post("/api/schedulers", {
      deviceId,
      repeatType: "ONCE",
      onTime,
      offTime,
    });
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div className="bg-white text-black rounded-xl p-6 w-96 space-y-4">
        <h3 className="text-lg font-semibold">⏰ Lập lịch thiết bị</h3>

        <div>
          <label className="block mb-1">Thời gian bật</label>
          <input
            type="datetime-local"
            className="w-full border rounded p-2"
            onChange={(e) =>
              setOnTime(e.target.value + ":00")
            }
          />
        </div>

        <div>
          <label className="block mb-1">Thời gian tắt</label>
          <input
            type="datetime-local"
            className="w-full border rounded p-2"
            onChange={(e) =>
              setOffTime(e.target.value + ":00")
            }
          />
        </div>

        <div className="flex justify-end gap-3">
          <button onClick={onClose}>Hủy</button>
          <button
            onClick={submit}
            className="bg-green-600 text-white px-4 py-1 rounded"
          >
            Lưu
          </button>
        </div>
      </div>
    </div>
  );
}
