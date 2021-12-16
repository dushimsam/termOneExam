package rw.ac.rca.termOneExam.utils;

import rw.ac.rca.termOneExam.domain.City;

import java.util.List;

public class CityUtil {

    public List<City> modifyList(List<City> cities) {
        for (City city : cities) {
            city.setFahrenheit(this.fromCelsiusToFahrenheit(city.getWeather()));
        }
        return cities;
    }

    public double fromCelsiusToFahrenheit(double celsius) {
        return (celsius * 1.8) + 32;
    }
}
