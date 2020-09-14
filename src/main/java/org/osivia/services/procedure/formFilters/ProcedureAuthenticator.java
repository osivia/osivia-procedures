/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2011, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package org.osivia.services.procedure.formFilters;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class ProcedureAuthenticator extends Authenticator {

  String username;
  String password;
  
  ProcedureAuthenticator(String username, String password) {
    this.username = username;
    this.password = password;
  }
  
  public PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(username, password);
}

}