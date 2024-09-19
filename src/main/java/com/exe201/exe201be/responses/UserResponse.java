package com.exe201.exe201be.responses;

import com.exe201.exe201be.entities.Role;
import com.exe201.exe201be.entities.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse extends BaseResponse{
    private Long id;
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("gender")
    private int gender;
    @JsonProperty("active")
    private boolean active;
    @JsonProperty("first_login")
    private Boolean firstLogin;
    @JsonProperty("role")
    private Role role;


    public static UserResponse fromUser(Users user) {
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .active(user.isActive())
                .firstLogin(user.getFirstLogin())
                .role(user.getRole())
                .build();
        userResponse.setCreatedDate(user.getCreatedDate());
        userResponse.setModifiedDate(user.getModifiedDate());
        return userResponse;
    }
}
