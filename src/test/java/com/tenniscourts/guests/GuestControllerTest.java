package com.tenniscourts.guests;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GuestControllerTest {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addGuest() throws Exception {
        mockMvc.perform(post("/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateGuestRequestDTO("New Guest"))))
                .andExpect(status().isCreated());

        MvcResult mvcResult = mockMvc.perform(get("/guests/3")).andReturn();
        GuestDTO guestDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GuestDTO.class);
        assertEquals(3, guestDTO.getId());
        assertEquals("New Guest", guestDTO.getName());
    }

    @Test
    public void deleteGuest() throws Exception {
        mockMvc.perform(delete("/guests/3"))
                .andExpect(status().isOk());
    }

    @Test
    public void findAllGuests() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/guests"))
                .andExpect(status().isOk()).andReturn();

        List<GuestDTO> guestDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(2, guestDTOs.size());
    }

    @Test
    public void findGuestById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/guests/1"))
                .andExpect(status().isOk()).andReturn();

        GuestDTO guestDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GuestDTO.class);
        assertEquals(1, guestDTO.getId());
    }

    @Test
    public void findGuestById_notFound() throws Exception {
        mockMvc.perform(get("/guests/100").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Guest not found.", result.getResolvedException().getMessage()));
    }

    @Test
    public void findGuestsContainingName() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/guests/by-name?name=Federer"))
                .andExpect(status().isOk())
                .andReturn();

        List<GuestDTO> guestDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(1, guestDTOs.size());
        assertEquals(1, guestDTOs.get(0).getId());
        assertTrue(guestDTOs.get(0).getName().contains("Federer"));
    }

    @Test
    public void updateGuest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new GuestDTO(1L, "New Guest"))))
                .andExpect(status().isOk())
                .andReturn();

        GuestDTO guestDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GuestDTO.class);
        assertEquals(1, guestDTO.getId());
        assertEquals("New Guest", guestDTO.getName());
    }
}
