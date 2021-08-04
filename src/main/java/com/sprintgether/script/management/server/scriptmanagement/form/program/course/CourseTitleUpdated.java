package com.sprintgether.script.management.server.scriptmanagement.form.program.course;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CourseTitleUpdated {
    @NotNull(message = "The course id cannot be null")
    @NotBlank(message = "The course id cannot be blank caracter")
    @NotEmpty(message = "The course id cannot be empty")
    String courseId;
    @NotNull(message = "The new course title cannot be null")
    @NotBlank(message = "The new course title cannot be blank caracter")
    @NotEmpty(message = "The new course title cannot be empty")
    String newCourseTitle;
}
