package com.sprintgether.script.management.server.scriptmanagement.form.program.subsection;

import com.sprintgether.script.management.server.scriptmanagement.model.program.EnumCoursePartType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SubSectionSaved {
    @NotNull(message = "The chapter title cannot be null")
    @NotBlank(message = "The chapter title cannot be blank caracter")
    @NotEmpty(message = "The chapter title cannot be empty")
    String title;
    @NotNull(message = "The sub section order cannot be null")
    @Min(value = 1, message = "The minimum number of chapter order is 1")
    int subSectionOrder;
    @NotNull(message = "The chapter type cannot be null it must be THEORETICAL/PRACTICAL")
    String subSectionType = EnumCoursePartType.THEORETICAL.name();
    String sectionId;
    String sectionTitle;
    String chapterTitle;
    String moduleTitle;
    String courseTitle;
    String ownerLevel;
    String ownerOption;
    String ownerDepartment;
    String ownerSchool;
}
