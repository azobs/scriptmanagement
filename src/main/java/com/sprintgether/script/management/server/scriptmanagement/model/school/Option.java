package com.sprintgether.script.management.server.scriptmanagement.model.school;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Option {
    @Id
    String id;
    String name;
    String acronym;
    String description;
    @DBRef
    Department ownerDepartment;

    public Option(String name, String acronym, String description, Department ownerDepartment) {
        this.name = name;
        this.acronym = acronym;
        this.description = description;
        this.ownerDepartment = ownerDepartment;
    }
}
