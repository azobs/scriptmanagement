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
@NoArgsConstructor
public class Institution {
    @Id
    String id;
    @Indexed(unique = true)
    String name;
    String acronym;
    String description;
    String location;
    String address;
    String logoInstitution;

    public Institution(String name, String acronym, String description,
                       String location, String address) {
        this.name = name;
        this.acronym = acronym;
        this.description = description;
        this.location = location;
        this.address = address;
    }
}
