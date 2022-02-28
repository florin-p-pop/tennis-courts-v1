package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("guests")
@AllArgsConstructor
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @PostMapping
    @ApiOperation("Add guest")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Guest added successfully"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid data")
    })
    public ResponseEntity<Void> addGuest(@RequestBody CreateGuestRequestDTO createGuestRequestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.addGuest(createGuestRequestDTO).getId())).build();
    }

    @PutMapping
    @ApiOperation("Update guest")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Guest successfully updated"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid data"),
            @ApiResponse(code = 404, message = "Not Found. Guest not found")
    })
    public ResponseEntity<GuestDTO> updateGuest(@RequestBody GuestDTO guestDTO) {
        guestService.findGuestById(guestDTO.getId());
        return ResponseEntity.ok(guestService.updateGuest(guestDTO));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete guest")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Guest successfully deleted"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid id"),
            @ApiResponse(code = 404, message = "Not Found. Guest not found")
    })
    public ResponseEntity<Void> deleteGuest(@PathVariable("id") Long guestId) {
        guestService.findGuestById(guestId);
        guestService.deleteGuest(guestId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @ApiOperation("Find guest by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok. Guest successfully found"),
            @ApiResponse(code = 400, message = "Bad Request. Invalid Id"),
            @ApiResponse(code = 404, message = "Not Found. Guest not found")
    })
    public ResponseEntity<GuestDTO> findGuestById(@PathVariable("id") Long guestId) {
        return ResponseEntity.ok(guestService.findGuestById(guestId));
    }

    @GetMapping("/by-name")
    @ApiOperation("Find guests containing name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok. Guests successfully returned"),
            @ApiResponse(code = 404, message = "Not Found. Guests not found")
    })
    public ResponseEntity<List<GuestDTO>> findGuestsContainingName(@RequestParam("name") String name) {
        return ResponseEntity.ok(guestService.findGuestsContainingName(name));
    }

    @GetMapping
    @ApiOperation("Find all guests")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok. All guests successfully returned"),
            @ApiResponse(code = 404, message = "Not Found. Guests not found")
    })
    public ResponseEntity<List<GuestDTO>> findAllGuests() {
        return ResponseEntity.ok(guestService.findAllGuests());
    }
}
