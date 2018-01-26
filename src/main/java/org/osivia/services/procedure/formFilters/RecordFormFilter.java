package org.osivia.services.procedure.formFilters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.portlet.model.UploadedFile;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import net.sf.json.JSONObject;


/**
 * Helping abstract class for record related form filters
 *
 * @author Dorian Licois
 */
public abstract class RecordFormFilter implements FormFilter {

    /**
     * Constructor.
     */
    public RecordFormFilter() {
        super();
    }


    /**
     * retrieves the PropertyList holding the references of the variables for a startingStep
     *
     * @param startingStep
     * @param properties
     * @return
     */
    protected PropertyList getGlobalVariablesReferences(String startingStep, PropertyMap properties) {
        final PropertyList stepsList = properties.getList("pcd:steps");
        if ((stepsList != null) && CollectionUtils.isNotEmpty(stepsList.list())) {
            for (Object stepO : stepsList.list()) {
                PropertyMap stepM = (PropertyMap) stepO;
                if (StringUtils.equals(stepM.getString("reference"), startingStep)) {
                    return stepM.getList("globalVariablesReferences");
                }
            }
        }
        return null;
    }


    /**
     * Get variables.
     * 
     * @param context form filter context
     * @param globalVariablesReferences global variables
     * @param uploadedFiles uploaded files
     * @return variables
     * @throws PortalException
     */
    protected Map<String, String> getVariables(FormFilterContext context, PropertyList globalVariablesReferences,
            Map<String, UploadedFile> uploadedFiles) throws PortalException {
        Map<String, String> variables = new HashMap<String, String>();

        for (Object variableREfO : globalVariablesReferences.list()) {
            PropertyMap variableREfM = (PropertyMap) variableREfO;
            String variableName = variableREfM.getString("variableName");
            variables.put(variableName, context.getVariables().get(variableName));
        }

        if (MapUtils.isNotEmpty(uploadedFiles)) {
            for (Entry<String, UploadedFile> entry : uploadedFiles.entrySet()) {
                String variableName = entry.getKey();
                UploadedFile uploadedFile = entry.getValue();

                // Temporary file
                File temporaryFile = uploadedFile.getTemporaryFile();

                if (uploadedFile.isDeleted()) {
                    variables.put(variableName, null);
                } else if (temporaryFile != null) {
                    // Digest
                    String digest;
                    try {
                        digest = this.getDigest(temporaryFile);
                    } catch (IOException e) {
                        throw new PortalException(e);
                    }

                    JSONObject object = new JSONObject();
                    object.put("digest", digest);
                    object.put("fileName", uploadedFile.getTemporaryMetadata().getFileName());

                    variables.put(variableName, object.toString());
                }
            }
        }

        return variables;
    }


    /**
     * Get file digest.
     * 
     * @param file file
     * @return digest
     * @throws IOException
     */
    protected String getDigest(File file) throws IOException {
        String digest;

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            digest = DigestUtils.md5Hex(inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return digest;
    }

}
