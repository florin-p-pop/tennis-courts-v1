package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("schedules")
@AllArgsConstructor
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @PostMapping
    @ApiOperation("Add schedule for tennis court")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Schedule for tennis court added successfully"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid data")
    })
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO).getId())).build();
    }

    @GetMapping("/between-dates")
    @ApiOperation("Find schedule between dates")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok. Schedules successfully found"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid input data"),
            @ApiResponse(code = 404, message = "Not Found. Schedules not found")
    })
    public ResponseEntity<List<ScheduleDTO>> findSchedulesBetweenDates(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(LocalDateTime.of(startDate, LocalTime.of(0, 0)), LocalDateTime.of(endDate, LocalTime.of(23, 59))));
    }

    @GetMapping("/{id}")
    @ApiOperation("Find schedule by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok. Schedule successfully found"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid Id"),
            @ApiResponse(code = 404, message = "Not Found. Schedule not found")
    })
    public ResponseEntity<ScheduleDTO> findScheduleById(@PathVariable("id") Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
