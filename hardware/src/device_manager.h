#pragma once
#include <Arduino.h>

/*
  Quản lý danh sách thiết bị điều khiển
  Mỗi thiết bị được ánh xạ: deviceId <-> GPIO
*/

#define MAX_DEVICES 10

struct Device
{
    int deviceId;
    int gpio;
};

Device devices[MAX_DEVICES];
int deviceCount = 0;

/* Tìm thiết bị theo deviceId */
Device *findDevice(int deviceId)
{
    for (int i = 0; i < deviceCount; i++)
    {
        if (devices[i].deviceId == deviceId)
            return &devices[i];
    }
    return nullptr;
}

/* Thêm thiết bị mới */
bool addDevice(int deviceId, int gpio)
{
    if (deviceCount >= MAX_DEVICES)
        return false;
    if (findDevice(deviceId))
        return false;

    pinMode(gpio, OUTPUT);
    digitalWrite(gpio, LOW);

    devices[deviceCount++] = {deviceId, gpio};

    Serial.printf("[DEVICE] Added deviceId=%d GPIO=%d\n", deviceId, gpio);
    return true;
}

/* Xóa thiết bị theo deviceId */
bool deleteDevice(int deviceId)
{
    int idx = -1;
    for (int i = 0; i < deviceCount; i++)
    {
        if (devices[i].deviceId == deviceId)
        {
            idx = i;
            break;
        }
    }

    if (idx == -1)
    {
        Serial.printf("[DEVICE] DeviceId=%d not found\n", deviceId);
        return false;
    }

    // Tắt thiết bị trước khi xóa
    digitalWrite(devices[idx].gpio, LOW);

    // Dời các phần tử phía sau lên trước
    for (int i = idx; i < deviceCount - 1; i++)
    {
        devices[i] = devices[i + 1];
    }
    deviceCount--;

    Serial.printf("[DEVICE] Deleted deviceId=%d\n", deviceId);
    return true;
}

/* Điều khiển thiết bị */
void controlDevice(int deviceId, bool on)
{
    Device *d = findDevice(deviceId);
    if (!d)
    {
        Serial.println("[DEVICE] Device not found");
        return;
    }
    digitalWrite(d->gpio, on ? HIGH : LOW);
}
