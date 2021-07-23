package com.sprintgether.script.management.server.scriptmanagement.form.School;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class LevelForm {
    @NotNull(message = "The option name cannot be null")
    @NotBlank(message = "The option name cannot be blank caracter")
    @NotEmpty(message = "The option name cannot be empty")
    String name;
    @NotNull(message = "The option acronym cannot be null")
    @NotBlank(message = "The option acronym cannot be blank caracter")
    @NotEmpty(message = "The option acronym cannot be empty")
    String acronym;
    @NotNull(message = "The owner option name cannot be null")
    @NotBlank(message = "The owner option name cannot be blank caracter")
    @NotEmpty(message = "The owner option name cannot be empty")
    String ownerOption;
    @NotNull(message = "The owner department name cannot be null")
    @NotBlank(message = "The owner department name cannot be blank caracter")
    @NotEmpty(message = "The owner department name cannot be empty")
    String ownerDepartment;
    @NotNull(message = "The owner school name cannot be null")
    @NotBlank(message = "The owner school name cannot be blank caracter")
    @NotEmpty(message = "The owner school name cannot be empty")
    String ownerSchool;
    String emailClassPerfect;
}
