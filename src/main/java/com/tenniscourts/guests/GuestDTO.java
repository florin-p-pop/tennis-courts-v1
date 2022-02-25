package com.tenniscourts.guests;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestDTO {

    private Long id;

    @NotBlank
    private String name;
}
