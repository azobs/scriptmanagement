package com.sprintgether.script.management.server.scriptmanagement.form.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class RoleForm {
    @NotNull(message = "The roleName cannot be null")
    @NotBlank(message = "The roleName cannot be blank caracter")
    @NotEmpty(message = "The roleName cannot be empty")
    @Size(min=2, max=50, message="The roleName must have at least 2 caracteres and at most 50")
    String roleName;
    String roleAlias;
    String roleDescription;

}
