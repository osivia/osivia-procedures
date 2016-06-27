package org.osivia.services.procedure.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import net.sf.json.JSONArray;

import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.procedure.portlet.exception.FilterException;
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
    public ProcedureModel retrieveProcedureByPath(NuxeoController nuxeoController, String path) throws PortletException;

    /**
     * @param nuxeoController
     * @param procedureModel
     * @return
     * @throws PortletException
     */
    public ProcedureModel createProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException;

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
     * @param form
     * @param stepReference
     * @return
     * @throws PortletException
     * @throws FilterException
     */
    public ProcedureInstance createProcedureInstance(NuxeoController nuxeoController, Form form, String stepReference) throws PortletException, FilterException;

    /**
     * @param nuxeoController
     * @return
     * @throws PortletException
     */
    public ProcedureInstance retrieveProcedureInstanceByPath(NuxeoController nuxeoController, String path) throws PortletException;

    /**
     * @param nuxeoController
     * @param form
     * @param procedureInstancePath
     * @param stepReference
     * @return
     * @throws PortletException
     * @throws FilterException
     */
    public ProcedureInstance updateProcedureInstance(NuxeoController nuxeoController, Form form, String procedureInstancePath, String stepReference)
            throws PortletException, FilterException;


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
     * @return
     * @throws PortletException
     */
    public List<ProcedureModel> listProcedures(NuxeoController nuxeoController, IPortalUrlFactory portalUrlFactory) throws PortletException;

    /**
     * @param nuxeoController
     * @param portalUrlFactory
     * @return
     * @throws PortletException
     */
    public String getAddUrl(NuxeoController nuxeoController, IPortalUrlFactory portalUrlFactory) throws PortletException;
}
