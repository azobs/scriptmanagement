package com.sprintgether.script.management.server.scriptmanagement.form.program.paragraph;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ParagraphTitleUpdated {
    @NotNull(message = "The paragraph id cannot be null")
    @NotBlank(message = "The paragraph id cannot be blank caracter")
    @NotEmpty(message = "The paragraph id cannot be empty")
    String paragraphId;
    @NotNull(message = "The new paragraph title cannot be null")
    @NotBlank(message = "The new paragraph title cannot be blank caracter")
    @NotEmpty(message = "The new paragraph title cannot be empty")
    String newParagraphTitle;
}
