package com.exe201.exe201be.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierTypeDTO {
    @JsonProperty(value = "type_name")
    private String typeName;

    @JsonProperty(value = "img_url")
    private String imgUrl;
}
