package com.sprintgether.script.management.server.scriptmanagement.form.school.course;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CourseStaff {
    String courseId;
    String courseTitle;
    String ownerLevel;
    String ownerOption;
    String ownerDepartment;
    String ownerSchool;
    @NotNull(message = "The lecturer email cannot be null")
    @NotBlank(message = "The lecturer email cannot be blank caracter")
    @NotEmpty(message = "The lecturer email cannot be empty")
    @Email(message = "The specified value does not respect email format")
    String lecturerEmail;
}
