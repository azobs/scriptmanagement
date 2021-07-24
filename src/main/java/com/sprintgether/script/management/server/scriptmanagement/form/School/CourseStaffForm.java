package com.sprintgether.script.management.server.scriptmanagement.form.School;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CourseStaffForm {
    @NotNull(message = "The course title cannot be null")
    @NotBlank(message = "The course title cannot be blank caracter")
    @NotEmpty(message = "The course title cannot be empty")
    String courseTitle;
    @NotNull(message = "The level name cannot be null")
    @NotBlank(message = "The level name cannot be blank caracter")
    @NotEmpty(message = "The level name cannot be empty")
    String ownerLevel;
    @NotNull(message = "The option name cannot be null")
    @NotBlank(message = "The option name cannot be blank caracter")
    @NotEmpty(message = "The option name cannot be empty")
    String ownerOption;
    @NotNull(message = "The department name cannot be null")
    @NotBlank(message = "The department name cannot be blank caracter")
    @NotEmpty(message = "The department name cannot be empty")
    String ownerDepartment;
    @NotNull(message = "The school name cannot be null")
    @NotBlank(message = "The school name cannot be blank caracter")
    @NotEmpty(message = "The school name cannot be empty")
    String ownerSchool;
    @NotNull(message = "The lecturer email cannot be null")
    @NotBlank(message = "The lecturer email cannot be blank caracter")
    @NotEmpty(message = "The lecturer email cannot be empty")
    @Email(message = "The specified value does not respect email format")
    String lecturerEmail;
}
