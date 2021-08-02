package com.sprintgether.script.management.server.scriptmanagement.form.school.chapter;

import com.sprintgether.script.management.server.scriptmanagement.model.school.EnumCoursePartType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ChapterSaved {
    @NotNull(message = "The chapter title cannot be null")
    @NotBlank(message = "The chapter title cannot be blank caracter")
    @NotEmpty(message = "The chapter title cannot be empty")
    String title;
    @NotNull(message = "The chapter order cannot be null")
    @Min(value = 1, message = "The minimum number of chapter order is 1")
    int chapterOrder;
    @NotNull(message = "The chapter type cannot be null it must be THEORETICAL/PRACTICAL")
    String chapterType = EnumCoursePartType.THEORETICAL.name();
    String moduleId;
    String chapterTitle;
    String moduleTitle;
    String courseTitle;
    String ownerLevel;
    String ownerOption;
    String ownerDepartment;
    String ownerSchool;
}
