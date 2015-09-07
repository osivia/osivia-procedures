/**
 * 
 */
package org.osivia.services.procedure.portlets.form.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyEntry;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyHelper;


/**
 * @author david
 *
 */
public class FormUtils {
    
    /**
     * Utility class.
     */
    private FormUtils(){
        
    }
    
    /**
     * @param mpFile
     * @return a Blob
     * @throws IOException
     */
    public static Blob adaptFile(MultipartFile mpFile) throws IOException{
        Blob blob = null;
        
        if ((mpFile != null) && (mpFile.getSize() > 0)) {
            File attachment = File.createTempFile(mpFile.getName(), null);
            
            attachment.deleteOnExit();
            mpFile.transferTo(attachment);
            
            blob = new FileBlob(attachment);
        }
        
        return blob;
    }
    
    /**
     * @param actorsProperty
     * @return actors as String
     */
    public static String getActors(PropertyList actorsProperty){
        StringBuffer actors = new StringBuffer();
        Iterator<Object> iterator = actorsProperty.list().iterator();
        while(iterator.hasNext()){
            String actor = (String) iterator.next();
            actors.append(actor);
            if(iterator.hasNext()){
                actors.append(",");
            }
        }
        return actors.toString();
    }
    
    /**
     * @param nuxeoController
     * @return VocabularyEntry
     */
    public static VocabularyEntry getVocabulary(NuxeoController nuxeoController, String vocabId){
        List<String> vocabs = new ArrayList<String>(1);
        vocabs.add(vocabId);
        return VocabularyHelper.getVocabularyEntry(nuxeoController, vocabs, false);
    }

}
