package com.sprintgether.script.management.server.scriptmanagement.form.school.option;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class OptionUpdated {
    @NotNull(message = "The option id cannot be null")
    @NotBlank(message = "The option id cannot be blank caracter")
    @NotEmpty(message = "The option id cannot be empty")
    String optionId;
    String name;
    String acronym;
    String description;
    String ownerDepartment;
    String ownerSchool;
}
