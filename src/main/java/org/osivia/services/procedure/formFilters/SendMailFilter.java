package org.osivia.services.procedure.formFilters;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;

import com.sun.mail.smtp.SMTPTransport;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


public class SendMailFilter implements FormFilter {

    public static final String ID = "SendMailFilter";

    public static final String LABEL_KEY = "SEND_MAIL_FILTER_LABEL";

    public static final String DESCRIPTION_KEY = "SEND_MAIL_FILTER_DESCRIPTION";

    public static final String MAILFROM_MISSING_ERROR_KEY = "SEND_MAIL_FILTER_MAILFROM_MISSING_ERROR";

    public static final String MAILTO_MISSING_ERROR_KEY = "SEND_MAIL_FILTER_MAILTO_MISSING_ERROR";

    private static final String body = "body";

    private static final String mailTo = "mailTo";

    private static final String mailFrom = "mailFrom";

    private static final String mailObject = "mailObject";

    private IBundleFactory bundleFactory;


    public SendMailFilter() {
        final IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getLabelKey() {
        return LABEL_KEY;
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    public Map<String, FormFilterParameterType> getParameters() {
        Map<String, FormFilterParameterType> parameters = new HashMap<String, FormFilterParameterType>();
        parameters.put(mailTo, FormFilterParameterType.TEXT);
        parameters.put(mailObject, FormFilterParameterType.TEXT);
        parameters.put(mailFrom, FormFilterParameterType.TEXT);
        parameters.put(body, FormFilterParameterType.TEXTAREA);
        return parameters;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        PortalControllerContext portalControllerContext = context.getPortalControllerContext();

        String mailFromVar = context.getParamValue(executor, mailFrom);
        String mailToVar = context.getParamValue(executor, mailTo);
        String mailObjectVar = context.getParamValue(executor, mailObject);
        String mailBodyVar = context.getParamValue(executor, body);

        // Récupération des propriétés systemes (configurés dans le portal.properties).
        Properties props = System.getProperties();

        Session mailSession = Session.getInstance(props, null);

        // Nouveau message
        final MimeMessage msg = new MimeMessage(mailSession);

        InternetAddress mailFromAddr = null;
        try {
            mailFromAddr = new InternetAddress(mailFromVar);
        } catch (AddressException e1) {
            throw new FormFilterException(bundleFactory.getBundle(portalControllerContext.getRequest().getLocale()).getString(MAILFROM_MISSING_ERROR_KEY));
        }
        InternetAddress[] mailToAddr = null;
        try {
            mailToAddr = InternetAddress.parse(mailToVar, false);
        } catch (AddressException e1) {
            throw new FormFilterException(bundleFactory.getBundle(portalControllerContext.getRequest().getLocale()).getString(MAILTO_MISSING_ERROR_KEY));
        }

        try {
            msg.setFrom(mailFromAddr);
            msg.setRecipients(Message.RecipientType.TO, mailToAddr);
            msg.setSubject(mailObjectVar, "UTF-8");
            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(mailBodyVar, "text/html; charset=UTF-8");
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);
            msg.setSentDate(new Date());
            InternetAddress[] replyToTab = new InternetAddress[1];
            replyToTab[0] = mailFromAddr;
            msg.setReplyTo(replyToTab);
            SMTPTransport t = (SMTPTransport) mailSession.getTransport();
            t.connect();
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();
        } catch (MessagingException e) {
            throw new FormFilterException(e);
        }

    }
}
