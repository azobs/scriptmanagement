package com.sprintgether.script.management.server.scriptmanagement.commonused;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerResponse<T> {
    String errorMessage;
    String moreDetails;
    //100 when no treatment has been done, 200 for good response, 400 for bad response, 500 for exception
    int responseCode;
    T associatedObject;
}
