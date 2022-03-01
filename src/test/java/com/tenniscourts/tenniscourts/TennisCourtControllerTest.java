package com.tenniscourts.tenniscourts;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TennisCourtControllerTest {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addTennisCourt() throws Exception {
        mockMvc.perform(post("/tennis-courts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateTennisCourtRequestDTO("New Court"))))
                .andExpect(status().isCreated());

        MvcResult mvcResult = mockMvc.perform(get("/tennis-courts/2")).andReturn();
        TennisCourtDTO tennisCourtDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TennisCourtDTO.class);
        assertEquals(2, tennisCourtDTO.getId());
        assertEquals("New Court", tennisCourtDTO.getName());
    }

    @Test
    public void findTennisCourtById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/tennis-courts/1"))
                .andExpect(status().isOk())
                .andReturn();

        TennisCourtDTO tennisCourtDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TennisCourtDTO.class);
        assertEquals(1, tennisCourtDTO.getId());
    }

    @Test
    public void findTennisCourtById_notFound() throws Exception {
        mockMvc.perform(get("/tennis-courts/100"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Tennis Court not found.", result.getResolvedException().getMessage()));
    }

    @Test
    public void findTennisCourtWithSchedulesById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/tennis-courts/1/schedules"))
                .andExpect(status().isOk())
                .andReturn();

        TennisCourtDTO tennisCourtDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TennisCourtDTO.class);
        assertEquals(1, tennisCourtDTO.getId());
        assertFalse(tennisCourtDTO.getTennisCourtSchedules().isEmpty());
    }
}
