package com.sprintgether.script.management.server.scriptmanagement.form.School;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CourseForm {
    @NotNull(message = "The course title cannot be null")
    @NotBlank(message = "The course title cannot be blank caracter")
    @NotEmpty(message = "The course title cannot be empty")
    String title;
    @NotNull(message = "The course code cannot be null")
    @NotBlank(message = "The course code cannot be blank caracter")
    @NotEmpty(message = "The course code cannot be empty")
    String courseCode;
    @NotNull(message = "The level name cannot be null")
    @NotBlank(message = "The level name cannot be blank caracter")
    @NotEmpty(message = "The level name cannot be empty")
    String levelName;
    @NotNull(message = "The level name cannot be null")
    @Min(value = 1, message = "The minimum number of credit is 1")
    int nbreCredit;
    @NotNull(message = "The option name cannot be null")
    @NotBlank(message = "The option name cannot be blank caracter")
    @NotEmpty(message = "The option name cannot be empty")
    String optionName;
    @NotNull(message = "The department name cannot be null")
    @NotBlank(message = "The department name cannot be blank caracter")
    @NotEmpty(message = "The department name cannot be empty")
    String departmentName;
    @NotNull(message = "The school name cannot be null")
    @NotBlank(message = "The school name cannot be blank caracter")
    @NotEmpty(message = "The school name cannot be empty")
    String schoolName;
    String courseOutlineTitle;
}
