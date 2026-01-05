#include "config.h"
#include "wifi_manager.h"
#include "mqtt_manager.h"
#include "sensor_manager.h"
#include <Preferences.h>

void setup()
{
  /* ===== RESET WIFI BẰNG BOOT (RẤT SỚM) ===== */
  pinMode(BOOT_PIN, INPUT_PULLUP);
  delay(50); // cho GPIO ổn định

  /* ===== SETUP BÌNH THƯỜNG ===== */
  Serial.begin(115200);
  dht.begin();

  addDevice(1, 18);
  addDevice(2, 19);

  // Kết nối WiFi (STA hoặc AP mode)
  connectWiFi();

  // 👉 CHỈ KHI ĐÃ KẾT NỐI WIFI THẬT
  if (WiFi.status() == WL_CONNECTED)
  {
    timeClient.begin();
    timeClient.forceUpdate();
    connectMQTT();
  }
}

void loop()
{
  if (Serial.available())
  {
    char c = Serial.read();
    if (c == 'R' || c == 'r')
    {
      resetWiFi();
    }
  }

  /* ===== ĐANG Ở AP MODE → CHỈ PHỤC VỤ WEB ===== */
  if (WiFi.getMode() == WIFI_AP)
  {
    server.handleClient();
    delay(10);
    return;
  }

  /* ===== CÓ WIFI → MQTT + SENSOR ===== */
  if (!mqttClient.connected())
    connectMQTT();

  mqttClient.loop();

  static unsigned long lastSend = 0;
  if (millis() - lastSend > 5000)
  {
    lastSend = millis();
    mqttClient.publish(
        SENSOR_TOPIC,
        buildPayload(readSensors()).c_str());
  }
}
