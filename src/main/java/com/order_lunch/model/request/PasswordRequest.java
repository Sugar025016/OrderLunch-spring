package com.order_lunch.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequest {

        @NotBlank
        @Size(min = 8, max = 16)
        private String password;

        @NotBlank
        @Size(min = 8, max = 16)
        private String newPassword;

}
