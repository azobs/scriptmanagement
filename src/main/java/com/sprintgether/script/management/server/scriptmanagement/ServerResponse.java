package com.sprintgether.script.management.server.scriptmanagement;

import com.sprintgether.script.management.server.scriptmanagement.EnumResponseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerResponse<T> {
    String errorMessage;
    String moreDetails;
    EnumResponseType responseType;
    T associatedObject;
}
