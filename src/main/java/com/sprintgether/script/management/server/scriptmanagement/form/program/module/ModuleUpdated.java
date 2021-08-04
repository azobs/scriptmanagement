package com.sprintgether.script.management.server.scriptmanagement.form.program.module;

import com.sprintgether.script.management.server.scriptmanagement.model.program.EnumCoursePartType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
public class ModuleUpdated {
    String moduleId;
    String title;
    @Min(value = 1, message = "The minimum number of module order is 1")
    int moduleOrder;
    String moduleType = EnumCoursePartType.THEORETICAL.name();
    String courseTitle;
    String ownerLevel;
    String ownerOption;
    String ownerDepartment;
    String ownerSchool;
}
