package org.osivia.services.procedure.formFilters;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;

import com.sun.mail.smtp.SMTPTransport;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Send mail filter.
 *
 * @see FormFilter
 */
public class SendMailFilter implements FormFilter {

    /** Logger. */
    private static final Log LOGGER = LogFactory.getLog(SendMailFilter.class);

    /** Identifier. */
    public static final String ID = "SendMailFilter";

    /** Label internationalization key. */
    private static final String LABEL_KEY = "SEND_MAIL_FILTER_LABEL";
    /** Description internationalization key. */
    private static final String DESCRIPTION_KEY = "SEND_MAIL_FILTER_DESCRIPTION";

    /** Body parameter. */
    private static final String BODY_PARAMETER = "body";
    /** "Mail to" parameter. */
    private static final String MAIL_TO_PARAMETER = "mailTo";
    /** "Mail from" parameter. */
    private static final String MAIL_FROM_PARAMETER = "mailFrom";
    /** Mail object parameter. */
    private static final String MAIL_OBJECT_PARAMETER = "mailObject";
    /** Continue workflow even if an error is thrown indicator. */
    private static final String CONTINUE_PARAMETER = "continue";


    /** Internationalization bundle factory. */
    private IBundleFactory bundleFactory;
    /** Notification service. */
    private INotificationsService notificationService;
    
    
    private class SMTPAuthenticator extends Authenticator {
        private final String userName, password;
        
        public SMTPAuthenticator(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }
        
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password);
        }        
     }


    /**
     * Constructor.
     */
    public SendMailFilter() {
        super();

        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());

        // Notification service
        this.notificationService = Locator.findMBean(INotificationsService.class, INotificationsService.MBEAN_NAME);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabelKey() {
        return LABEL_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, FormFilterParameterType> getParameters() {
        Map<String, FormFilterParameterType> parameters = new HashMap<String, FormFilterParameterType>();
        parameters.put(MAIL_TO_PARAMETER, FormFilterParameterType.TEXT);
        parameters.put(MAIL_OBJECT_PARAMETER, FormFilterParameterType.TEXT);
        parameters.put(MAIL_FROM_PARAMETER, FormFilterParameterType.TEXT);
        parameters.put(BODY_PARAMETER, FormFilterParameterType.TEXTAREA);
        parameters.put(CONTINUE_PARAMETER, FormFilterParameterType.BOOLEAN);
        return parameters;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChildren() {
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        // Portal controller context
        PortalControllerContext portalControllerContext = context.getPortalControllerContext();
        
        // Locale
        Locale locale = null;
        if(portalControllerContext.getRequest() != null) {
        	locale = portalControllerContext.getRequest().getLocale();
        }
        
        
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // Parameters
        String mailFromVar = context.getParamValue(executor, MAIL_FROM_PARAMETER);
        String mailToVar = context.getParamValue(executor, MAIL_TO_PARAMETER);
        String mailObjectVar = context.getParamValue(executor, MAIL_OBJECT_PARAMETER);
        String mailBodyVar = context.getParamValue(executor, BODY_PARAMETER);
        boolean continueEvenIfError = BooleanUtils.toBoolean(context.getParamValue(executor, CONTINUE_PARAMETER));

        String logoPath = System.getProperty("osivia.procedure.mail.logo.path");
        logoPath = logoPath.replaceAll("\\$\\{catalina.base\\}", System.getProperty("catalina.base"));
        
        String logoHeight = System.getProperty("osivia.procedure.mail.logo.height");
        String margin = System.getProperty("osivia.procedure.mail.margin");
        String fontFamily = System.getProperty("osivia.procedure.mail.font.family");
        String fontSize = System.getProperty("osivia.procedure.mail.font.size");
        
        // Body
        StringBuilder body = new StringBuilder();
        if( logoPath != null) {
            body.append("<img ");
            if( logoHeight != null)
                body.append("height=\""+logoHeight+"\" ");
  
            body.append("src=\"cid:logo\"><br>");
        }
        
        body.append("<div style=\"");
        
        if( margin != null)    {
            body.append("margin-left:"+margin+"; margin-right:"+margin+"; "); 
        }

        if( fontFamily != null) {
            body.append("font-family:"+fontFamily+"; "); 
        }
        
        if( fontSize != null) {
            body.append("font-size:"+fontSize+"; "); 
        }
        
        
        
        body.append("\">");
        
        
        for (String line : StringUtils.split(mailBodyVar, System.lineSeparator())) {

            body.append(line);
            body.append("<br>");
        }
        

        body.append("</div>");


        // System properties
        Properties properties = System.getProperties();

        
//		mail.transport.protocol=smtp
//		mail.smtp.auth=true
//		mail.smtp.starttls.enable=true
//		mail.smtp.host=smtp.gmail.com
//		mail.smtp.port=587
//		mail.smtp.user=demo@osivia.com
//		mail.smtp.password=demo-osivia
//      mail.replyTo=no-reply-demo@osivia.com

		
		String userName = properties.getProperty("mail.smtp.user");
		String password = properties.getProperty("mail.smtp.password");       

		Authenticator auth = null;
		if( userName != null && password !=null)  {
			auth = new SMTPAuthenticator(userName, password);
		}
			
		
		Session mailSession = Session.getInstance(properties, auth);


        // Message
        MimeMessage message = new MimeMessage(mailSession);

        // "Mail from" address
        InternetAddress mailFromAddr = null;
        if (StringUtils.isNotBlank(mailFromVar)) {
            try {
                mailFromAddr = new InternetAddress(mailFromVar);
            } catch (AddressException e1) {
                throw new FormFilterException(bundle.getString("SEND_MAIL_FILTER_MAILFROM_MISSING_ERROR"));
            }
        }
        
        if (mailFromAddr == null) {
            String defaultFrom = System.getProperty("mail.replyTo");
            if (StringUtils.isNotEmpty(defaultFrom)) {
                try {
                    mailFromAddr = new InternetAddress(defaultFrom);
                } catch (AddressException e1) {
                    throw new FormFilterException(bundle.getString("SEND_MAIL_FILTER_MAILFROM_MISSING_ERROR"));
                }
            }
        }
            
        
        
        
        // "Mail to" address
        InternetAddress[] mailToAddr;
        try {
            mailToAddr = InternetAddress.parse(mailToVar, false);
        } catch (AddressException e1) {
            throw new FormFilterException(bundle.getString("SEND_MAIL_FILTER_MAILTO_MISSING_ERROR"));
        }

        try {
            message.setFrom(mailFromAddr);
            message.setRecipients(Message.RecipientType.TO, mailToAddr);
            message.setSubject(mailObjectVar, "UTF-8");

           
            // Multipart
            Multipart multipart = new MimeMultipart("related");


            
            // Main
            MimeBodyPart mainPart = new MimeBodyPart();
            mainPart.setContent(body.toString(), "text/html; charset=UTF-8");
            multipart.addBodyPart(mainPart);
             
            // Add logo
            if( logoPath != null) {
                MimeBodyPart image = new MimeBodyPart();
                image.attachFile(logoPath);
                image.setHeader("Content-ID", "<logo>");
                multipart.addBodyPart(image);       
            }          

            
            message.setContent(multipart);

            message.setSentDate(new Date());

            if (mailFromAddr != null) {
                InternetAddress[] replyToTab = new InternetAddress[1];
                replyToTab[0] = mailFromAddr;
                message.setReplyTo(replyToTab);
            }
            // SMTP transport
            SMTPTransport transport = (SMTPTransport) mailSession.getTransport();
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException | IOException e) {
            if (continueEvenIfError) {
                // Notification
                String errorMsg = bundle.getString("SEND_MAIL_FILTER_NOTIFICATION_ERROR");
                this.notificationService.addSimpleNotification(portalControllerContext, errorMsg,
                        NotificationsType.ERROR);
                LOGGER.error(errorMsg, e);
            } else {
                // Exception
                throw new FormFilterException(e);
            }
        }
    }
}
