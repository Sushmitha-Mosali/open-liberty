/*******************************************************************************
 * Copyright (c) 2022 IBM Corporation and others.
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
package io.openliberty.security.jakartasec.tokens;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.junit.Test;

import jakarta.security.enterprise.authentication.mechanism.http.openid.OpenIdConstant;
import jakarta.security.enterprise.identitystore.openid.AccessToken;
import jakarta.security.enterprise.identitystore.openid.AccessToken.Type;
import jakarta.security.enterprise.identitystore.openid.JwtClaims;

/**
 * Serialization test for the AccessToken.
 */
public class AccessTokenImplSerializationTest {

    public static final String JWT_ACCESS_TOKEN_STRING = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKYWNrc29uIiwiYXRfaGFzaCI6ImJrR0NWcy1EcndMMGMycEtoN0ZVNGciLCJyZWFsbU5hbWUiOiJCYXNpY1JlZ2lzdHJ5IiwidW5pcXVlU2VjdXJpdHlOYW1lIjoiSmFja3NvbiIsInNpZCI6InFTTzBXeWs0VVNjMWFCYlMyUVlmIiwiaXNzIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6OTQ0My9vaWRjL2VuZHBvaW50L09QIiwiYXVkIjoib2lkY2NsaWVudCIsImV4cCI6MTY2MTIwNzIwOCwiaWF0IjoxNjYxMjAwMDA4fQ.a4PRKYeG18vsmBOukcjmNve10KnVSBGVgwh2RqXkNbY";

    private static final String SERIALIZED_FILE_VERS_1 = "test-resources/testdata/ser-files/AccessTokenImpl_1.ser";

    @Test
    public void testAccessTokenSerialization_Ver1() throws Exception {
        /*
         * Deserialize the object from the serialized file.
         */
        AccessTokenImpl object = null;
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(SERIALIZED_FILE_VERS_1))) {
            object = (AccessTokenImpl) input.readObject();
        }
        assertNotNull("AccessTokenImpl instance could not be read from the serialized file.", object);

        verifyAccessTokenContents(object);
    }

    public static void verifyAccessTokenContents(AccessTokenImpl object) {
        /*
         * Verify the data from the instance matches the expected.
         */
        assertEquals("The getToken() method returned an unexpected value.", JWT_ACCESS_TOKEN_STRING, object.getToken());

        /*
         * Walk though the claimsMap contents
         */
        Map<String, Object> claimsMap = object.getClaims();
        assertNotNull("The claimsMap is null", claimsMap);
        /*
         * Expected contents for claimsMap: sub=Jackson, at_hash=bkGCVs-DrwL0c2pKh7FU4g, realmName=BasicRegistry, uniqueSecurityName=Jackson, sid=qSO0Wyk4USc1aBbS2QYf,
         * iss=https://localhost:9443/oidc/endpoint/OP, aud=oidcclient, exp=1661207208, iat=1661200008]
         */
        assertEquals("The claimsMap has an unexpected value for sub", AccessTokenImplTest.SUBJECT_IN_ACCESS_TOKEN, claimsMap.get(OpenIdConstant.SUBJECT_IDENTIFIER));
        assertEquals("The claimsMap has an unexpected value for at_hash", "bkGCVs-DrwL0c2pKh7FU4g", claimsMap.get("at_hash"));
        assertEquals("The claimsMap has an unexpected value for realmName", "BasicRegistry", claimsMap.get("realmName"));
        assertEquals("The claimsMap has an unexpected value for uniqueSecurityName", AccessTokenImplTest.SUBJECT_IN_ACCESS_TOKEN, claimsMap.get("uniqueSecurityName"));
        assertEquals("The claimsMap has an unexpected value for sid", "qSO0Wyk4USc1aBbS2QYf", claimsMap.get("sid"));
        assertEquals("The claimsMap has an unexpected value for iss", "https://localhost:9443/oidc/endpoint/OP", claimsMap.get("iss"));
        assertEquals("The claimsMap has an unexpected value for aud", "oidcclient", claimsMap.get("aud"));
        assertEquals("The claimsMap has an unexpected value for exp", 1661207208L, claimsMap.get("exp"));
        assertEquals("The claimsMap has an unexpected value for iat", 1661200008L, claimsMap.get("iat"));

        /*
         * Spot check the jwtClaims map
         */

        JwtClaims jwtClaims = object.getJwtClaims();
        assertNotNull("The jwtClaims map is null, ", jwtClaims);
        assertEquals("The JwtClaimsMap has an unexpected value for sub", AccessTokenImplTest.SUBJECT_IN_ACCESS_TOKEN, jwtClaims.getSubject().get());
        assertEquals("The JwtClaimsMap has an unexpected value for iss", "https://localhost:9443/oidc/endpoint/OP", jwtClaims.getIssuer().get());

        assertNull("The scope was note set", object.getScope());
        assertTrue("Token should be expired", object.isExpired());
        assertTrue("Token should be a jwt", object.isJWT());
        assertEquals("The getExpirationTime() method returned an unexpected value", AccessTokenImplTest.ONE_HOUR, object.getExpirationTime());
        assertEquals("The getType() method returned an unexpected value", Type.BEARER, object.getType());

    }

    /**
     * Method used to create and serialize the AccessTokenImpl for testing.
     *
     * If AccessTokenImpl changes, previously serialized versions of
     * AccessTokenImpl must remain deserializable. Use this method to
     * create a new AccessTokenImpl_x.ser file, replacing the x with the
     * current version + 1. Then write a test that deserializes that version and
     * all previous AccessTokenImpl_x.ser files.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Create a serialized AccessToken file");
        final String filename = "test-resources/testdata/ser-files/AccessTokenImpl_x.ser"; // change x to a version number now, or after creation

        Map<String, Object> accessTokenClaimsMap = AccessTokenImplTest.createClaimsMap(JWT_ACCESS_TOKEN_STRING);

        AccessToken jwtAccessTokenToWrite = new AccessTokenImpl(JWT_ACCESS_TOKEN_STRING, accessTokenClaimsMap, AccessTokenImplTest.A_MINUTE_AGO, AccessTokenImplTest.ONE_HOUR, AccessTokenImplTest.TOKEN_MIN_VALIDITY_10_MILLIS);

        /*
         * Serialize the object to a file.
         */
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename))) {
            output.writeObject(jwtAccessTokenToWrite);
        }
    }
};