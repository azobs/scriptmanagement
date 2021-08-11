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
public class Indication {
    @Id
    String id;
    @DBRef
    List<Content> listofContent = new ArrayList<>();
    @DBRef
    Staff ownerStaff;

    public Indication(List<Content> listofContent) {
        this.listofContent = listofContent;
    }

    @Override
    public String toString() {
        return "Indication{" +
                "id='" + id + '\'' +
                ", content=" + listofContent +
                '}';
    }
}
