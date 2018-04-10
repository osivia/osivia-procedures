<%@tag import="fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext"%>
<%@tag import="org.osivia.portal.api.cache.services.CacheInfo"%>
<%@tag import="fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext"%>
<%@tag import="fr.toutatice.portail.cms.nuxeo.api.NuxeoController"%>
<%@ tag import="javax.portlet.PortletContext"%>
<%@ tag import="javax.portlet.PortletConfig"%>

<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="webId" required="true" %>


<%
String webId = (String) jspContext.getAttribute("webId");

PortletConfig portletConfig = (PortletConfig) request.getAttribute("javax.portlet.config");
PortletContext portletContext = portletConfig.getPortletContext();

NuxeoController nuxeoController = new NuxeoController(portletContext);
nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);

NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(webId);
String title = documentContext.getDocument().getTitle();

jspContext.setAttribute("title", title);
%>


<span>${title}</span>
