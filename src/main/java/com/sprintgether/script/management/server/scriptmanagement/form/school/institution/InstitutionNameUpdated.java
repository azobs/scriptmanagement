package com.sprintgether.script.management.server.scriptmanagement.form.school.institution;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class InstitutionNameUpdated {
    @NotNull(message = "The institution id cannot be null")
    @NotBlank(message = "The institution id cannot be blank caracter")
    @NotEmpty(message = "The institution id cannot be empty")
    String institutionId;
    @NotNull(message = "The new institution name cannot be null")
    @NotBlank(message = "The new institution name cannot be blank caracter")
    @NotEmpty(message = "The new institution name cannot be empty")
    String newInstitutionName;
}
