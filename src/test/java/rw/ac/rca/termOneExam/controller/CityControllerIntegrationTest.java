package rw.ac.rca.termOneExam.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.service.CityService;
import rw.ac.rca.termOneExam.utils.APICustomResponse;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(CityController.class)
public class CityControllerIntegrationTest {
    @MockBean
    private CityService cityServiceMock;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void getAll_success() throws Exception {
        List<City> asList = Arrays.asList(new City(102, "Musanze", 34),
                new City(101, "Kigali", 24));
        when(cityServiceMock.getAll()).thenReturn(asList);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/all-items")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":102,\"name\":\"Musanze\",\"weather\":34},{\"id\":101,\"name\":\"Kigali\",\"weather\":24}]"))
                .andReturn();

    }


    @Test
    public void getById_success() throws Exception {
        Optional<City> city = Optional.of(new City(101, "Kigali", 24, 0));


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/id/" + city.get().getId())
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":101,\"name\":\"Kigali\",\"weather\":24,\"fahrenheit\":0}"))
                .andReturn();

    }

    @Test
    public void getById_failure() throws Exception {
        Optional<City> city = Optional.ofNullable(null);

        when(cityServiceMock.getById(city.get().getId())).thenReturn(city);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/id/2")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc
                .perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\":false,\"message\":\"City not found with id 2\"}"))
                .andReturn();

    }


    @Test
    public void saveItem_success() throws Exception {
        City city = new City(101, "Kigali", 24, 0);
        CreateCityDTO dto = new CreateCityDTO();
        dto.setName(city.getName());
        dto.setWeather(city.getWeather());

        ResponseEntity.status(HttpStatus.CREATED).body(city);


        when(cityServiceMock.existsByName(city.getName())).thenReturn(false);
        when(cityServiceMock.save(dto)).thenReturn(city);

        String json = "{\"name\":Kigali,\"weather\":\"24\"}";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        MvcResult result = mockMvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().json(json))
                .andReturn();
    }


    @Test
    public void saveItem_failure() throws Exception {
        City city = new City(101, "Kigali", 24, 0);

        when(cityServiceMock.existsByName(city.getName())).thenReturn(true);

        String item_post_json = "{\"name\":Kigali,\"weather\":\"24\"}";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(item_post_json);

        MvcResult result = mockMvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\":false,\"message\":\"City name Kigali is registered already\"}"))
                .andReturn();
    }
}
