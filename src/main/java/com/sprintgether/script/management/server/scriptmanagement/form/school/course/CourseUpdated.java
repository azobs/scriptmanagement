package com.sprintgether.script.management.server.scriptmanagement.form.school.course;

import com.sprintgether.script.management.server.scriptmanagement.model.school.EnumCoursePartType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CourseUpdated {
    @NotNull(message = "The course id cannot be null")
    @NotBlank(message = "The course id cannot be blank caracter")
    @NotEmpty(message = "The course id cannot be empty")
    String courseId;
    String title;
    String courseCode;
    @Min(value = 1, message = "The minimum number of credit is 1")
    int nbreCredit;
    String courseType = EnumCoursePartType.THEORETICAL.name();
    String ownerLevel;
    String ownerOption;
    String ownerDepartment;
    String ownerSchool;
}
