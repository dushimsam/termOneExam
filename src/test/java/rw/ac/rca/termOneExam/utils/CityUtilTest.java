package rw.ac.rca.termOneExam.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.repository.ICityRepository;
import rw.ac.rca.termOneExam.service.CityService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityUtilTest {


    @Mock
    private ICityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void moreThan40Celsius_success() throws JSONException, JsonProcessingException {
        String jsonStr = this.restTemplate.getForObject("/api/cities/all", String.class);
        ObjectMapper mapper = new ObjectMapper();
        City[] jsonObj = mapper.readValue(jsonStr, City[].class);
        for (City itr : jsonObj) {
            assertTrue(itr.getWeather() <= 40);
        }
    }


    @Test
    public void lessThan10Celsius_success() throws JsonProcessingException {
        String jsonStr = this.restTemplate.getForObject("/api/cities/all", String.class);
        ObjectMapper mapper = new ObjectMapper();
        City[] jsonObj = mapper.readValue(jsonStr, City[].class);
        for (City itr : jsonObj) {
            assertTrue(itr.getWeather() >= 10);
        }
    }


    @Test
    public void containsMusanzeAndKigali_success() throws JsonProcessingException {
        String jsonStr = this.restTemplate.getForObject("/api/cities/all", String.class);
        ObjectMapper mapper = new ObjectMapper();
        City[] jsonObj = mapper.readValue(jsonStr, City[].class);
        ArrayList<String> allCities = new ArrayList<>();

        ArrayList<String> existingCities = new ArrayList<>();

        for (City itr : jsonObj) {
            allCities.add(itr.getName());
        }

        existingCities.add("Musanze");
        existingCities.add("Kigali");
        assertTrue(allCities.containsAll(existingCities));
    }


    @Test
    public void testSpying() throws JsonProcessingException {
        List<String> list = new ArrayList<String>();
        List<String> spyList = Mockito.spy(list);

        String jsonStr = this.restTemplate.getForObject("/api/cities/all", String.class);
        ObjectMapper mapper = new ObjectMapper();
        City[] jsonObj = mapper.readValue(jsonStr, City[].class);
        ArrayList<String> allCities = new ArrayList<>();

        for (City itr : jsonObj) {
            allCities.add(itr.getName());
        }

        spyList.add(allCities.get(0));
        spyList.add(allCities.get(1));

        verify(spyList).add(allCities.get(0));
        verify(spyList).add(allCities.get(1));

        assertEquals(2, spyList.size());
    }

    @Test
    public void testMocking() throws JsonProcessingException {
        String jsonStr = this.restTemplate.getForObject("/api/cities/all", String.class);
        ObjectMapper mapper = new ObjectMapper();
        City[] jsonObj = mapper.readValue(jsonStr, City[].class);
        ArrayList<String> allCities = new ArrayList<>();

        for (City itr : jsonObj) {
            allCities.add(itr.getName());
        }

        List<String> mockList = mock(List.class);
        mockList.add(allCities.get(0));
        mockList.size();

        verify(mockList).add(allCities.get(0));
    }
}
