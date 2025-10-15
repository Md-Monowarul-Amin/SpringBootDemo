package com.example.springboot.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private List<UserDTO> content;
    private int page;
    private int totalPages;
    private long totalElements;
}
