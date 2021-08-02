package com.sprintgether.script.management.server.scriptmanagement.form.school.school;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class SchoolSaved {
    @NotNull(message = "The school name cannot be null")
    @NotBlank(message = "The school name cannot be blank caracter")
    @NotEmpty(message = "The school name cannot be empty")
    @Size(min=2, max=50, message="The school name must have at least 2 caracters " +
            "and at most 50")
    String name;
    @NotNull(message = "The school acronym cannot be null")
    @NotBlank(message = "The school acronym cannot be blank caracter")
    @NotEmpty(message = "The school acronym cannot be empty")
    @Size(min=2, max=50, message="The school acronym must have at least 2 caracters " +
            "and at most 50")
    String acronym;
    String description;
    String logoSchool;
    String ownerInstitution;
    String parentInstitution;
}
