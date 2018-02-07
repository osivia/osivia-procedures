package org.osivia.services.procedure.portlet.drools;

import org.osivia.portal.api.PortalException;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;

public class Context {

	public String phase;

	public static String PREPARE_PHASE = "prepare";
	public static String APPLY_PHASE = "apply";
	public static String VALIDATE_PHASE = "validate";

	/**
	 * @param msg
	 *            the msg to set
	 * @throws PortalException
	 */
	public void sendError(String msg) throws FormFilterException {
		throw new FormFilterException(msg);
	}

	public Context( String phase) {
		super();

		this.phase = phase;
	}


	/**
	 * @return the phase
	 */
	public String getPhase() {
		return phase;
	}

	/**
	 * @param phase
	 *            the phase to set
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}

}
