package com.sprintgether.script.management.server.scriptmanagement.form.school.option;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class OptionNameUpdated {
    @NotNull(message = "The option id cannot be null")
    @NotBlank(message = "The option id cannot be blank caracter")
    @NotEmpty(message = "The option id cannot be empty")
    String optionId;
    @NotNull(message = "The new option name cannot be null")
    @NotBlank(message = "The new option name cannot be blank caracter")
    @NotEmpty(message = "The new option name cannot be empty")
    String newOptionName;
}
