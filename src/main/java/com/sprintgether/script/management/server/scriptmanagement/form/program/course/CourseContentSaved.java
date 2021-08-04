package com.sprintgether.script.management.server.scriptmanagement.form.program.course;

import com.sprintgether.script.management.server.scriptmanagement.model.script.EnumContentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CourseContentSaved {
    String courseId;
    String courseTitle;
    String ownerLevel;
    String ownerOption;
    String ownerDepartment;
    String ownerSchool;

    @NotNull(message = "The content value cannot be null")
    @NotBlank(message = "The content value cannot be blank caracter")
    @NotEmpty(message = "The content value cannot be empty")
    String value;
    @NotNull(message = "The content type cannot be null")
    @NotBlank(message = "The content type cannot be blank caracter")
    @NotEmpty(message = "The content  type cannot be empty")
    String contentType = EnumContentType.TEXT.name();

}
