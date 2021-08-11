package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.ContentNotBelongingToException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.IndicationNotBelongingToStaffException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.IndicationNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Indication;


public interface IndicationService {
    ServerResponse<Indication> findIndicationById(String indicationId);


    /****
     * To save an indication we must have the data of the first content
     * @param staffId
     * @param value
     * @param contentType
     * @return
     */
    ServerResponse<Indication> saveIndication(String staffId, String value, String contentType)
            throws StaffNotFoundException;

    ServerResponse<Indication> duplicateIndicationForStaff(String indicationId, String staffId)
            throws IndicationNotFoundException, StaffNotFoundException;

    ServerResponse<Indication> addContentToIndication(String value, String contentType,
                                                      String indicationId)
            throws IndicationNotFoundException;

    ServerResponse<Indication> updateContentToIndication(String contentId, String value,
                                                      String indicationId)
            throws ContentNotFoundException, IndicationNotFoundException, ContentNotBelongingToException;

    ServerResponse<Indication> removeContentToIndication(String contentId, String indicationId)
            throws ContentNotFoundException, IndicationNotFoundException, ContentNotBelongingToException;

    ServerResponse<Indication> removeIndication(String indicationId, String staffId)
            throws IndicationNotFoundException, StaffNotFoundException, IndicationNotBelongingToStaffException;
}
