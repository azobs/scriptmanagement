package com.sprintgether.script.management.server.scriptmanagement.form.School;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class InstitutionSchoolForm {
    @NotNull(message = "The institutionName cannot be null")
    @NotBlank(message = "The institutionName cannot be blank caracter")
    @NotEmpty(message = "The institutionName cannot be empty")
    @Size(min=2, max=50, message="The institutionName must have at least 2 caracteres and at most 50")
    String institutionName;
    @NotNull(message = "The schoolName cannot be null")
    @NotBlank(message = "The schoolName cannot be blank caracter")
    @NotEmpty(message = "The schoolName cannot be empty")
    @Size(min=2, max=50, message="The schoolName must have at least 2 caracteres and at most 50")
    String schoolName;
}
