package com.sprintgether.script.management.server.scriptmanagement.form.program.chapter;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ChapterTitleUpdated {
    @NotNull(message = "The module id cannot be null")
    @NotBlank(message = "The module id cannot be blank caracter")
    @NotEmpty(message = "The module id cannot be empty")
    String chapterId;
    @NotNull(message = "The new module title cannot be null")
    @NotBlank(message = "The new module title cannot be blank caracter")
    @NotEmpty(message = "The new module title cannot be empty")
    String newChapterTitle;
}
