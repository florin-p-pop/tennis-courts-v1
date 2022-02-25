package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reservations")
@AllArgsConstructor
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping
    @ApiOperation("Book reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Reservation successfully booked"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid data")
    })
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping("/{id}")
    @ApiOperation("Find reservation by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok. Reservation successfully found"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid Id"),
            @ApiResponse(code = 404, message = "Not Found. Reservation not found")
    })
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable("id") Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("Cancel reservation by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok. Reservation successfully cancelled"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid Id"),
            @ApiResponse(code = 404, message = "Not Found. Reservation not found")
    })
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable("id") Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping("/reschedule")
    @ApiOperation("Reschedule reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Reservation successfully rescheduled"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid data"),
            @ApiResponse(code = 404, message = "Not Found. Reservation not found")
    })
    public ResponseEntity<ReservationDTO> rescheduleReservation(@RequestParam("reservationId") Long reservationId, @RequestParam("scheduleId") Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
