package com.sprintgether.script.management.server.scriptmanagement.form.School;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DepartmentForm {
    @NotNull(message = "The department name cannot be null")
    @NotBlank(message = "The department name cannot be blank caracter")
    @NotEmpty(message = "The department name cannot be empty")
    String name;
    @NotNull(message = "The department acronym  cannot be null")
    @NotBlank(message = "The department acronym  cannot be blank caracter")
    @NotEmpty(message = "The department acronym  cannot be empty")
    String acronym;
    String description;
    @NotNull(message = "The owner school name cannot be null")
    @NotBlank(message = "The owner school name cannot be blank caracter")
    @NotEmpty(message = "The owner school name cannot be empty")
    String ownerSchoolName;
}
