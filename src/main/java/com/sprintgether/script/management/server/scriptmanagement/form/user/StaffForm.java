package com.sprintgether.script.management.server.scriptmanagement.form.user;

import com.sprintgether.script.management.server.scriptmanagement.model.user.EnumStaffType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class StaffForm {

    String staffId;
    @NotNull(message = "The staff name cannot be null")
    @NotBlank(message = "The staff name cannot be blank caracter")
    @NotEmpty(message = "The staff name cannot be empty")
    @Size(min=2, max=50, message="The staff name must have at least 2 caracters and at most 50")
    String firstName = "firstName";
    String lastName;
    @NotNull(message = "The staff type cannot be null")
    String staffType = EnumStaffType.LECTURER.name();
    @NotNull(message = "The staff email cannot be null")
    @NotBlank(message = "The staff email cannot be blank caracter")
    @NotEmpty(message = "The staff email cannot be empty")
    @Email(message = "Check the email enter and ensure that the email structure is respected")
    String email;
    @Email(message = "Check the email enter and ensure that the email structure is respected")
    String newEmail;
    String phoneNumber;
    String address;
    String description;
    /****
     * Field associated with User Model
     */
    @NotNull(message = "The staff username cannot be null")
    @NotBlank(message = "The staff username cannot be blank caracter")
    @NotEmpty(message = "The staff username cannot be empty")
    @Size(min=2, max=50, message="The staff username must have at least 2 caracteres and at most 50")
    String username;
    @NotEmpty(message = "The staff new username cannot be empty")
    @NotBlank(message = "The staff new username cannot be blank caracter")
    @Size(min=2, max=50, message="The staff new username must have at least 2 caracteres and at most 50")
    String newUsername;
    @NotNull(message = "The staff password cannot be null")
    @NotBlank(message = "The staff password cannot be blank caracter")
    @NotEmpty(message = "The staff password cannot be empty")
    @Size(min=2, max=50, message="The staff password must have at least 2 caracters and at most 50")
    String password = "1234";
    boolean active = false;

}
