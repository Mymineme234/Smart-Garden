#pragma once
#include <DHT.h>
#include "config.h"

DHT dht(DHTPIN, DHTTYPE);

struct SensorData
{
    float temperature;
    float humidity;
    int light;
    int soilMoisture;
};

SensorData readSensors()
{
    SensorData data;
    data.temperature = dht.readTemperature();
    data.humidity = dht.readHumidity();
    data.light = analogRead(LIGHT_SENSOR_PIN);
    data.soilMoisture = analogRead(SOIL_SENSOR_PIN);
    return data;
}

/* Payload sensor – GIỮ NGUYÊN */
String buildPayload(const SensorData &data)
{
    String payload = "{";
    payload += "\"gardenId\":" + String(GARDEN_ID) + ",";
    payload += "\"temperature\":" + String(data.temperature) + ",";
    payload += "\"humidity\":" + String(data.humidity) + ",";
    payload += "\"light\":" + String(data.light) + ",";
    payload += "\"soil_moisture\":" + String(data.soilMoisture);
    payload += "}";
    return payload;
}
