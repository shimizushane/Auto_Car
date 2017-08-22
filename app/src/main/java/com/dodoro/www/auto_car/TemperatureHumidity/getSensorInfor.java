package com.dodoro.www.auto_car.TemperatureHumidity;

/**
 * Created by YVTC on 2017/8/21.
 */

public class getSensorInfor {
    String date;
    float temperature;
    float humidity;
    float distance;

    public getSensorInfor() {
    }

    public getSensorInfor(String date, float temperature, float humidity, float distance) {
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.distance = distance;
    }

    public String getDate() {
        return date;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getDistance(){return distance;}
}
