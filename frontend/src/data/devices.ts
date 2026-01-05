export interface Device {
  id: number;
  deviceName: string;
  status: "ON" | "OFF";
  gardenName: string;
}
