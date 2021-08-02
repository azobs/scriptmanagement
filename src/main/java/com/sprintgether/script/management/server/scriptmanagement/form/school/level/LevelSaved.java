package com.sprintgether.script.management.server.scriptmanagement.form.school.level;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class LevelSaved {
    @NotNull(message = "The level name cannot be null")
    @NotBlank(message = "The level name cannot be blank caracter")
    @NotEmpty(message = "The level name cannot be empty")
    String name;
    @NotNull(message = "The level acronym cannot be null")
    @NotBlank(message = "The level acronym cannot be blank caracter")
    @NotEmpty(message = "The level acronym cannot be empty")
    String acronym;
    String optionId;
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
