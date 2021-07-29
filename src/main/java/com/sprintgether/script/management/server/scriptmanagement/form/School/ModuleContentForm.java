package com.sprintgether.script.management.server.scriptmanagement.form.School;

import com.sprintgether.script.management.server.scriptmanagement.model.script.EnumContentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ModuleContentForm {
    @NotNull(message = "The module title cannot be null")
    @NotBlank(message = "The module title cannot be blank caracter")
    @NotEmpty(message = "The module title cannot be empty")
    String moduleTitle;
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

    String contentId;
    @NotNull(message = "The content value cannot be null")
    @NotBlank(message = "The content value cannot be blank caracter")
    @NotEmpty(message = "The content value cannot be empty")
    String value;
    @NotNull(message = "The content type cannot be null")
    @NotBlank(message = "The content type cannot be blank caracter")
    @NotEmpty(message = "The content  type cannot be empty")
    String contentType = EnumContentType.TEXT.name();
}
