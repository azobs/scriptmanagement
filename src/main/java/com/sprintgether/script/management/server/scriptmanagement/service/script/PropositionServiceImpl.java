package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.ContentRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.PropositionRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.ContentNotBelongingToException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.PropositionNotBelongingToStaffException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.PropositionNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Content;
import com.sprintgether.script.management.server.scriptmanagement.model.script.EnumContentType;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Proposition;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.service.user.StaffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PropositionServiceImpl implements PropositionService {
    PropositionRepository propositionRepository;
    ContentRepository contentRepository;
    StaffService staffService;

    public PropositionServiceImpl(PropositionRepository propositionRepository,
                                  ContentRepository contentRepository,
                                  StaffService staffService) {
        this.propositionRepository = propositionRepository;
        this.contentRepository = contentRepository;
        this.staffService = staffService;
    }

    @Override
    public ServerResponse<Proposition> findPropositionById(String propositionId) {
        ServerResponse<Proposition> srProp = new ServerResponse<>();
        propositionId = propositionId.trim();
        srProp.setErrorMessage("The proposition is not found in the system");
        srProp.setResponseCode(ResponseCode.PROPOSITION_NOT_FOUND);

        Optional<Proposition> optionalProp = propositionRepository.findById(propositionId);
        if(optionalProp.isPresent()){
            srProp.setErrorMessage("The proposition is successfully found in the system");
            srProp.setResponseCode(ResponseCode.PROPOSITION_FOUND);
            srProp.setAssociatedObject(optionalProp.get());
        }

        return srProp;
    }

    @Override
    public Proposition saveProposition(Proposition proposition) {
        return propositionRepository.save(proposition);
    }

    @Override
    public ServerResponse<Proposition> saveProposition(String staffId, String value,
                                                       String contentType, boolean valid)
            throws StaffNotFoundException {
        ServerResponse<Proposition> srProp = new ServerResponse<>();
        staffId = staffId.trim();
        value = value.trim();
        contentType = contentType.trim();

        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff " +
                    "in the system");
        }
        try{
            EnumContentType enumContentType = EnumContentType.valueOf(
                    contentType.toUpperCase());
            List<Content> listofContent = new ArrayList<>();
            /**
             * Creation of the first content
             */
            Content content = new Content();
            content.setDateHeureContent(new Date());
            content.setValue(value);
            content.setContentType(enumContentType);
            Content contentSaved = contentRepository.save(content);

            listofContent.add(contentSaved);

            Staff ownerStaff = srStaff.getAssociatedObject();
            Proposition propToSaved = new Proposition();
            propToSaved.setListofContent(listofContent);
            propToSaved.setOwnerStaff(ownerStaff);
            propToSaved.setValid(valid);
            Proposition propSaved = propositionRepository.save(propToSaved);

            srProp.setErrorMessage("The proposition has been created successfully");
            srProp.setResponseCode(ResponseCode.PROPOSITION_CREATED);
            srProp.setAssociatedObject(propSaved);
        } catch (IllegalArgumentException e) {
            srProp.setResponseCode(ResponseCode.EXCEPTION_ENUM_CONTENT_TYPE);
            srProp.setErrorMessage("IllegalArgumentException");
            srProp.setMoreDetails(e.getMessage());
        }

        return srProp;
    }

    @Override
    public ServerResponse<Proposition> duplicatePropositionForStaff(String propositionId,
                                                                    String staffId)
            throws StaffNotFoundException, PropositionNotFoundException {
        ServerResponse<Proposition> srProp = new ServerResponse<>();
        propositionId = propositionId.trim();
        staffId = staffId.trim();
        ServerResponse<Proposition> srPropToDuplicate = this.findPropositionById(propositionId);
        if(srPropToDuplicate.getResponseCode() != ResponseCode.PROPOSITION_FOUND){
            throw new PropositionNotFoundException("The proposition id does not match any proposition " +
                    "in the system");
        }
        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id does not match any staff in the " +
                    "system");
        }
        Staff newOwnerStaff = srStaff.getAssociatedObject();
        Proposition propToDuplicate = srPropToDuplicate.getAssociatedObject();

        Proposition propCopy = new Proposition();
        propCopy.setValid(propToDuplicate.isValid());
        propCopy.setOwnerStaff(newOwnerStaff);
        propCopy.setListofContent(propToDuplicate.getListofContent());

        Proposition propDuplicated = propositionRepository.save(propCopy);
        srProp.setErrorMessage("The proposition has been sucessfully duplicated");
        srProp.setResponseCode(ResponseCode.PROPOSITION_CREATED);
        srProp.setAssociatedObject(propDuplicated);

        return srProp;
    }

    @Override
    public ServerResponse<Proposition> updateProposition(String propositionId, boolean valid)
            throws PropositionNotFoundException {
        ServerResponse<Proposition> srProp = new ServerResponse<>();
        propositionId = propositionId.trim();
        ServerResponse<Proposition> srProp1 = this.findPropositionById(propositionId);
        if(srProp1.getResponseCode() != ResponseCode.PROPOSITION_FOUND){
            throw new PropositionNotFoundException("The proposition id does not match any proposition " +
                    "in the system");
        }
        Proposition propToUpdate = srProp1.getAssociatedObject();
        propToUpdate.setValid(valid);
        Proposition propUpdated = this.saveProposition(propToUpdate);

        srProp.setErrorMessage("The proposition has been sucessfully updated");
        srProp.setResponseCode(ResponseCode.PROPOSITION_UPDATED);
        srProp.setAssociatedObject(propUpdated);
        return srProp;
    }

    @Override
    public boolean isPropositionValid(String propositionId)
            throws PropositionNotFoundException {
        ServerResponse<Proposition> srProp1 = this.findPropositionById(propositionId);
        if(srProp1.getResponseCode() != ResponseCode.PROPOSITION_FOUND){
            throw new PropositionNotFoundException("The proposition id does not match any proposition " +
                    "in the system");
        }
        Proposition concernedProp = srProp1.getAssociatedObject();

        return concernedProp.isValid();
    }

    @Override
    public ServerResponse<Proposition> addContentToProposition(String value, String contentType,
                                                               String propositionId)
            throws PropositionNotFoundException {
        ServerResponse<Proposition> srProp = new ServerResponse<>();
        value = value.trim();
        contentType = contentType.trim();
        propositionId = propositionId.trim();
        ServerResponse<Proposition> srProp1 = this.findPropositionById(propositionId);
        if(srProp1.getResponseCode() != ResponseCode.PROPOSITION_FOUND){
            throw new PropositionNotFoundException("The proposition id does not match any proposition " +
                    "in the system");
        }
        Proposition concernedProp = srProp1.getAssociatedObject();

        try {
            EnumContentType enumContentType = EnumContentType.valueOf(contentType.toUpperCase());

            Content content = new Content();
            content.setValue(value);
            content.setContentType(enumContentType);
            Date dateSaving = new Date();
            content.setDateHeureContent(dateSaving);
            Content contentSaved = contentRepository.save(content);
            concernedProp.getListofContent().add(contentSaved);

            Proposition propSaved = this.saveProposition(concernedProp);
            srProp.setResponseCode(ResponseCode.CONTENT_ADDED);
            srProp.setErrorMessage("The content has been successfully added to the proposition");
            srProp.setAssociatedObject(propSaved);

        } catch (IllegalArgumentException e) {
            srProp.setResponseCode(ResponseCode.EXCEPTION_ENUM_CONTENT_TYPE);
            srProp.setErrorMessage("IllegalArgumentException");
            srProp.setMoreDetails(e.getMessage());
        }

        return srProp;
    }

    public Optional<Content> findContentInContentPropositionList(String contentId,
                                                                Proposition proposition){
        Content contentSearch = null;
        for(Content content : proposition.getListofContent()){
            if(content.getId().equalsIgnoreCase(contentId)){
                contentSearch = content;
                break;
            }
        }
        return Optional.ofNullable(contentSearch);
    }

    @Override
    public ServerResponse<Proposition> updateContentToProposition(String contentId, String value,
                                                                  String propositionId)
            throws PropositionNotFoundException, ContentNotBelongingToException {
        ServerResponse<Proposition> srProp = new ServerResponse<>();
        contentId = contentId.trim();
        value = value.trim();
        propositionId = propositionId.trim();

        ServerResponse<Proposition> srPropToUpdateContent = this.findPropositionById(propositionId);
        if(srPropToUpdateContent.getResponseCode() != ResponseCode.PROPOSITION_FOUND){
            throw new PropositionNotFoundException("the proposition id does not math any proposition " +
                    "in the system");
        }
        Proposition propToUpdateContent = srPropToUpdateContent.getAssociatedObject();

        Optional<Content> optionalContentInProposition = this.findContentInContentPropositionList(contentId,
                propToUpdateContent);
        if(!optionalContentInProposition.isPresent()){
            throw new ContentNotBelongingToException("The content id does not exist in the propostion");
        }
        Content contentToUpdate = optionalContentInProposition.get();
        contentToUpdate.setValue(value);
        contentRepository.save(contentToUpdate);

        srPropToUpdateContent = this.findPropositionById(propositionId);
        if(srPropToUpdateContent.getResponseCode() != ResponseCode.PROPOSITION_FOUND){
            throw new PropositionNotFoundException("the proposition id does not math any proposition " +
                    "in the system");
        }
        Proposition propUpdateContent = srPropToUpdateContent.getAssociatedObject();

        srProp.setResponseCode(ResponseCode.CONTENT_UPDATED);
        srProp.setErrorMessage("The content has been successfully updated");
        srProp.setAssociatedObject(propUpdateContent);

        return srProp;
    }

    @Override
    public ServerResponse<Proposition> removeContentToProposition(String contentId,
                                                                  String propositionId)
            throws PropositionNotFoundException, ContentNotBelongingToException {
        contentId = contentId.trim();
        propositionId = propositionId.trim();
        ServerResponse<Proposition> srProp = new ServerResponse<>();
        ServerResponse<Proposition> srPropToDeleteContent =
                this.findPropositionById(propositionId);
        if(srPropToDeleteContent.getResponseCode() != ResponseCode.PROPOSITION_FOUND){
            throw new PropositionNotFoundException("The proposition id does not match any " +
                    "proposition in the system");
        }
        Proposition propToDeleteContent = srPropToDeleteContent.getAssociatedObject();

        Optional<Content> optionalContent = this.findContentInContentPropositionList(contentId,
                propToDeleteContent);
        if(!optionalContent.isPresent()){
            throw new ContentNotBelongingToException("The content is not found among the list " +
                    "of content of Indication");
        }
        contentRepository.delete(optionalContent.get());

        srPropToDeleteContent = this.findPropositionById(propositionId);
        if(srPropToDeleteContent.getResponseCode() != ResponseCode.PROPOSITION_FOUND){
            throw new PropositionNotFoundException("The proposition id does not match any " +
                    "proposition in the system  after deleting his content");
        }
        Proposition propDeleteContent = srPropToDeleteContent.getAssociatedObject();

        srProp.setResponseCode(ResponseCode.CONTENT_DELETED);
        srProp.setErrorMessage("The content has been successfully deleted in the proposition");
        srProp.setAssociatedObject(propDeleteContent);

        return srProp;
    }

    @Override
    public ServerResponse<Proposition> removeProposition(String propositionId, String staffId)
            throws PropositionNotFoundException, StaffNotFoundException,
            PropositionNotBelongingToStaffException {
        return null;
    }
}
