package org.osivia.services.procedure.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;


/**
 * @author dorian
 */
/**
 * @author dorian
 */
/**
 * @author dorian
 */
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
     * @param procedureModel
     * @param procedureInstance
     * @param taskTitle
     * @return
     * @throws PortletException
     */
    public ProcedureInstance createProcedureInstance(NuxeoController nuxeoController, ProcedureModel procedureModel, ProcedureInstance procedureInstance,
            String taskTitle) throws PortletException;

    /**
     * @param nuxeoController
     * @return
     * @throws PortletException
     */
    public ProcedureInstance retrieveProcedureInstanceByPath(NuxeoController nuxeoController, String path) throws PortletException;

    /**
     * @param nuxeoController
     * @param procedureModel
     * @param procedureInstance
     * @param procedureInstancePath
     * @param taskTitle
     * @return
     * @throws PortletException
     */
    public ProcedureInstance updateProcedureInstance(NuxeoController nuxeoController, ProcedureModel procedureModel, ProcedureInstance procedureInstance,
            String procedureInstancePath,
            String taskTitle) throws PortletException;


    /**
     * @param nuxeoController
     * @param procedureInstancePath
     * @param variableName
     * @throws PortletException
     */
    public void createDocumentFromBlob(NuxeoController nuxeoController, String procedureInstancePath, String variableName) throws PortletException;

    /**
     * @param nuxeoController
     * @param form
     * @throws PortletException
     */
    public void updateFormWithObjectsValues(NuxeoController nuxeoController, Form form) throws PortletException;

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
