/*******************************************************************************
 * Copyright (c) 2010, 2019 IBM Corporation and others.
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
package com.ibm.ejb2x.ejbinwar.intf;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface Stateless2xRemote extends EJBObject {
    String test(String s) throws RemoteException;

    void testStatelessLocal() throws RemoteException;

    void testStatefulLocal() throws RemoteException;
}
