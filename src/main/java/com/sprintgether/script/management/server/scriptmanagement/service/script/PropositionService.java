package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.ContentNotBelongingToException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.PropositionNotBelongingToStaffException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.PropositionNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Proposition;

public interface PropositionService {
    ServerResponse<Proposition> findPropositionById(String propositionId);

    Proposition saveProposition(Proposition proposition);
    ServerResponse<Proposition> saveProposition(String staffId, String value, String contentType,
                                                boolean valid) throws StaffNotFoundException;

    ServerResponse<Proposition> duplicatePropositionForStaff(String propositionId, String staffId)
            throws StaffNotFoundException, PropositionNotFoundException;

    ServerResponse<Proposition> updateProposition(String propositionId, boolean valid)
            throws PropositionNotFoundException;

    ServerResponse<Proposition> removeProposition(String propositionId, String staffId)
            throws PropositionNotFoundException, StaffNotFoundException,
            PropositionNotBelongingToStaffException;

    boolean isPropositionValid(String propositionId) throws PropositionNotFoundException;

    ServerResponse<Proposition> addContentToProposition(String value, String contentType,
                                                        String propositionId)
            throws PropositionNotFoundException;

    ServerResponse<Proposition> updateContentToProposition(String contentId, String value,
                                                        String propositionId)
            throws PropositionNotFoundException, ContentNotBelongingToException;

    ServerResponse<Proposition> removeContentToProposition(String contentId,
                                                        String propositionId)
            throws PropositionNotFoundException, ContentNotBelongingToException;
}
