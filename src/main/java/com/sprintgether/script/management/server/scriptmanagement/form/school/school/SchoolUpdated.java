package com.sprintgether.script.management.server.scriptmanagement.form.school.school;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class SchoolUpdated {
    @NotNull(message = "The school id cannot be null")
    @NotBlank(message = "The school id cannot be blank caracter")
    @NotEmpty(message = "The school id cannot be empty")
    String schoolId;
    String name;
    String acronym;
    String description;
    String logoSchool;
    String ownerInstitution;
    String parentInstitution;
}
