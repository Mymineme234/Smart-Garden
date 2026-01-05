export interface DeviceLog {
  id: number;
  deviceName: string;
  status: "ON" | "OFF";
  actionAt: string;
}
