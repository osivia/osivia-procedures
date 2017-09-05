package org.osivia.services.procedure.portlet.service;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import net.sf.json.JSONArray;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.Record;
import org.osivia.services.procedure.portlet.model.WebIdException;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;


/**
 * @author Dorian Licois
 */
public interface IProcedureService {


    /**
     * @param nuxeoController
     * @return
     * @throws PortletException
     */
    public ProcedureModel retrieveProcedureByWebId(NuxeoController nuxeoController, String path) throws PortletException;

    /**
     * @param nuxeoController
     * @param procedurepath
     * @param filter
     * @return
     * @throws PortletException
     */
    public List<ProcedureModel> retrieveProcedureModels(NuxeoController nuxeoController, String procedurepath, String filter) throws PortletException;


    /**
     * @param nuxeoController
     * @param procedureModel
     * @param Procedurepath
     * @return
     * @throws PortletException
     */
    public ProcedureModel createProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel, String Procedurepath) throws PortletException,
            WebIdException;

    /**
     * @param nuxeoController
     * @param procedureModel
     * @return
     * @throws PortletException
     */
    public ProcedureModel updateProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException, WebIdException;


    /**
     * @param nuxeoController
     * @param procedureModel
     * @throws PortletException
     */
    public void deleteProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException;

    /**
     * @param nuxeoController
     * @return
     * @throws PortletException
     */
    public ProcedureInstance retrieveProcedureInstanceByWebId(NuxeoController nuxeoController, String webId) throws PortletException;

    /**
     * @param nuxeoController
     * @param webId
     * @return
     * @throws PortletException
     */
    public Record retrieveRecordInstanceByWebId(NuxeoController nuxeoController, String webId) throws PortletException;

    /**
     * @param nuxeoController
     * @return
     * @throws PortletException
     */
    public ProcedureInstance retrieveProcedureInstanceById(NuxeoController nuxeoController, String uuid) throws PortletException;

    /**
     * @param nuxeoController
     * @param filter
     * @param vocabularyName
     * @return
     * @throws PortletException
     */
    public JSONArray getVocabularyValues(NuxeoController nuxeoController, String filter, String vocabularyName) throws PortletException;

    /**
     * @param nuxeoController
     * @param procedurepath
     * @return
     * @throws PortletException
     */
    public List<ProcedureModel> listProcedures(NuxeoController nuxeoController, String procedurepath)
            throws PortletException;

    /**
     * @param nuxeoController
     * @param procedurePath
     * @param displayContext
     * @param procedureType
     * @return
     * @throws PortletException
     */
    public String getAddUrl(NuxeoController nuxeoController, String procedurePath, String displayContext, String procedureType) throws PortletException;

    /**
     * @param nuxeoController
     * @param procedureModel
     * @return
     */
    public List<ProcedureInstance> retrieveProceduresInstanceByModel(NuxeoController nuxeoController, ProcedureModel procedureModel);

    /**
     * Get close procedure URL.
     * 
     * @param portalControllerContext portal controller context
     * @return URL
     * @throws PortletException
     */
    public String getCloseUrl(PortalControllerContext portalControllerContext) throws PortletException;

    /**
     * search database for steps like given name
     * 
     * @param nuxeoController
     * @param filter
     * @return
     */
    public List<Map<String, String>> retrieveStepsByName(NuxeoController nuxeoController, String filter);

    /**
     * perform post retrieve updates
     * 
     * @param nuxeoController
     * @param form
     * @throws PortletException
     */
    public void updateData(NuxeoController nuxeoController, Form form) throws PortletException;

}
