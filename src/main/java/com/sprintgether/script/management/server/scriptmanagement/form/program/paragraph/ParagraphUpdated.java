package com.sprintgether.script.management.server.scriptmanagement.form.program.paragraph;

import com.sprintgether.script.management.server.scriptmanagement.model.program.EnumCoursePartType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ParagraphUpdated {
    @NotNull(message = "The paragraph id cannot be null")
    @NotBlank(message = "The paragraph id cannot be blank caracter")
    @NotEmpty(message = "The paragraph id cannot be empty")
    String paragraphId;
    String title;
    @Min(value = 1, message = "The minimum number of paragraph order is 1")
    int paragraphOrder;
    String paragraphType = EnumCoursePartType.THEORETICAL.name();
    String subSectionTitle;
    String sectionTitle;
    String chapterTitle;
    String moduleTitle;
    String courseTitle;
    String ownerLevel;
    String ownerOption;
    String ownerDepartment;
    String ownerSchool;
}
