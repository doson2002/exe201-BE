package com.exe201.exe201be.dtos;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FAQRequestDTO {
    private String title;
    private String article;

}