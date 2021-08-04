package com.sprintgether.script.management.server.scriptmanagement.form.program.course;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CourseOutlineUpdated {
    String courseId;
    String courseTitle;
    String ownerLevel;
    String ownerOption;
    String ownerDepartment;
    String ownerSchool;
    @NotNull(message = "The course outline title cannot be null")
    @NotBlank(message = "The course outline title cannot be blank caracter")
    @NotEmpty(message = "The course outline title cannot be empty")
    String courseOutlineTitle;
}
