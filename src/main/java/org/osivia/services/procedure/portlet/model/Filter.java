package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


/**
 * @author Dorian Licois
 */
@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
creatorVisibility = Visibility.NONE)
public class Filter implements Comparable<Filter> {

    /** filterName */
    @JsonProperty("filterName")
    private String filterName;

    /** filterPath */
    @JsonProperty("filterPath")
    private String filterPath;

    /** filterInstanceId */
    @JsonProperty("filterInstanceId")
    private String filterInstanceId;

    /** filterId */
    @JsonProperty("filterId")
    private String filterId;

    /** argumentsList */
    @JsonProperty("argumentsList")
    private List<Argument> argumentsList;

    /** hasChildren */
    @JsonIgnore
    private boolean hasChildren;

    /** filters */
    @JsonIgnore
    private List<Filter> filters;

    /** labelKey */
    @JsonIgnore
    private String labelKey;

    /** descriptionKey */
    @JsonIgnore
    private String descriptionKey;

    /** classLoader */
    @JsonIgnore
    private ClassLoader classLoader;

    /** bundleFactory */
    @JsonIgnore
    private IBundleFactory bundleFactory;

    /** parameterNames */
    @JsonIgnore
    private Map<String, String> parameterNames;

    public Filter() {
    }

    public Filter(FormFilter formFilter, String path) {
        setFilterPath(path);
        setFilterId(formFilter.getId());
        setFilterInstanceId(getFilterId().concat(getFilterPath()));
        setHasChildren(formFilter.hasChildren());
        setLabelKey(formFilter.getLabelKey());
        setDescriptionKey(formFilter.getDescriptionKey());
        setClassLoader(formFilter.getClass().getClassLoader());
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        setBundleFactory(internationalizationService.getBundleFactory(getClassLoader()));
        if (formFilter.getParameters() != null) {
            final List<Argument> argumentsList = new ArrayList<Argument>(formFilter.getParameters().entrySet().size());
            for (Entry<String, FormFilterParameterType> argumentEntry : formFilter.getParameters().entrySet()) {
                Argument argument = new Argument();
                argument.setArgumentName(argumentEntry.getKey());
                argument.setType(argumentEntry.getValue());
                argumentsList.add(argument);
            }
            setArgumentsList(argumentsList);
        }
    }

    public Filter(PropertyMap propertyMap, NuxeoController nuxeoController) {
        setFilterName(propertyMap.getString("filterName"));
        setFilterPath(propertyMap.getString("filterPath"));
        setFilterId(propertyMap.getString("filterId"));
        setFilterInstanceId(propertyMap.getString("filterInstanceId"));

        Map<String, FormFilter> formsFilters = nuxeoController.getNuxeoCMSService().getCMSCustomizer().getFormsFilters();
        FormFilter formFilter = formsFilters.get(getFilterId());
        if (formFilter != null) {
            setLabelKey(formFilter.getLabelKey());
            setDescriptionKey(formFilter.getDescriptionKey());
            setHasChildren(formFilter.hasChildren());
            setClassLoader(formFilter.getClass().getClassLoader());
            IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                    IInternationalizationService.MBEAN_NAME);
            setBundleFactory(internationalizationService.getBundleFactory(getClassLoader()));
        }
        final PropertyList argumentsPptyList = propertyMap.getList("argumentsList");
        if (argumentsPptyList != null) {
            final List<Argument> argumentsList = new ArrayList<Argument>();
            for (final Object argumentO : argumentsPptyList.list()) {
                final PropertyMap argumentMap = (PropertyMap) argumentO;
                argumentsList.add(new Argument(argumentMap));
            }
            setArgumentsList(argumentsList);
        }
    }

    /**
     * @param filter
     */
    public void updateFilter(Filter filter) {
        setFilterName(filter.getFilterName());
        setArgumentsList(filter.getArgumentsList());
    }

    /**
     * @param newPath
     */
    public void updateFilterPath(String newPath) {
        setFilterPath(newPath);
        setFilterInstanceId(getFilterId().concat(getFilterPath()));
    }


    /**
     * Getter for filterName.
     *
     * @return the filterName
     */
    public String getFilterName() {
        return filterName;
    }


    /**
     * Setter for filterName.
     *
     * @param filterName the filterName to set
     */
    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }


    /**
     * Getter for filterPath.
     *
     * @return the filterPath
     */
    public String getFilterPath() {
        return filterPath;
    }


    /**
     * Setter for filterPath.
     *
     * @param filterPath the filterPath to set
     */
    public void setFilterPath(String filterPath) {
        this.filterPath = filterPath;
    }


    /**
     * Getter for argumentsList.
     *
     * @return the argumentsList
     */
    public List<Argument> getArgumentsList() {
        return argumentsList;
    }


    /**
     * Setter for argumentsList.
     *
     * @param argumentsList the argumentsList to set
     */
    public void setArgumentsList(List<Argument> argumentsList) {
        this.argumentsList = argumentsList;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    @Override
    public int compareTo(Filter filter) {
        int returnValue;

        int index = 0;
        final String[] pathArray = StringUtils.split(getFilterPath(), ',');
        final String[] comparedPathArray = StringUtils.split(filter.getFilterPath(), ',');
        Integer pathPart = Integer.parseInt(pathArray[index]);
        Integer comparedPathPart = Integer.parseInt(comparedPathArray[index]);
        returnValue = pathPart.compareTo(comparedPathPart);
        boolean deeperPath = pathArray.length > (index + 1);
        boolean deeperComparedPath = comparedPathArray.length > (index + 1);
        if ((returnValue == 0) && (deeperPath || deeperComparedPath)) {
            if (deeperPath && !deeperComparedPath) {
                returnValue = 1;
            } else if (!deeperPath && deeperComparedPath) {
                returnValue = -1;
            } else {
                index++;
                returnValue = compare(pathArray, comparedPathArray, index);
            }
        }
        return returnValue;
    }

    private int compare(String[] pathArray, String[] comparedPathArray, int index) {
        Integer pathPart = Integer.parseInt(pathArray[index]);
        Integer comparedPathPart = Integer.parseInt(comparedPathArray[index]);
        int returnValue = pathPart.compareTo(comparedPathPart);
        boolean deeperPath = pathArray.length > (index + 1);
        boolean deeperComparedPath = comparedPathArray.length > (index + 1);
        if ((returnValue == 0) && (deeperPath || deeperComparedPath)) {
            if (deeperPath && !deeperComparedPath) {
                returnValue = 1;
            } else if (!deeperPath && deeperComparedPath) {
                returnValue = -1;
            } else {
                index++;
                returnValue = compare(pathArray, comparedPathArray, index);
            }
        }
        return returnValue;
    }

    /**
     * Getter for filterInstanceId.
     *
     * @return the filterInstanceId
     */
    public String getFilterInstanceId() {
        return filterInstanceId;
    }

    /**
     * Setter for filterInstanceId.
     *
     * @param filterInstanceId the filterInstanceId to set
     */
    public void setFilterInstanceId(String filterInstanceId) {
        this.filterInstanceId = filterInstanceId;
    }


    /**
     * Getter for filters.
     *
     * @return the filters
     */
    public List<Filter> getFilters() {
        return filters;
    }


    /**
     * Setter for filters.
     *
     * @param filters the filters to set
     */
    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    /**
     * Getter for hasChildren.
     *
     * @return the hasChildren
     */
    public boolean isHasChildren() {
        return hasChildren;
    }

    /**
     * Setter for hasChildren.
     *
     * @param hasChildren the hasChildren to set
     */
    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    /**
     * Getter for labelKey.
     *
     * @return the labelKey
     */
    public String getLabelKey() {
        return labelKey;
    }

    /**
     * Setter for labelKey.
     *
     * @param labelKey the labelKey to set
     */
    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey;
    }


    /**
     * Getter for descriptionKey.
     * 
     * @return the descriptionKey
     */
    public String getDescriptionKey() {
        return descriptionKey;
    }


    /**
     * Setter for descriptionKey.
     * 
     * @param descriptionKey the descriptionKey to set
     */
    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    /**
     * Getter for classLoader.
     *
     * @return the classLoader
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Setter for classLoader.
     *
     * @param classLoader the classLoader to set
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Getter for bundleFactory.
     * 
     * @return the bundleFactory
     */
    public IBundleFactory getBundleFactory() {
        return bundleFactory;
    }

    /**
     * Setter for bundleFactory.
     * 
     * @param bundleFactory the bundleFactory to set
     */
    public void setBundleFactory(IBundleFactory bundleFactory) {
        this.bundleFactory = bundleFactory;
    }

    /**
     * Getter for parameterNames.
     * 
     * @return the parameterNames
     */
    public Map<String, String> getParameterNames() {
        return parameterNames;
    }

    /**
     * Setter for parameterNames.
     * 
     * @param parameterNames the parameterNames to set
     */
    public void setParameterNames(Map<String, String> parameterNames) {
        this.parameterNames = parameterNames;
    }

}
