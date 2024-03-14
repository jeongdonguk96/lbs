package io.spring.lbs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.lbs.mongo.CenterSphereRequestDto;
import io.spring.lbs.mongo.PolygonRequestDto;
import io.spring.lbs.mysql.GeospatialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ApiControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private GeospatialRepository geospatialRepository;

    @BeforeEach
    void deleteAll() {
        geospatialRepository.deleteAll();
    }

    @Test
    @DisplayName(value = "Polygon으로 mongo 조회 후 mysql 삽입 테스트")
    void useMongoPolygon() throws Exception {
        // given
        List<List<Double>> coordinates = new ArrayList<>();
        coordinates.add(Arrays.asList(126.969665, 37.554877));
        coordinates.add(Arrays.asList(126.976429, 37.553087));
        coordinates.add(Arrays.asList(126.976546, 37.555221));
        coordinates.add(Arrays.asList(126.978747, 37.553207));
        coordinates.add(Arrays.asList(126.985415, 37.553687));
        coordinates.add(Arrays.asList(126.995229, 37.547229));
        coordinates.add(Arrays.asList(126.998325, 37.549695));
        coordinates.add(Arrays.asList(127.004368, 37.550159));
        coordinates.add(Arrays.asList(127.006133, 37.548202));
        coordinates.add(Arrays.asList(127.004997, 37.546170));
        coordinates.add(Arrays.asList(127.007278, 37.543859));
        coordinates.add(Arrays.asList(127.008985, 37.544133));
        coordinates.add(Arrays.asList(127.009591, 37.539639));
        coordinates.add(Arrays.asList(127.017262, 37.537681));
        coordinates.add(Arrays.asList(127.017193, 37.533789));
        coordinates.add(Arrays.asList(127.006140, 37.522902));
        coordinates.add(Arrays.asList(126.990702, 37.513092));
        coordinates.add(Arrays.asList(126.985537, 37.506547));
        coordinates.add(Arrays.asList(126.975249, 37.507021));
        coordinates.add(Arrays.asList(126.949885, 37.517518));
        coordinates.add(Arrays.asList(126.949879, 37.527029));
        coordinates.add(Arrays.asList(126.944819, 37.534520));
        coordinates.add(Arrays.asList(126.953357, 37.537500));
        coordinates.add(Arrays.asList(126.958174, 37.542313));
        coordinates.add(Arrays.asList(126.957905, 37.545463));
        coordinates.add(Arrays.asList(126.963720, 37.548792));
        coordinates.add(Arrays.asList(126.962338, 37.551555));
        coordinates.add(Arrays.asList(126.965714, 37.554151));
        coordinates.add(Arrays.asList(126.969185, 37.555662));
        coordinates.add(Arrays.asList(126.969665, 37.554877));

        PolygonRequestDto polygonRequestDto = new PolygonRequestDto();
        polygonRequestDto.setGu("용산구");
        polygonRequestDto.setPolygonCoordinates(coordinates);

        String requestBody = objectMapper.writeValueAsString(polygonRequestDto);

        // when
        ResultActions resultActions = mvc.perform(post("/mongo/polygon").contentType(MediaType.APPLICATION_JSON).content(requestBody));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        // then
        resultActions.andExpect(status().is2xxSuccessful());
        long totalGeospatialSize = geospatialRepository.findAll().size();
        assertThat(totalGeospatialSize).isEqualTo(Integer.parseInt(responseBody));
    }

    @Test
    @DisplayName(value = "CenterSphere로 mongo 조회 후 mysql 삽입 테스트")
    void useMongoCenterSphere() throws Exception {
        // given
        CenterSphereRequestDto centerSphereRequestDto = new CenterSphereRequestDto();
        centerSphereRequestDto.setPlace("용산구청");
        centerSphereRequestDto.setCoordinates(Arrays.asList(126.972658, 37.576025));
        centerSphereRequestDto.setKmRadius(5);

        String requestBody = objectMapper.writeValueAsString(centerSphereRequestDto);

        // when
        ResultActions resultActions = mvc.perform(post("/mongo/centerSphere").contentType(MediaType.APPLICATION_JSON).content(requestBody));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        // then
        resultActions.andExpect(status().is2xxSuccessful());
        long totalGeospatialSize = geospatialRepository.findAll().size();
        assertThat(totalGeospatialSize).isEqualTo(Integer.parseInt(responseBody));
    }
}