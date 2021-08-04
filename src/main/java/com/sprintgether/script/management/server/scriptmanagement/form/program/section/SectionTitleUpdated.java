package com.sprintgether.script.management.server.scriptmanagement.form.program.section;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SectionTitleUpdated {
    @NotNull(message = "The section id cannot be null")
    @NotBlank(message = "The section id cannot be blank caracter")
    @NotEmpty(message = "The section id cannot be empty")
    String sectionId;
    @NotNull(message = "The new section title cannot be null")
    @NotBlank(message = "The new section title cannot be blank caracter")
    @NotEmpty(message = "The new section title cannot be empty")
    String newSectionTitle;
}
