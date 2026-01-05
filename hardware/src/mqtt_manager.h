#pragma once
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include <WiFiUdp.h>
#include <NTPClient.h>

#include "config.h"
#include "device_manager.h"

/* ================= NTP ================= */
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org", 7 * 3600);

/* ================= MQTT ================= */
WiFiClient espClient;
PubSubClient mqttClient(espClient);

/* Lấy timestamp ISO chuẩn 'yyyy-MM-ddTHH:mm:ss' */
String getTimestamp()
{
    timeClient.update();
    time_t raw = timeClient.getEpochTime();
    struct tm *t = localtime(&raw);

    char buf[25];
    // Thêm 'T' giữa ngày và giờ → chuẩn ISO
    sprintf(buf, "%04d-%02d-%02dT%02d:%02d:%02d",
            t->tm_year + 1900, t->tm_mon + 1, t->tm_mday,
            t->tm_hour, t->tm_min, t->tm_sec);
    return String(buf);
}

/* ===== Publish log (GIỮ NGHIỆP VỤ, THÊM TIME) ===== */
void publishLog(int deviceId, const String &status)
{
    String payload = "{";
    payload += "\"deviceId\":" + String(deviceId) + ",";
    payload += "\"status\":\"" + status + "\",";
    payload += "\"timestamp\":\"" + getTimestamp() + "\"";
    payload += "}";

    mqttClient.publish(LOG_TOPIC, payload.c_str());
    Serial.println("[LOG] " + payload);
}

/* ===== MQTT Callback ===== */
void mqttCallback(char *topic, byte *payload, unsigned int length)
{
    String message;
    for (unsigned int i = 0; i < length; i++)
        message += (char)payload[i];

    Serial.println("[MQTT] " + message);

    if (String(topic) == NEW_DEVICE_TOPIC)
    {
        StaticJsonDocument<128> doc;
        deserializeJson(doc, message);
        addDevice(doc["deviceId"], doc["gpio"]);
    }

    if (String(topic) == DELETE_DEVICE_TOPIC)
    {
        StaticJsonDocument<128> doc;
        deserializeJson(doc, message);
        int deviceId = doc["deviceId"];
        deleteDevice(deviceId);
    }

    if (String(topic) == CONTROL_TOPIC)
    {
        int idx = message.indexOf('/');
        if (idx > 0)
        {
            int deviceId = message.substring(0, idx).toInt();
            String cmd = message.substring(idx + 1);

            controlDevice(deviceId, cmd == "ON");
            publishLog(deviceId, cmd);
        }
    }
}

/* ===== Kết nối MQTT ===== */
void connectMQTT()
{
    mqttClient.setServer(MQTT_SERVER, MQTT_PORT);
    mqttClient.setCallback(mqttCallback);

    while (!mqttClient.connected())
    {
        String clientId = "ESP32-GARDEN-" + String(GARDEN_ID);
        if (mqttClient.connect(clientId.c_str()))
        {
            mqttClient.subscribe(CONTROL_TOPIC);
            mqttClient.subscribe(NEW_DEVICE_TOPIC);
        }
        else
        {
            delay(2000);
        }
    }
}
