package com.tenniscourts.reservations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.guests.GuestDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void bookReservation() throws Exception {
        mockMvc.perform(post("/reservations").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateReservationRequestDTO.builder().guestId(1L).scheduleId(4L).build())))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void bookReservation_startDateInThePast() throws Exception {
        mockMvc.perform(post("/reservations").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateReservationRequestDTO.builder().guestId(1L).scheduleId(1L).build())))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("StartDateTime can not be in the past.", result.getResolvedException().getMessage()));
    }

    @Test
    public void cancelReservation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/reservations/cancel/2"))
                .andExpect(status().isOk())
                .andReturn();

        ReservationDTO reservationDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationDTO.class);
        assertEquals(2, reservationDTO.getId());
        assertEquals(ReservationStatus.CANCELLED.name(), reservationDTO.getReservationStatus());
    }

    @Test
    public void cancelReservation_wrongStatus() throws Exception {
        mockMvc.perform(put("/reservations/cancel/3"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Cannot cancel/reschedule because it's not in ready to play status.", result.getResolvedException().getMessage()));
    }

    @Test
    public void cancelReservation_dateInThePast() throws Exception {
        mockMvc.perform(put("/reservations/cancel/1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Can cancel/reschedule only future dates.", result.getResolvedException().getMessage()));
    }

    @Test
    public void findAllPastReservations() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/reservations/past-reservations"))
                .andExpect(status().isOk())
                .andReturn();

        List<ReservationDTO> reservationDTOs = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(1, reservationDTOs.size());
        assertEquals(1, reservationDTOs.get(0).getId());
    }

    @Test
    public void findReservation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/reservations/1"))
                .andExpect(status().isOk())
                .andReturn();

        ReservationDTO reservationDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationDTO.class);
        assertEquals(1, reservationDTO.getId());
    }

    @Test
    public void findReservation_notFound() throws Exception {
        mockMvc.perform(get("/reservations/100"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("Reservation not found.", result.getResolvedException().getMessage()));
    }

    @Test
    public void rescheduleReservation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/reservations/reschedule?" + "reservationId=4" + "&scheduleId=5"))
                .andExpect(status().isOk())
                .andReturn();

        ReservationDTO reservationDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationDTO.class);
        assertEquals(5, reservationDTO.getId());
        assertEquals(5, reservationDTO.getSchedule().getId());
        assertEquals(ReservationStatus.READY_TO_PLAY.name(), reservationDTO.getReservationStatus());
        ReservationDTO previousReservation = reservationDTO.getPreviousReservation();
        assertEquals(4, previousReservation.getId());
        assertEquals(4, previousReservation.getSchedule().getId());
        assertEquals(ReservationStatus.RESCHEDULED.name(), previousReservation.getReservationStatus());
    }
}
