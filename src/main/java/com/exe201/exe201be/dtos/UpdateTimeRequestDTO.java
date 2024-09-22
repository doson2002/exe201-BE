package com.exe201.exe201be.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTimeRequestDTO {

    @JsonProperty(value = "open_time")
    private LocalTime openTime;

    @JsonProperty(value = "close_time")
    private LocalTime closeTime;
}
