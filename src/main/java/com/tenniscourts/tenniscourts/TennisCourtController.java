package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tennis-courts")
@AllArgsConstructor
public class TennisCourtController extends BaseRestController {

    private final TennisCourtService tennisCourtService;

    @PostMapping
    @ApiOperation("Add tennis court")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "TennisCourt added successfully"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid data")
    })
    public ResponseEntity<Void> addTennisCourt(@RequestBody CreateTennisCourtRequestDTO createTennisCourtRequestDTO) {
        return ResponseEntity.created(locationByEntity(tennisCourtService.addTennisCourt(createTennisCourtRequestDTO).getId())).build();
    }

    @GetMapping("/{id}")
    @ApiOperation("Find tennis court by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok. TennisCourt successfully found"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid Id"),
            @ApiResponse(code = 404, message = "Not Found. TennisCourt not found")
    })
    public ResponseEntity<TennisCourtDTO> findTennisCourtById(@PathVariable("id") Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtById(tennisCourtId));
    }

    @GetMapping("/{id}/schedules")
    @ApiOperation("Find tennis court with schedules by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok. TennisCourt with schedules successfully found"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid Id"),
            @ApiResponse(code = 404, message = "Not Found. TennisCourt with schedules not found")
    })
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@PathVariable("id") Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
    }
}
