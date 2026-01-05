package com.example.backend.dto;

import com.example.backend.constant.OnOffEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class DeviceDTO implements Serializable {
    private int id;
    private  String deviceName;
    private  OnOffEnum status;
    private int gpio;
    private  String gardenName;
}
