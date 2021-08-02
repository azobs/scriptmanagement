package com.sprintgether.script.management.server.scriptmanagement.form.school.level;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class LevelUpdated {
    @NotNull(message = "The level id cannot be null")
    @NotBlank(message = "The level id cannot be blank caracter")
    @NotEmpty(message = "The level id cannot be empty")
    String levelId;
    String name;
    String acronym;
    String ownerOption;
    String ownerDepartment;
    String ownerSchool;
    String emailClassPerfect;
}
