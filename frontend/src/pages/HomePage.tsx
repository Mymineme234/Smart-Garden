import bgGarden from "../assets/bg-garden.jpg";
import Welcome from "../components/Welcome";
import SensorTable from "../components/SensorTable";
import DeviceTable from "../components/DeviceTable";
import DeviceLogTable from "../components/DeviceLogTable";
import SchedulerTable from "../components/SchedulerTable";

export default function HomePage() {
  return (
    <div
      className="min-h-screen w-full bg-cover bg-center text-white"
      style={{ backgroundImage: `url(${bgGarden})` }}
    >
      {/* overlay */}
      <div className="min-h-screen bg-black/40">
        <div className="max-w-[1400px] mx-auto px-8 py-10 space-y-12">
          <Welcome />
          <SensorTable />
          <DeviceTable />
          <SchedulerTable />
          <DeviceLogTable />
        </div>
      </div>
    </div>
  );
}
