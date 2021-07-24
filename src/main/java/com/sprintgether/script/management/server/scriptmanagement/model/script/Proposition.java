package com.sprintgether.script.management.server.scriptmanagement.model.script;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@NoArgsConstructor
public class Proposition {
    @Id
    String id;
    Boolean valid;
    @DBRef
    List<Content> listofContent = new ArrayList<>();

    public Proposition(Boolean valid, List<Content> listofContent) {
        this.valid = valid;
        this.listofContent = listofContent;
    }

    @Override
    public String toString() {
        return "Proposition{" +
                "id='" + id + '\'' +
                ", valid=" + valid +
                ", content=" + listofContent +
                '}';
    }
}
