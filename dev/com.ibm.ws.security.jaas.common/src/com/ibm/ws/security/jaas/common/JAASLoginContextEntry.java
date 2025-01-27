/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.security.jaas.common;

import java.util.List;

/**
 *
 */
public interface JAASLoginContextEntry {
    public static final String CFG_KEY_LOGIN_MODULE_REF = "loginModuleRef";
    public static final String CFG_KEY_ID = "id";

    String getId();

    String getEntryName();

    List<JAASLoginModuleConfig> getLoginModules();
}