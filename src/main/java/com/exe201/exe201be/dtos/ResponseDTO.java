package com.exe201.exe201be.dtos;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private String status;
    private String errorMessage;

    // Constructor cho trường hợp thành công
    public ResponseDTO(String status) {
        this.status = status;
    }
}
