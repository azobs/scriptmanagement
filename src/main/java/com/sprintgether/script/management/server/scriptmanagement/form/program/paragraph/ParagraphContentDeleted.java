package com.sprintgether.script.management.server.scriptmanagement.form.program.paragraph;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ParagraphContentDeleted {
    String paragraphId;
    String paragraphTitle;
    String subSectionTitle;
    String sectionTitle;
    String chapterTitle;
    String moduleTitle;
    String courseTitle;
    String ownerLevel;
    String ownerOption;
    String ownerDepartment;
    String ownerSchool;
    @NotNull(message = "The content id cannot be null")
    @NotBlank(message = "The content id cannot be blank caracter")
    @NotEmpty(message = "The content id cannot be empty")
    String contentId;
}
