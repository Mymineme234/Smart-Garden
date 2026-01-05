#pragma once
#include <WiFi.h>
#include <WebServer.h>
#include <Preferences.h>

/*
  - Nếu chưa có WiFi → ESP32 mở AP mode
  - Người dùng nhập SSID + Password qua web
  - ESP32 lưu lại và reboot
*/

Preferences prefs;
WebServer server(80);

/* Bật AP mode + web cấu hình */
void startAPMode()
{
    WiFi.softAP("ESP32_SETUP");
    Serial.println("[WIFI] AP mode started");
    Serial.println(WiFi.softAPIP());

    server.on("/", []()
              { server.send(200, "text/html",
                            "<h3>WiFi Setup</h3>"
                            "<form action='/save'>"
                            "SSID:<input name='s'><br>"
                            "Password:<input name='p' type='password'><br>"
                            "<input type='submit'>"
                            "</form>"); });

    server.on("/save", []()
              {
        prefs.begin("wifi", false);
        prefs.putString("ssid", server.arg("s"));
        prefs.putString("pass", server.arg("p"));
        prefs.end();

        server.send(200, "text/plain", "Saved. Rebooting...");
        delay(1000);
        ESP.restart(); });

    server.begin();
}

void resetWiFi()
{
    Serial.println("[WIFI] Reset WiFi requested");

    prefs.begin("wifi", false);
    prefs.clear();
    prefs.end();

    delay(500);
    ESP.restart();
}

/* Kết nối WiFi */
void connectWiFi()
{
    prefs.begin("wifi", true);
    String ssid = prefs.getString("ssid", "");
    String pass = prefs.getString("pass", "");
    prefs.end();

    // Chưa có WiFi → vào AP mode
    if (ssid == "")
    {
        startAPMode();
        return;
    }

    WiFi.begin(ssid.c_str(), pass.c_str());
    Serial.print("[WIFI] Connecting");

    unsigned long start = millis();
    while (WiFi.status() != WL_CONNECTED)
    {
        if (millis() - start > 20000)
        {
            Serial.println("\n[WIFI] Failed → AP mode");
            startAPMode();
            return;
        }
        delay(500);
        Serial.print(".");
    }

    Serial.println("\n[WIFI] Connected");
    Serial.println(WiFi.localIP());
}
