package org.osivia.services.procedure.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.procedure.portlet.exception.FilterException;
import org.osivia.services.procedure.portlet.model.Action;
import org.osivia.services.procedure.portlet.model.AddField;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.Field;
import org.osivia.services.procedure.portlet.model.FilePath;
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.ProcedureObject;
import org.osivia.services.procedure.portlet.model.Step;
import org.osivia.services.procedure.portlet.model.Variable;
import org.osivia.services.procedure.portlet.service.IProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;
import org.springframework.web.portlet.multipart.MultipartActionRequest;

import fr.toutatice.outils.ldap.entity.Profil;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

@Controller
@SessionAttributes("form")
@RequestMapping(value = "VIEW")
public class ProcedurePortletController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

    /** create view. */
    private static final String CREATE_VIEW = "editProcedure";

    /** edit view. */
    private static final String EDIT_VIEW = "editStep";

    /** procedure view. */
    private static final String VIEW_PROCEDURE = "viewProcedure";

    /** VIEW_ENDSTEP */
    private static final String VIEW_ENDSTEP = "endStep";

    private static final String VIEW_ACTION = "editAction";

    private static final String LIST_VIEW = "list";


    /** Portlet context. */
    private PortletContext portletContext;
    /** Portlet config. */
    private PortletConfig portletConfig;

    /** procedureService */
    @Autowired
    private IProcedureService procedureService;

    @Autowired
    private Profil profil;

    public ProcedurePortletController() {
        super();
    }

    /**
     * Portlet initialization.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(portletConfig);
    }

    /**
     * View page render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view page path
     * @throws PortletException
     * @throws CMSException
     */
    @RenderMapping
    public String defaultView(RenderRequest request, RenderResponse response) throws PortletException, CMSException {

        if (StringUtils.equals(getAction(request), "adminproc")) {
            return CREATE_VIEW;
        } else if (StringUtils.equals(getAction(request), "adminprocstep")) {
            return EDIT_VIEW;
        } else {
            if (getPath(request) != null) {
                return VIEW_PROCEDURE;
            } else {
                return LIST_VIEW;
            }
        }
    }

    @RenderMapping(params = "action=editProcedure")
    public String editProcedureView(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        return CREATE_VIEW;
    }

    @RenderMapping(params = "action=editStep")
    public String editStepView(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        return EDIT_VIEW;
    }

    @RenderMapping(params = "editAction")
    public String endStepView(RenderRequest request, RenderResponse response, @RequestParam(value = "editAction", required = false) String editAction)
            throws PortletException, CMSException {

        request.setAttribute("editAction", editAction);
        return VIEW_ACTION;
    }

    @RenderMapping(params = "action=viewProcedure")
    public String viewProcedure(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        return VIEW_PROCEDURE;
    }

    @RenderMapping(params = "action=endStep")
    public String endStepView(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        return VIEW_ENDSTEP;
    }


    @ModelAttribute(value = "form")
    public Form getForm(PortletRequest request, PortletResponse response, @RequestParam(value = "selectedStep", required = false) String selectedStep)
            throws PortletException {

        Form form;
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        if (StringUtils.isNotEmpty(getPath(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREMODEL.getName())) {
            final ProcedureModel procedureModel = procedureService.retrieveProcedureByPath(nuxeoController, getPath(request));
            form = new Form(procedureModel);
            if (StringUtils.isNotEmpty(selectedStep)) {
                form.setSelectedStep(selectedStep);
            } else {
                form.setSelectedStep("0");
            }
            if (!StringUtils.equals(getAction(request), "adminproc") && !StringUtils.equals(getAction(request), "adminprocstep")) {
                procedureService.updateVocabulariesWithValues(nuxeoController, form);
            }
        } else if (StringUtils.isNotEmpty(getPath(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREINSTANCE.getName())) {
            final ProcedureInstance procedureInstance = procedureService.retrieveProcedureInstanceByPath(nuxeoController, getPath(request));
            final ProcedureModel procedureModel = procedureService.retrieveProcedureByPath(nuxeoController, procedureInstance.getProcedureModelPath());
            form = new Form(procedureModel, procedureInstance);
            procedureService.updateFormWithObjectsValues(nuxeoController, form);
            procedureService.updateVocabulariesWithValues(nuxeoController, form);
        } else {
            form = new Form();
        }
        return form;
    }

    @ModelAttribute(value = "procedureList")
    public List<ProcedureModel> getListProcedureModel(PortletRequest request, PortletResponse response) throws PortletException {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        return procedureService.listProcedures(nuxeoController, getPortalUrlFactory());
    }

    @ModelAttribute(value = "addUrl")
    public String getAddUrl(PortletRequest request, PortletResponse response) throws PortletException {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        return procedureService.getAddUrl(nuxeoController, getPortalUrlFactory());
    }


    @ResourceMapping(value = "groupSearch")
    public void getProfils(ResourceRequest request, ResourceResponse response, @RequestParam(value = "filter", required = false) String filter)
            throws PortletException {

        final List<Profil> listeProfils = profil.findProfilByFiltre("(&(objectClass=groupOfNames)(cn=*" + filter + "*))");
        response.setContentType("application/json");
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getPortletOutputStream(), listeProfils);
        } catch (final IOException e) {
            throw new PortletException(e);
        }
    }

    @ResourceMapping(value = "vocabularySearch")
    public void getVocabulary(ResourceRequest request, ResourceResponse response, @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "vocabularyName", required = true) String vocabularyName) throws PortletException {

        // Nuxeo controller
        final NuxeoController nuxeoController = new NuxeoController(request, response, getPortletContext());
        nuxeoController.setCacheTimeOut(TimeUnit.HOURS.toMillis(1));
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);

        try {
            final JSONArray values = procedureService.getVocabularyValues(nuxeoController, filter, vocabularyName);
            final PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
            printWriter.write(values.toString());
            printWriter.close();
        } catch (final IOException e) {
            throw new PortletException(e);
        } catch (final PortletException e) {
            throw new PortletException(e);
        }
    }

    @ActionMapping(value = "editProcedure", params = "launchProcedure")
    public void launchProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        form.setProcedureInstance(new ProcedureInstance());
        form.getProcedureInstance().setProcedureModelPath(form.getProcedureModel().getPath());

        response.setRenderParameter("action", "viewProcedure");
    }

    @ActionMapping(value = "actionProcedure", params = "proceedProcedure")
    public void proceedProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "stepReference") String stepReference, SessionStatus sessionStatus) throws PortletException, IOException {

        if (request instanceof MultipartActionRequest) {
            // set the uploaded files in the instance
            final MultipartActionRequest multipartActionRequest = (MultipartActionRequest) request;
            for (final Field field : form.getTheCurrentStep().getFields()) {
                setMultipartFile(field, multipartActionRequest, form);
            }
        }

        try {
            if (StringUtils.isNotEmpty(getPath(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREMODEL.getName())) {
                // if there is no instance, create it
                final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
                procedureService.createProcedureInstance(nuxeoController, form, stepReference);
                // redirect to end of step page
                response.setRenderParameter("action", "endStep");
            } else if (StringUtils.isNotEmpty(getPath(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREINSTANCE.getName())) {
                // update the instance
                final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
                final ProcedureInstance procedureInstance = procedureService.updateProcedureInstance(nuxeoController, form, getPath(request), stepReference);
                form.setProcedureInstance(procedureInstance);

                // redirect to end of step page
                response.setRenderParameter("action", "endStep");
            } else {
                // shouldn't happen
                response.setRenderParameter("action", "viewProcedure");
            }
        } catch (final FilterException e) {
            form.setFilterMessage(e.getFilterMessage());
            request.setAttribute("filterMessage", e.getFilterMessage());
            response.setRenderParameter("action", "viewProcedure");
        }
        sessionStatus.setComplete();
    }


    @ActionMapping(value = "editProcedure", params = "saveProcedure")
    public void saveProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException, IOException {

        final String path = getPath(request);

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        if (StringUtils.isNotEmpty(path)) {
            // if the procedure exist in database, update it
            procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
            response.setRenderParameter("action", "editProcedure");
        } else {
            // if the procedure doesn't exist in database, create it
            final ProcedureModel createdProcedure = procedureService.createProcedure(nuxeoController, form.getProcedureModel());

            final Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put(Constants.WINDOW_PROP_URI, createdProcedure.getPath());
            windowProperties.put("osivia.doctype", DocumentTypeEnum.PROCEDUREMODEL.getName());
            windowProperties.put("osivia.title", "Éditer une procedure");
            windowProperties.put("osivia.hideDecorators", "1");
            windowProperties.put("osivia.ajaxLink", "1");
            windowProperties.put("osivia.procedure.admin", "adminproc");
            String redirectUrl;
            try {
                redirectUrl = getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance",
                        windowProperties, false);
            } catch (final PortalException e) {
                throw new PortletException(e);
            }
            response.sendRedirect(redirectUrl);
        }
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editProcedure", params = "deleteProcedure")
    public void deleteProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException, IOException {

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.deleteProcedure(nuxeoController, form.getProcedureModel());

        final String redirectUrl = getPortalUrlFactory().getBackURL(nuxeoController.getPortalCtx(), false);
        response.sendRedirect(redirectUrl);
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editProcedure", params = "addStep")
    public void addStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException, IOException {

        final Integer newIndex = Integer.valueOf(form.getProcedureModel().getSteps().size());
        final String path = getPath(request);

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        if (StringUtils.isNotEmpty(path)) {
            // if the procedure exist in database, update it
            form.getProcedureModel().getSteps().add(new Step(newIndex));
            form.setSelectedStep(String.valueOf(newIndex));
            procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
            response.setRenderParameter("action", "editStep");
        } else {
            // if the procedure doesn't exist in database, create it
            final ProcedureModel createdProcedure = procedureService.createProcedure(nuxeoController, form.getProcedureModel());
            createdProcedure.getSteps().add(new Step(newIndex));
            procedureService.updateProcedure(nuxeoController, createdProcedure);

            final Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put(Constants.WINDOW_PROP_URI, createdProcedure.getPath());
            windowProperties.put("osivia.doctype", DocumentTypeEnum.PROCEDUREMODEL.getName());
            windowProperties.put("osivia.title", "Éditer une procedure");
            windowProperties.put("osivia.hideDecorators", "1");
            windowProperties.put("osivia.ajaxLink", "1");
            windowProperties.put("osivia.procedure.admin", "adminprocstep");
            String redirectUrl;
            try {
                redirectUrl = getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance",
                        windowProperties, false);
            } catch (final PortalException e) {
                throw new PortletException(e);
            }
            response.sendRedirect(redirectUrl);
        }
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editProcedure", params = "addObject")
    public void addObject(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException, IOException {

        final String path = getPath(request);

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        if (StringUtils.isNotEmpty(path)) {
            // if the procedure exist in database, update it
            form.getProcedureModel().getProcedureObjects().add(new ProcedureObject());
            procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
            response.setRenderParameter("action", "editProcedure");
        } else {
            // if the procedure doesn't exist in database, create it
            final ProcedureModel createdProcedure = procedureService.createProcedure(nuxeoController, form.getProcedureModel());
            createdProcedure.getProcedureObjects().add(new ProcedureObject());
            procedureService.updateProcedure(nuxeoController, createdProcedure);

            final Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put(Constants.WINDOW_PROP_URI, createdProcedure.getPath());
            windowProperties.put("osivia.doctype", DocumentTypeEnum.PROCEDUREMODEL.getName());
            windowProperties.put("osivia.title", "Éditer une procedure");
            windowProperties.put("osivia.hideDecorators", "1");
            windowProperties.put("osivia.ajaxLink", "1");
            windowProperties.put("osivia.procedure.admin", "adminproc");
            String redirectUrl;
            try {
                redirectUrl = getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance",
                        windowProperties, false);
            } catch (final PortalException e) {
                throw new PortletException(e);
            }
            response.sendRedirect(redirectUrl);
        }
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editProcedure", params = "deleteObject")
    public void deleteObject(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "selectedObject",
    required = false) String selectedObject, SessionStatus sessionStatus) throws PortletException, IOException {

        form.getProcedureModel().getProcedureObjects().remove(Integer.valueOf(selectedObject).intValue());

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editProcedure");
        sessionStatus.setComplete();
    }

    /**
     * @param request
     * @return
     */
    private String getPath(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        final String path = window.getProperty(Constants.WINDOW_PROP_URI);
        return path;
    }

    /**
     * @param request
     * @return
     */
    private String getDocType(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        final String path = window.getProperty("osivia.doctype");
        return path;
    }

    /**
     * @param request
     * @return
     */
    private String getAction(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        final String path = window.getProperty("osivia.procedure.admin");
        return path;
    }

    @ActionMapping(value = "editProcedure", params = "editStep")
    public void editStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, IOException {

        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editStep", params = "saveStep")
    public void saveStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException {

        final Set<Field> fieldsSet = new HashSet<Field>();
        addAllFieldsToSet(fieldsSet, form.getTheSelectedStep().getFields());
        form.getTheSelectedStep().setFieldsSet(fieldsSet);


        form.getProcedureModel().getSteps().set(Integer.valueOf(form.getTheSelectedStep().getIndex()), form.getTheSelectedStep());

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editProcedure");
        sessionStatus.setComplete();
    }

    private void addAllFieldsToSet(Set<Field> fieldsSet, List<Field> fields) {
        fieldsSet.addAll(fields);
        for (final Field field : fields) {
            if (field.getFields() != null) {
                addAllFieldsToSet(fieldsSet, field.getFields());
            }
        }
    }

    @ActionMapping(value = "editStep", params = "deleteStep")
    public void deleteStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException {
        form.getProcedureModel().getSteps().remove(Integer.valueOf(form.getSelectedStep()).intValue());
        form.getProcedureModel().updateStepsIndexes();

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editProcedure");
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editProcedure", params = "deleteStep")
    public void deleteStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "selectedStep",
    required = false) String selectedStep, SessionStatus sessionStatus) throws PortletException {

        form.getProcedureModel().getSteps().remove(Integer.valueOf(selectedStep).intValue());
        form.getProcedureModel().updateStepsIndexes();

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editProcedure");
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editProcedure", params = "duplicateStep")
    public void duplicateStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "selectedStep",
    required = false) String selectedStep, SessionStatus sessionStatus) throws PortletException {

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        final Integer newIndex = Integer.valueOf(form.getProcedureModel().getSteps().size());

        final Step copiedStep = form.getProcedureModel().getSteps().get(Integer.valueOf(selectedStep).intValue());

        form.getProcedureModel().getSteps().add(new Step(newIndex, copiedStep));
        form.setSelectedStep(String.valueOf(newIndex));
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editStep");
        sessionStatus.setComplete();
    }


    @ActionMapping(value = "editStep", params = "editField")
    public void editField(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "selectedField",
    required = false) String selectedField) throws PortletException {


        final Field editedField = form.getTheSelectedStep().getFields().get((Integer.valueOf(selectedField).intValue()));
        final String[] varOptionsTab = editedField.getVarOptions().split(",");

        final Variable variable = new Variable(editedField.getName(), editedField.getLabel(), editedField.getType(), Arrays.asList(varOptionsTab));

        form.getProcedureModel().getVariables().put(editedField.getName(), variable);

        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editStep", params = "addField")
    public void addField(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        final AddField addField = form.getNewField();

        final Variable variable;
        final Field field = new Field(form.getTheSelectedStep().getNextPath());
        final String[] varOptionsTab = addField.getVarOptions().split(",");
        variable = new Variable(addField.getVariableName(), addField.getLabel(), addField.getType(), Arrays.asList(varOptionsTab));
        form.getProcedureModel().getVariables().put(addField.getVariableName(), variable);
        field.setInput(true);
        field.setName(addField.getVariableName());

        updateProcedureWithForm(request, response, form, field);
    }

    @ActionMapping(value = "editStep", params = "addFieldSet")
    public void addFieldSet(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "newFieldSetLabel", required = false) String newFieldSetLabel) throws PortletException {

        final Field field = new Field(form.getTheSelectedStep().getNextPath());
        field.setFieldSet(true);
        field.setSuperLabel(newFieldSetLabel);
        field.setName(newFieldSetLabel);

        updateProcedureWithForm(request, response, form, field);
    }

    private void updateProcedureWithForm(ActionRequest request, ActionResponse response, Form form, final Field field) throws PortletException {
        field.setPath(String.valueOf(form.getTheSelectedStep().getFields().size()));
        form.getTheSelectedStep().getFields().add(field);
        form.setNewField(new AddField());
        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editStep", params = "updateForm")
    public void updateForm(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        final Map<String, List<Field>> allFieldsMap = new HashMap<String, List<Field>>();
        addAllFields(allFieldsMap, form.getTheSelectedStep().getFields());

        rebuildStep(allFieldsMap, form.getTheSelectedStep());

        response.setRenderParameter("action", "editStep");
    }

    private void rebuildStep(Map<String, List<Field>> allFieldsMap, Step step) {
        final List<Field> baseFields = new ArrayList<Field>();

        final List<Field> fieldList = allFieldsMap.get(StringUtils.EMPTY);
        if (fieldList != null) {
            baseFields.addAll(fieldList);
            Collections.sort(baseFields);
        }
        rebuildFields(allFieldsMap, baseFields);

        step.setFields(baseFields);
    }

    private void rebuildFields(Map<String, List<Field>> allFieldsMap, List<Field> fields) {
        if (fields != null) {
            for (final Field field : fields) {
                field.setFields(allFieldsMap.get(field.getPath()));
                rebuildFields(allFieldsMap, field.getFields());
            }
        }
    }

    private void addAllFields(Map<String, List<Field>> allFieldsMap, List<Field> fields) {
        if (fields != null) {
            for (final Field field : fields) {
                if (field.getPath() != null) {
                    // on ajoute la field dans la map avec le path parent comme clé
                    final String parentPath = field.getPath().length() > 1 ? StringUtils.substringBeforeLast(field.getPath(), ",") : StringUtils.EMPTY;
                    List<Field> parentFields = allFieldsMap.get(parentPath);
                    if (parentFields == null) {
                        parentFields = new ArrayList<Field>();
                    }
                    parentFields.add(field);
                    Collections.sort(parentFields);
                    allFieldsMap.put(parentPath, parentFields);
                    addAllFields(allFieldsMap, field.getFields());
                }
            }
        }
    }

    @ActionMapping(value = "editStep", params = "addButton")
    public void addButton(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        form.getTheSelectedStep().getActions().add(new Action());
        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editStep", params = "deleteButton")
    public void deleteButton(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form,
            @RequestParam(value = "selectedButton") String index) throws PortletException {
        form.getTheSelectedStep().getActions().remove(Integer.valueOf(index).intValue());

        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editStep", params = "deleteField")
    public void deleteField(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form,
            @RequestParam(value = "selectedField") String selectedField) throws PortletException {

        final String[] path = selectedField.split(",");
        removeFieldByPath(form.getTheSelectedStep().getFields(), path);

        response.setRenderParameter("action", "editStep");
    }

    private void removeFieldByPath(List<Field> fields, String[] path) {

        final Integer index = Integer.parseInt(path[0]);
        Field nestedField;
        if ((path.length == 1) && (fields != null)) {
            // on a fini de parcourir le path
            final ListIterator<Field> listIterator = fields.listIterator();
            while (listIterator.hasNext()) {
                final Field field = listIterator.next();
                final String[] pathArray = StringUtils.split(field.getPath(), ',');
                if ((pathArray.length > 0) && (Integer.parseInt(pathArray[pathArray.length - 1]) == index)) {
                    listIterator.remove();
                }
            }
        } else {
            // on continue de parcourir le path
            nestedField = fields.get(index);
            ArrayUtils.remove(path, 0);
            removeFieldByPath(nestedField.getFields(), path);
        }
    }


    /**
     * set the uploaded files in the instance, traversing recursive fields
     *
     * @param field
     * @param multipartActionRequest
     * @param form
     */
    private void setMultipartFile(Field field, MultipartActionRequest multipartActionRequest, Form form) {
        if (!field.isFieldSet()) {
            if (field.isInput()) {
                final MultipartFile multipartFile = multipartActionRequest.getFileMap().get("file:" + field.getName());
                if ((multipartFile != null) && (multipartFile.getSize() > 0)) {
                    final FilePath filePath = new FilePath();
                    filePath.setFile(multipartFile);
                    filePath.setVariableName(field.getName());
                    filePath.setFileName(String.valueOf(multipartFile.getOriginalFilename()));
                    form.getProcedureInstance().getFilesPath().put(filePath.getVariableName(), filePath);
                }
            }
        } else {
            for (final Field nestedField : field.getFields()) {
                setMultipartFile(nestedField, multipartActionRequest, form);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

}