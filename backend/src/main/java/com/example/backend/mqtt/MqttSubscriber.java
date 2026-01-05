package com.example.backend.mqtt;

import com.example.backend.entity.Garden;
import com.example.backend.entity.SensorData;
import com.example.backend.repository.DataRepository;
import com.example.backend.repository.GardenRepository;
import com.example.backend.service.DataService;
import com.example.backend.service.DeviceLogService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.example.backend.constant.OnOffEnum;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class MqttSubscriber {

    private static final String SENSOR_TOPIC = "sensor_data";
    private static final String LOG_TOPIC = "device_log";

    @Autowired
    private MqttClient mqttClient;

    @Autowired
    private DataService dataService;

    @Autowired
    private DeviceLogService deviceLogService;

    @PostConstruct
    public void subscribe() throws MqttException {
        mqttClient.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("MQTT connection lost: " + cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                JsonObject json = JsonParser.parseString(payload).getAsJsonObject();

                if (topic.equals(SENSOR_TOPIC)) {
                    int gardenId = json.get("gardenId").getAsInt();
                    LocalDateTime now = LocalDateTime.now();

                    dataService.updateByGardenAndType(gardenId, "TEMPERATURE", json.get("temperature").getAsDouble(), now);
                    dataService.updateByGardenAndType(gardenId, "HUMIDITY", json.get("humidity").getAsDouble(), now);
                    dataService.updateByGardenAndType(gardenId, "LIGHT", json.get("light").getAsDouble(), now);
                    dataService.updateByGardenAndType(gardenId, "SOIL_MOISTURE", json.get("soil_moisture").getAsDouble(), now);

                } else if (topic.equals(LOG_TOPIC)) {
                    int deviceId = json.get("deviceId").getAsInt();
                    OnOffEnum status = OnOffEnum.valueOf(json.get("status").getAsString());
                    LocalDateTime time = LocalDateTime.parse(json.get("timestamp").getAsString());

                    deviceLogService.updateDeviceStatus(deviceId, status, time);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });

        mqttClient.subscribe(SENSOR_TOPIC);
        mqttClient.subscribe(LOG_TOPIC);
    }
}