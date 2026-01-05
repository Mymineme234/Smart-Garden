package com.example.backend.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

@Component
public class MqttPublisher {

    private final MqttClient client;

    public MqttPublisher(MqttClient client) {
        this.client = client;
    }

    public void publish(String topic, String payload) {
        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1);
            client.publish(topic, message);
        } catch (Exception e) {
            throw new RuntimeException("MQTT publish failed", e);
        }
    }
}
