package org.osivia.services.procedure.portlet.service;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.Record;
import org.osivia.services.procedure.portlet.model.WebIdException;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import net.sf.json.JSONArray;


/**
 * @author Dorian Licois
 */
public interface IProcedureService {


    /**
     * @param nuxeoController
     * @return
     * @throws PortletException
     */
    ProcedureModel retrieveProcedureByWebId(NuxeoController nuxeoController, String path) throws PortletException;


    /**
     * @param nuxeoController
     * @param procedurepath
     * @param filter
     * @return
     * @throws PortletException
     */
    List<ProcedureModel> retrieveProcedureModels(NuxeoController nuxeoController, String procedurepath, String filter) throws PortletException;


    /**
     * @param nuxeoController
     * @param procedureModel
     * @param Procedurepath
     * @return
     * @throws PortletException
     */
    ProcedureModel createProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel, String Procedurepath)
            throws PortletException, WebIdException;


    /**
     * @param nuxeoController
     * @param procedureModel
     * @return
     * @throws PortletException
     */
    ProcedureModel updateProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException, WebIdException;


    /**
     * @param nuxeoController
     * @param procedureModel
     * @throws PortletException
     */
    void deleteProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException;


    /**
     * @param nuxeoController
     * @return
     * @throws PortletException
     */
    ProcedureInstance retrieveProcedureInstanceByWebId(NuxeoController nuxeoController, String webId) throws PortletException;


    /**
     * @param nuxeoController
     * @param webId
     * @return
     * @throws PortletException
     */
    Record retrieveRecordInstanceByWebId(NuxeoController nuxeoController, String webId) throws PortletException;


    /**
     * @param nuxeoController
     * @return
     * @throws PortletException
     */
    ProcedureInstance retrieveProcedureInstanceById(NuxeoController nuxeoController, String uuid) throws PortletException;


    /**
     * @param nuxeoController
     * @param procedurepath
     * @return
     * @throws PortletException
     */
    List<ProcedureModel> listProcedures(NuxeoController nuxeoController, String procedurepath) throws PortletException;


    /**
     * @param nuxeoController
     * @param procedurePath
     * @param displayContext
     * @param procedureType
     * @return
     * @throws PortletException
     */
    String getAddUrl(NuxeoController nuxeoController, String procedurePath, String displayContext, String procedureType) throws PortletException;


    /**
     * @param nuxeoController
     * @param procedureModel
     * @return
     */
    List<ProcedureInstance> retrieveProceduresInstanceByModel(NuxeoController nuxeoController, ProcedureModel procedureModel);


    /**
     * Get close procedure URL.
     * 
     * @param portalControllerContext portal controller context
     * @return URL
     * @throws PortletException
     */
    String getCloseUrl(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * search database for steps like given name
     * 
     * @param nuxeoController
     * @param filter
     * @return
     */
    List<Map<String, String>> retrieveStepsByName(NuxeoController nuxeoController, String filter);


    /**
     * perform post retrieve updates
     * 
     * @param nuxeoController
     * @param form
     * @throws PortletException
     */
    void updateData(NuxeoController nuxeoController, Form form) throws PortletException;


    /**
     * Search vocabulary values.
     * 
     * @param portalControllerContext portal controller context
     * @param vocabularyId vocabulary identifier
     * @param filter search filter
     * @return search results
     * @throws PortletException
     */
    JSONArray searchVocabularyValues(PortalControllerContext portalControllerContext, String vocabularyId, String filter) throws PortletException;


    /**
     * Get record types.
     * 
     * @param portalControllerContext portal controller context
     * @return record types
     * @throws PortletException
     */
    Map<String, String> getRecordTypes(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Search records.
     * 
     * @param portalControllerContext portal controller context
     * @param recordFolderWebId parent record folder webId
     * @param filter search filter
     * @return search results
     * @throws PortletException
     */
    JSONArray searchRecords(PortalControllerContext portalControllerContext, String recordFolderWebId, String filter) throws PortletException;

}
