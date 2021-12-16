package rw.ac.rca.termOneExam.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.repository.ICityRepository;
import rw.ac.rca.termOneExam.utils.CityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceTest {

    @Mock
    private ICityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    public void getById_success() {
        long id = 101;
        when(cityRepository.findById(id)).thenReturn(Optional.of(new City(101, "Kigali", 20, 0)));
        double expectedValue = (20 * 1.8) + 32;
        assertEquals(expectedValue, cityService.getById(id).get().getFahrenheit());
    }


    @Test
    public void getAll_success() {
        when(cityRepository.findAll()).thenReturn(Arrays.asList(new City(101, "Kigali", 20, 0), new City(102, "Musanze", 10, 0), new City(103, "Rubavu", 5, 0)));
        double expectedValue = (20 * 1.8) + 32;
        assertEquals(expectedValue, cityService.getAll().get(0).getFahrenheit());
    }

    @Test
    public void existsByName_success() {
        String name = "Kigali";
        when(cityRepository.existsByName(name)).thenReturn(true);
        assertTrue(cityService.existsByName(name));
    }

    @Test
    public void save_success() {
        City city = new City(101, "Kigali", 24, 0);

        CreateCityDTO dto = new CreateCityDTO();
        dto.setName(city.getName());
        dto.setWeather(city.getWeather());

        when(cityRepository.save(city)).thenReturn(city);
        double expectedValue = (24 * 1.8) + 32;
        assertEquals(expectedValue, cityService.save(dto).getFahrenheit());
    }


}
