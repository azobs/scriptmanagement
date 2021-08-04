package com.sprintgether.script.management.server.scriptmanagement.form.program.subsection;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SubSectionTitleUpdated {
    @NotNull(message = "The sub section id cannot be null")
    @NotBlank(message = "The sub section id cannot be blank caracter")
    @NotEmpty(message = "The sub section id cannot be empty")
    String subSectionId;
    @NotNull(message = "The new sub section title cannot be null")
    @NotBlank(message = "The new sub section title cannot be blank caracter")
    @NotEmpty(message = "The new sub section title cannot be empty")
    String newSubSectionTitle;
}
