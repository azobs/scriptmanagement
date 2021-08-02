package com.sprintgether.script.management.server.scriptmanagement.form.school.module;

import com.sprintgether.script.management.server.scriptmanagement.model.school.EnumCoursePartType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ModuleSaved {
    @NotNull(message = "The course title cannot be null")
    @NotBlank(message = "The course title cannot be blank caracter")
    @NotEmpty(message = "The course title cannot be empty")
    String title;
    @NotNull(message = "The level name cannot be null")
    @Min(value = 1, message = "The minimum number of credit is 1")
    int moduleOrder;
    @NotNull(message = "The course type cannot be null THEORETICAL/PRACTICAL")
    String moduleType = EnumCoursePartType.THEORETICAL.name();
    String courseId;
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
}
