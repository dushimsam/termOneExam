package rw.ac.rca.termOneExam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.repository.ICityRepository;
import rw.ac.rca.termOneExam.utils.CityUtil;

@Service
public class CityService {

    private final CityUtil cityUtil = new CityUtil();

    @Autowired
    private ICityRepository cityRepository;

    public Optional<City> getById(long id) {
        Optional<City> city = cityRepository.findById(id);
        city.get().setFahrenheit(cityUtil.fromCelsiusToFahrenheit(city.get().getWeather()));
        return city;
    }

    public List<City> getAll() {
        return cityUtil.modifyList(cityRepository.findAll());
    }

    public boolean existsByName(String name) {
        return cityRepository.existsByName(name);
    }

    public City save(CreateCityDTO dto) {
        City city = new City(dto.getName(), dto.getWeather());
        City savedCity = cityRepository.save(city);
        savedCity.setFahrenheit(cityUtil.fromCelsiusToFahrenheit(city.getWeather()));
        return savedCity;
    }



}
