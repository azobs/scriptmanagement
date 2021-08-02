package com.sprintgether.script.management.server.scriptmanagement.form.school.department;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DepartmentNameUpdated {
    @NotNull(message = "The department id cannot be null")
    @NotBlank(message = "The department id cannot be blank caracter")
    @NotEmpty(message = "The department id cannot be empty")
    String departmentId;
    @NotNull(message = "The department name cannot be null")
    @NotBlank(message = "The department name cannot be blank caracter")
    @NotEmpty(message = "The department name cannot be empty")
    String newDepartmentName;
}
