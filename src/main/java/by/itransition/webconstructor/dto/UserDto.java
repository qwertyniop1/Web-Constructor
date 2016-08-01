package by.itransition.webconstructor.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDto {

    @NotNull
    @Size(min = 2, max = 60)
    private String firstname;

    @NotNull
    @Size(min = 2, max = 60)
    private String lastname;

    @NotNull
    @Size(min = 2, max = 60)
    private String email;

    @NotNull
    @Size(min = 8, max = 60)
    private String password;

    private String matchingPassword;

    @NotNull
    @Size(min = 5, max = 60)
    private String username;

}
