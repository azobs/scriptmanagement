package com.sprintgether.script.management.server.scriptmanagement.service.script;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.commonused.ContentNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.ContentNotBelongingToException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.IndicationAlreadyBelongingToStaffException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.IndicationNotBelongingToStaffException;
import com.sprintgether.script.management.server.scriptmanagement.exception.script.IndicationNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.exception.user.StaffNotFoundException;
import com.sprintgether.script.management.server.scriptmanagement.model.script.Indication;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;


public interface IndicationService {
    /***********************************************************************
     * Retunr in the ServerResponse object the indication associated to the
     * indicationId passed in parameter. If the indicationId does not match any
     * Indication object in the system, the ResponseCode must indicate it.
     * @param indicationId
     * @return
     */
    ServerResponse<Indication> findIndicationById(String indicationId);

    Indication saveindication(Indication indication);

    /******************************************************************************
     * To save an indication we must have the data of the first content
     * And the ownerStaffId. That is why the method throws StaffNotFoundException
     * because the staffId precised can be wrong or cannot match any Staff
     * in the system.
     * @param staffId
     * @param value
     * @param contentType
     * @param staffId
     * @param value
     * @param contentType
     * @return
     * @throws StaffNotFoundException
     */
    ServerResponse<Indication> saveIndication(String staffId, String value, String contentType)
            throws StaffNotFoundException;

    /*******************************************************************************************
     *This method can be used by an user who have permission to duplicate question, problem or
     * paper. In fact, it is not possible to personalize somebody's question, problem or paper.
     * But to achieve it, it is possible to duplicate the object to have a copy that belonging
     * to you. After it, the copy can be update according to your taste.
     * The duplication include all the encapsulated object like Content object.
     * @param indicationId
     * @param staffId
     * @return
     * @throws IndicationNotFoundException
     * @throws StaffNotFoundException
     */
    ServerResponse<Indication> duplicateIndicationForStaff(String indicationId,
                                                           String staffId)
            throws IndicationNotFoundException, StaffNotFoundException;

    /*****************************************************************************************
     * To add a content to an indication Object. The concerned indication is precised by his
     * Id (indicationId).
     * @param value
     * @param contentType
     * @param indicationId
     * @return
     * @throws IndicationNotFoundException
     */
    ServerResponse<Indication> addContentToIndication(String value, String contentType,
                                                      String indicationId)
            throws IndicationNotFoundException;



    /*****************************************************************************************
     * This function is used to update the content associated to an indication. Like explaining
     * before, the indicationId is to find the indication object and use it to find the content
     * to update. After founding the content object, only the value can be modify.
     * If the content specified does not belonging to indication then
     * ContentNotBelongingToException must be lunch.
     * @param contentId
     * @param value
     * @param indicationId
     * @return
     * @throws IndicationNotFoundException
     * @throws ContentNotBelongingToException
     */
    ServerResponse<Indication> updateContentToIndication(String contentId, String value,
                                                      String indicationId)
            throws IndicationNotFoundException, ContentNotBelongingToException;

    /*****************************************************************************************
     * To remove or supprim a content in the indication object. But we must be sure that the
     * content belonging to the indication object before.
     * @param contentId
     * @param indicationId
     * @return
     * @throws ContentNotFoundException
     * @throws IndicationNotFoundException
     * @throws ContentNotBelongingToException
     */
    ServerResponse<Indication> removeContentToIndication(String contentId, String indicationId)
            throws IndicationNotFoundException, ContentNotBelongingToException;

    /*****************************************************************************************
     * To remove or supprim an indication object belonging to a staff. Only the owner staff
     * can do it.
     * @param indicationId
     * @param staffId
     * @return
     * @throws IndicationNotFoundException
     * @throws StaffNotFoundException
     * @throws IndicationNotBelongingToStaffException
     */
    ServerResponse<Indication> removeIndication(String indicationId, String staffId)
            throws IndicationNotFoundException, StaffNotFoundException,
            IndicationNotBelongingToStaffException;

    /*************************************************************************
     * Remove or supprim an indication object to the system. This can only be used by
     * an adminitrator with admin role.
     * @param indicationId
     * @return
     * @throws IndicationNotFoundException
     */
    ServerResponse<Indication> removeIndication(String indicationId)
            throws IndicationNotFoundException;
}
