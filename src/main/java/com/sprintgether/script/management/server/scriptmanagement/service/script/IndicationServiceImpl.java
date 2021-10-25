package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.ContentRepository;
import com.sprintgether.script.management.server.scriptmanagement.dao.script.IndicationRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.ContentNotBelongingToException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.IndicationNotBelongingToStaffException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.IndicationNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Content;
import com.sprintgether.script.management.server.scriptmanagement.model.script.EnumContentType;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Indication;
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
public class IndicationServiceImpl implements IndicationService {

    IndicationRepository indicationRepository;
    ContentRepository contentRepository;
    StaffService staffService;

    public IndicationServiceImpl(IndicationRepository indicationRepository,
                                 ContentRepository contentRepository,
                                 StaffService staffService) {
        this.indicationRepository = indicationRepository;
        this.contentRepository = contentRepository;
        this.staffService = staffService;
    }

    @Override
    public ServerResponse<Indication> findIndicationById(String indicationId) {
        ServerResponse<Indication> srInd = new ServerResponse<>();
        indicationId = indicationId.trim();
        srInd.setMoreDetails("Tne indication has not found in the system");
        srInd.setResponseCode(ResponseCode.INDICATION_NOT_FOUND);
        Optional<Indication> optionalInd = indicationRepository.findById(indicationId);
        if(optionalInd.isPresent()){
            srInd.setResponseCode(ResponseCode.INDICATION_FOUND);
            srInd.setErrorMessage("The indication is successfully found in the system");
            srInd.setAssociatedObject(optionalInd.get());
        }
        return srInd;
    }

    @Override
    public Indication saveindication(Indication indication) {
        return indicationRepository.save(indication);
    }

    @Override
    public ServerResponse<Indication> saveIndication(String staffId, String value,
                                                     String contentType)
            throws StaffNotFoundException {
        ServerResponse<Indication> srInd = new ServerResponse<>(ResponseCode.INDICATION_NOT_CREATED);
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
            Indication indication = new Indication();
            indication.setListofContent(listofContent);
            indication.setOwnerStaff(ownerStaff);
            Indication indicationSaved = indicationRepository.save(indication);

            srInd.setResponseCode(ResponseCode.INDICATION_CREATED);
            srInd.setErrorMessage("The indication has been successfully created");
            srInd.setAssociatedObject(indicationSaved);
        } catch (IllegalArgumentException e) {
            srInd.setResponseCode(ResponseCode.EXCEPTION_ENUM_CONTENT_TYPE);
            srInd.setErrorMessage("IllegalArgumentException");
            srInd.setMoreDetails(e.getMessage());
        }
        return srInd;
    }

    @Override
    public ServerResponse<Indication> duplicateIndicationForStaff(String indicationId,
                                                                  String staffId)
            throws IndicationNotFoundException, StaffNotFoundException {
        ServerResponse<Indication> srInd = new ServerResponse<>();
        indicationId = indicationId.trim();
        staffId = staffId.trim();
        ServerResponse<Indication> srInd1 = this.findIndicationById(indicationId);
        if(srInd1.getResponseCode() != ResponseCode.INDICATION_FOUND){
            throw new IndicationNotFoundException("the indication id does not match any " +
                    "indication in the system");
        }
        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id does not match any staff in the " +
                    "system");
        }
        Indication indicationToDuplicate = srInd1.getAssociatedObject();
        Staff newOwnerStaff = srStaff.getAssociatedObject();

        Indication indication = new Indication();
        indication.setOwnerStaff(newOwnerStaff);
        indication.setListofContent(indicationToDuplicate.getListofContent());

        Indication indicationDuplicated = indicationRepository.save(indication);
        srInd.setErrorMessage("The indication has been sucessfully duplicated");
        srInd.setResponseCode(ResponseCode.INDICATION_CREATED);
        srInd.setAssociatedObject(indicationDuplicated);

        return srInd;
    }

    @Override
    public ServerResponse<Indication> addContentToIndication(String value, String contentType,
                                                             String indicationId)
            throws IndicationNotFoundException {
        ServerResponse<Indication> srInd = new ServerResponse<>();
        value = value.trim();
        contentType = contentType.trim();
        indicationId = indicationId.trim();

        ServerResponse<Indication> srInd1 = this.findIndicationById(indicationId);
        if(srInd1.getResponseCode() != ResponseCode.INDICATION_FOUND){
            throw new IndicationNotFoundException("The indication id does not match any " +
                    "indication in the system");
        }
        Indication concernedIndication = srInd1.getAssociatedObject();
        try{
            EnumContentType enumContentType = EnumContentType.valueOf(contentType.toUpperCase());

            Content content = new Content();
            content.setValue(value);
            content.setContentType(enumContentType);
            Date dateSaving = new Date();
            content.setDateHeureContent(dateSaving);
            Content contentSaved = contentRepository.save(content);
            concernedIndication.getListofContent().add(contentSaved);

            Indication indicationSaved = this.saveindication(concernedIndication);
            srInd.setResponseCode(ResponseCode.CONTENT_ADDED);
            srInd.setErrorMessage("The content has been successfully added to the indication");
            srInd.setAssociatedObject(indicationSaved);
        } catch (IllegalArgumentException e) {
            srInd.setResponseCode(ResponseCode.EXCEPTION_ENUM_CONTENT_TYPE);
            srInd.setErrorMessage("IllegalArgumentException");
            srInd.setMoreDetails(e.getMessage());
        }

        return srInd;
    }

    public Optional<Content> findContentInContentIndicationList(String contentId,
                                                                Indication indication){
        Content contentSearch = null;
        for(Content content : indication.getListofContent()){
            if(content.getId().equalsIgnoreCase(contentId)){
                contentSearch = content;
                break;
            }
        }
        return Optional.ofNullable(contentSearch);
    }

    @Override
    public ServerResponse<Indication> updateContentToIndication(String contentId, String value,
                                                                String indicationId)
            throws IndicationNotFoundException, ContentNotBelongingToException {
        ServerResponse<Indication> srInd = new ServerResponse<>(ResponseCode.CONTENT_NOT_UPDATED);
        contentId = contentId.trim();
        value = value.trim();
        indicationId = indicationId.trim();

        ServerResponse<Indication> srIndicationToUpdateContent =
                this.findIndicationById(indicationId);
        if(srIndicationToUpdateContent.getResponseCode() != ResponseCode.INDICATION_FOUND){
            throw new IndicationNotFoundException("The indication id does not match any " +
                    "indication in the system");
        }
        Indication indicationToUpdateContent = srIndicationToUpdateContent.getAssociatedObject();

        Optional<Content> optionalContent = this.findContentInContentIndicationList(contentId,
                indicationToUpdateContent);

        if(!optionalContent.isPresent()){
            throw new ContentNotBelongingToException("The content id does not belonging to indication");
        }
        Content contentToUpdate = optionalContent.get();
        contentToUpdate.setValue(value);
        contentRepository.save(contentToUpdate);

        srIndicationToUpdateContent = this.findIndicationById(indicationId);
        if(srIndicationToUpdateContent.getResponseCode() != ResponseCode.INDICATION_FOUND){
            throw new IndicationNotFoundException("The indication id does not match any " +
                    "indication in the system");
        }
        Indication indicationUpdateContent = srIndicationToUpdateContent.getAssociatedObject();

        srInd.setResponseCode(ResponseCode.CONTENT_UPDATED);
        srInd.setErrorMessage("The content has been successfully updated");
        srInd.setAssociatedObject(indicationUpdateContent);
        return srInd;
    }

    @Override
    public ServerResponse<Indication> removeContentToIndication(String contentId,
                                                                String indicationId)
            throws ContentNotBelongingToException, IndicationNotFoundException{
        ServerResponse<Indication> srInd = new ServerResponse<>(ResponseCode.CONTENT_NOT_DELETED);
        contentId = contentId.trim();
        indicationId = indicationId.trim();

        ServerResponse<Indication> srIndicationToDeleteContent =
                this.findIndicationById(indicationId);
        if(srIndicationToDeleteContent.getResponseCode() != ResponseCode.INDICATION_FOUND){
            throw new IndicationNotFoundException("The indication id does not match any " +
                    "indication in the system");
        }
        Indication indicationToDeleteContent = srIndicationToDeleteContent.getAssociatedObject();

        Optional<Content> optionalContent = this.findContentInContentIndicationList(contentId,
                indicationToDeleteContent);
        if(!optionalContent.isPresent()){
            throw new ContentNotBelongingToException("The content is not found among the list " +
                    "of content of Indication");
        }
        contentRepository.delete(optionalContent.get());

        srIndicationToDeleteContent =
                this.findIndicationById(indicationId);
        if(srIndicationToDeleteContent.getResponseCode() != ResponseCode.INDICATION_FOUND){
            throw new IndicationNotFoundException("The indication id does not match any " +
                    "indication in the system");
        }

        Indication indicationDeleteContent = srIndicationToDeleteContent.getAssociatedObject();
        srInd.setErrorMessage("The content in the indication has been removed successfully");
        srInd.setResponseCode(ResponseCode.CONTENT_DELETED);
        srInd.setAssociatedObject(indicationDeleteContent);

        return srInd;
    }

    @Override
    public ServerResponse<Indication> removeIndication(String indicationId, String staffId)
            throws IndicationNotFoundException, StaffNotFoundException,
            IndicationNotBelongingToStaffException {
        ServerResponse<Indication> srIndication1 = this.findIndicationById(indicationId);
        if(srIndication1.getResponseCode() != ResponseCode.INDICATION_FOUND){
            throw new IndicationNotFoundException("The indication id precised does not match any indication " +
                    "in the system");
        }
        ServerResponse<Staff> srStaff = staffService.findStaffById(staffId);
        if(srStaff.getResponseCode() != ResponseCode.STAFF_FOUND){
            throw new StaffNotFoundException("The staff id precised does not match any staff in the " +
                    "system");
        }
        /***
         * If we are here in the execution, this means that the srIndication1 content the
         * indication that we want to delete
         */
        Indication concernedIndication = srIndication1.getAssociatedObject();
        if(concernedIndication.getOwnerStaff().getId() != staffId){
            throw new IndicationNotBelongingToStaffException("The precised indication does not " +
                    "belonging to the precised staff in the system.");
        }
        /****
         * If we are here, this means that the indication  can be deleted without problem
         */

        return this.removeIndication(indicationId);
    }

    @Override
    public ServerResponse<Indication> removeIndication(String indicationId)
            throws IndicationNotFoundException {
        ServerResponse<Indication> srIndication = new ServerResponse<>(ResponseCode.INDICATION_NOT_DELETED);
        ServerResponse<Indication> srIndication1 = this.findIndicationById(indicationId);
        if(srIndication1.getResponseCode() != ResponseCode.INDICATION_FOUND){
            throw new IndicationNotFoundException("The indication id precised does not match any indication " +
                    "in the system");
        }
        /****
         * We are sur that the indication is found in the system
         */
        Indication indicationToRemove = srIndication1.getAssociatedObject();

        indicationRepository.delete(indicationToRemove);

        srIndication.setResponseCode(ResponseCode.INDICATION_DELETED);
        srIndication.setErrorMessage("The indication has been deleted sucessfully");
        srIndication.setAssociatedObject(indicationToRemove);

        return srIndication;
    }
}
