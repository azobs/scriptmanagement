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
public class CourseSaved {
    @NotNull(message = "The course title cannot be null")
    @NotBlank(message = "The course title cannot be blank caracter")
    @NotEmpty(message = "The course title cannot be empty")
    String title;
    @NotNull(message = "The course code cannot be null")
    @NotBlank(message = "The course code cannot be blank caracter")
    @NotEmpty(message = "The course code cannot be empty")
    String courseCode;
    @NotNull(message = "The level name cannot be null")
    @Min(value = 1, message = "The minimum number of credit is 1")
    int nbreCredit;
    @NotNull(message = "The course type cannot be null THEORETICAL/PRACTICAL")
    String courseType = EnumCoursePartType.THEORETICAL.name();
    String levelId;
    String ownerLevel;
    String ownerOption;
    String ownerDepartment;
    String ownerSchool;
    String courseOutlineTitle;

}
