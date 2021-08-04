package com.sprintgether.script.management.server.scriptmanagement.form.program.section;

import com.sprintgether.script.management.server.scriptmanagement.model.program.EnumCoursePartType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SectionUpdated {
    @NotNull(message = "The chapter id cannot be null")
    @NotBlank(message = "The chapter id cannot be blank caracter")
    @NotEmpty(message = "The chapter id cannot be empty")
    String sectionId;
    String title;
    @Min(value = 1, message = "The minimum number of chapter order is 1")
    int sectionOrder;
    String sectionType = EnumCoursePartType.THEORETICAL.name();
    String chapterTitle;
    String moduleTitle;
    String courseTitle;
    String ownerLevel;
    String ownerOption;
    String ownerDepartment;
    String ownerSchool;
}
