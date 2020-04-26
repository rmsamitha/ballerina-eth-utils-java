/*
*   Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.ballerina.utils;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class EncryptRSAWithPublicKeyPlainText {
    public static String execute(String publicKey, String data) {
        publicKey = "-----BEGIN+RSA+PUBLIC+KEY-----MIIBCgKCAQEAiwh1JK5pgblpHiNX3CycsCYumCvN5hLdLZ00tZ8jUzsDmfQseNk4TxM2pj9Q8NtOro1QxHDMalLbtqpnDbQKdPQm2QvLc3d%2Fzw0BeCNtrEzR0yGjV6WaRzz3b1RrDSmPe27BNIN9qsaABsNSFbAGNsWE8VPsd5bTu2piIoSp8XgMhFCCCBcRNyPdBGoGhiNobdW56qJzSuAjwwx%2B5ug7O2%2FMfReVeYLVUgG7zwuPQqzg9qKj5K3dPA6wOYb5hFQSkEZc%2BhVYXYe6%2FP207hk29JPX%2FOKHe24cXum4BGLPKyzQSqel31GfhkdeIc4AoYFVNKPOsxQeyidFBJ4OsgtucQIDAQAB-----END+RSA+PUBLIC+KEY-----";

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getBytes());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pk = keyFactory.generatePublic(keySpec);

            cipher.init(Cipher.ENCRYPT_MODE, pk);

            String encryptedString = new String(cipher.doFinal(data.getBytes()), "UTF-8");

            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}