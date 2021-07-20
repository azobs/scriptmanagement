package com.sprintgether.script.management.server.scriptmanagement.form.School;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class InstitutionForm {
    @NotNull(message = "The institution name cannot be null")
    @NotBlank(message = "The institution name cannot be blank caracter")
    @NotEmpty(message = "The institution name cannot be empty")
    @Size(min=2, max=50, message="The institution name must have at least 2 caracters and at most 50")
    String name;
    @NotNull(message = "The institution acronym cannot be null")
    @NotBlank(message = "The institution acronym cannot be blank caracter")
    @NotEmpty(message = "The institution acronym cannot be empty")
    @Size(min=2, max=50, message="The institution acronym must have at least 2 caracters and at most 50")
    String acronym;
    String description;
    String location;
    String address;
    String logoInstitution;
}
