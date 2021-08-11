package com.sprintgether.script.management.server.scriptmanagement.model.script;

import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
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
    boolean valid;
    @DBRef
    Staff ownerStaff;
    @DBRef
    List<Content> listofContent = new ArrayList<>();

    public Proposition(boolean valid, List<Content> listofContent) {
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
