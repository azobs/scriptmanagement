package com.sprintgether.script.management.server.scriptmanagement.form.school.institution;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class InstitutionUpdated {
    @NotNull(message = "The institution name cannot be null")
    @NotBlank(message = "The institution name cannot be blank caracter")
    @NotEmpty(message = "The institution name cannot be empty")
    String institutionId;
    String name;
    String acronym;
    String description;
    String location;
    String address;
    String logoInstitution;
}
