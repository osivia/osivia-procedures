package org.osivia.services.procedure.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import net.sf.json.JSONArray;

import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;


/**
 * @author dorian
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
     * @param procedureModel
     * @param Procedurepath
     * @return
     * @throws PortletException
     */
    public ProcedureModel createProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel, String Procedurepath) throws PortletException;

    /**
     * @param nuxeoController
     * @param procedureModel
     * @return
     * @throws PortletException
     */
    public ProcedureModel updateProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException;


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
     * @return
     * @throws PortletException
     */
    public ProcedureInstance retrieveProcedureInstanceById(NuxeoController nuxeoController, String uuid) throws PortletException;

    /**
     * @param nuxeoController
     * @param form
     * @throws PortletException
     */
    public void updateFormWithObjectsValues(NuxeoController nuxeoController, Form form) throws PortletException;

    /**
     * @param nuxeoController
     * @param form
     * @throws PortletException
     */
    public void updateVocabulariesWithValues(NuxeoController nuxeoController, Form form) throws PortletException;

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
     * @param portalUrlFactory
     * @param Procedurepath
     * @return
     * @throws PortletException
     */
    public List<ProcedureModel> listProcedures(NuxeoController nuxeoController, IPortalUrlFactory portalUrlFactory, String Procedurepath)
            throws PortletException;

    /**
     * @param nuxeoController
     * @param portalUrlFactory
     * @param procedurePath
     * @return
     * @throws PortletException
     */
    public String getAddUrl(NuxeoController nuxeoController, IPortalUrlFactory portalUrlFactory, String procedurePath) throws PortletException;
}
