#pragma once
#include <WiFi.h>

/* ================= MQTT ================= */
const char *MQTT_SERVER = "broker.emqx.io";
const int MQTT_PORT = 1883;

const char *SENSOR_TOPIC = "sensor_data";
const char *CONTROL_TOPIC = "control";
const char *LOG_TOPIC = "device_log";
const char *NEW_DEVICE_TOPIC = "new_device";
const char *DELETE_DEVICE_TOPIC = "delete_device";

/* ================= GARDEN ================= */
#define GARDEN_ID 1

/* ================= SENSORS ================= */
#define DHTPIN 4
#define DHTTYPE DHT11
#define LIGHT_SENSOR_PIN 34
#define SOIL_SENSOR_PIN 35

/* ================= SYSTEM ================= */
#define BOOT_PIN 0
