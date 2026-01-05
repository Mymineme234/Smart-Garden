package com.example.backend.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    private static final String BROKER = "tcp://broker.emqx.io:1883";
    private static final String CLIENT_ID = "spring-mqtt-backend";

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setAutomaticReconnect(true);
        return options;
    }

    @Bean
    public MqttClient mqttClient(MqttConnectOptions options) throws Exception {
        MqttClient client = new MqttClient(BROKER, CLIENT_ID);
        client.connect(options);
        return client;
    }
}
