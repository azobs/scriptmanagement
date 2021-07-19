package com.sprintgether.script.management.server.scriptmanagement.form.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class StaffRoleForm {
    @Email(message = "Check the email enter and ensure that the email structure is respected")
    String email;
    @NotNull(message = "The roleName cannot be null")
    @NotBlank(message = "The roleName cannot be blank caracter")
    @NotEmpty(message = "The roleName cannot be empty")
    @Size(min=2, max=50, message="The roleName must have at least 2 caracteres and at most 50")
    String roleName;
}
