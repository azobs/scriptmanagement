package com.sprintgether.script.management.server.scriptmanagement.commonused;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ServerResponse<T> {
    String errorMessage;
    String moreDetails;
    int responseCode;
    T associatedObject;

    public ServerResponse() {
        this.errorMessage = "";
        this.moreDetails = "";
        this.responseCode = ResponseCode.INITIALISATION;
        this.associatedObject = null;
    }
    public ServerResponse(int responseCode) {
        this.errorMessage = "";
        this.moreDetails = "";
        this.responseCode = responseCode;
        this.associatedObject = null;
    }
}
