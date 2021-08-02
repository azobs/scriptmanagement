package com.sprintgether.script.management.server.scriptmanagement.model.script;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@NoArgsConstructor
public class Content {
    @Id
    String id;
    String value;
    EnumContentType contentType;
    /*************
     * All the content element of an element must be classified according to the
     * time of saving it. This classification will allow to display all the content
     * of an element in the order in witch there was saving in the system.
     */
    Date dateHeureContent;

    public Content(String value, EnumContentType contentType) {
        this.value = value;
        this.contentType = contentType;
        dateHeureContent = new Date();
    }

    @Override
    public String toString() {
        return "Content{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", contentType=" + contentType +
                '}';
    }
}
