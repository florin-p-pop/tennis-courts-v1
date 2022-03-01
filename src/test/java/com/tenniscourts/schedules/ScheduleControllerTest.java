package com.tenniscourts.schedules;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.Month.DECEMBER;
import static java.time.Month.FEBRUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleControllerTest {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addScheduleTennisCourt() throws Exception {
        mockMvc.perform(post("/schedules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateScheduleRequestDTO(1L, LocalDateTime.now().plusDays(10)))))
                .andExpect(status().isCreated());
    }

    @Test
    public void findScheduleById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/schedules/1"))
                .andExpect(status().isOk())
                .andReturn();

        ScheduleDTO scheduleDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ScheduleDTO.class);
        assertEquals(1, scheduleDTO.getId());
    }

    @Test
    public void findSchedulesBetweenDates() throws Exception {
        LocalDate startDate = LocalDate.of(2020, FEBRUARY, 20);
        LocalDate endDate = LocalDate.of(2020, FEBRUARY, 21);
        MvcResult mvcResult = mockMvc.perform(get("/schedules//between-dates?" + "startDate=" + startDate + "&endDate=" + endDate))
                .andExpect(status().isOk())
                .andReturn();

        List<ScheduleDTO> scheduleDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(1, scheduleDTOs.size());
        assertEquals(1, scheduleDTOs.get(0).getId());
    }

    @Test
    public void findAvailableSchedulesBetweenDates() throws Exception {
        LocalDate startDate = LocalDate.of(2030, DECEMBER, 1);
        LocalDate endDate = LocalDate.of(2030, DECEMBER, 2);
        MvcResult mvcResult = mockMvc.perform(get("/schedules//between-dates?" + "startDate=" + startDate + "&endDate=" + endDate))
                .andExpect(status().isOk())
                .andReturn();

        List<ScheduleDTO> scheduleDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(4, scheduleDTOs.size());
    }
}
