package com.sprintgether.script.management.server.scriptmanagement.form.school.school;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class SchoolNameUpdated {
    @NotNull(message = "The school id cannot be null")
    @NotBlank(message = "The school id cannot be blank caracter")
    @NotEmpty(message = "The school id cannot be empty")
    String schoolId;
    @NotNull(message = "The new school name cannot be null")
    @NotBlank(message = "The new school name cannot be blank caracter")
    @NotEmpty(message = "The new school name cannot be empty")
    @Size(min=2, max=50, message="The new school name must have at least 2 caracters " +
            "and at most 50")
    String newSchoolName;
}
