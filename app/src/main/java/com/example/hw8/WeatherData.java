package com.example.hw8;

public class WeatherData {
    private static WeatherData single_instance = null;

    private String city;
    private String country;
    private String temperature;
    private String feels_like;
    private String temp_min;
    private String temp_max;
    private String pressure;
    private String humidity;
    private String windspeed;
    private String winddir;
    private String weathermain;
    private String description;
    private String lon;
    private String lat;

    //gets
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getTemperature() { return temperature; }
    public String getFeels_like() { return feels_like; }
    public String getTemp_min() { return temp_min; }
    public String getTemp_max() { return temp_max; }
    public String getPressure() { return pressure; }
    public String getHumidity() { return humidity; }
    public String getWindspeed() { return windspeed; }
    public String getWinddir() { return winddir; }
    public String getWeathermain() { return weathermain; }
    public String getDescription() { return description; }
    public String getLon() { return lon; }
    public String getLat() { return lat; }

    //sets
    public void setCity(String temp) { city = temp; }
    public void setCountry(String temp) { country = temp; }
    public void setTemperature(String temp) { temperature = temp; }
    public void setFeels_like(String temp) { feels_like = temp; }
    public void setTemp_min(String temp) { temp_min = temp; }
    public void setTemp_max(String temp) { temp_max = temp; }
    public void setPressure(String temp) { pressure = temp; }
    public void setHumidity(String temp) { humidity = temp; }
    public void setWindspeed(String temp) { windspeed = temp; }
    public void setWinddir(String temp) { winddir = temp; }
    public void setWeathermain(String temp) { weathermain = temp; }
    public void setDescription(String temp) { description = temp; }
    public void setLon(String temp) { lon = temp; }
    public void setLat(String temp) { lat = temp; }

    //instantiate
    public static WeatherData getInstance()
    {
        if(single_instance == null)
        {
            single_instance = new WeatherData();
        }
        return single_instance;
    }

}
