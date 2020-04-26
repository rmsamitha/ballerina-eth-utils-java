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

import java.io.PrintStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class BlockChainInterface {
    public static String writeBlockchainRecord(String key, String value, String blockChainAPIURL, String account, String password, String workDirectory) {
        String s = null;

        try {
            Process p = Runtime.getRuntime().exec("geth --unlock \""+ account +"\" --password \"" + password + "\" attach " + blockChainAPIURL);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                s = s.trim();

                if (s.equals("modules: eth:1.0 net:1.0 personal:1.0 rpc:1.0 web3:1.0")) {
                    break;
                }
            }

            OutputStream outStrm = p.getOutputStream();
            PrintStream prtStrm = new PrintStream(outStrm);
            //We need to unlock the account
            prtStrm.println("personal.unlockAccount(eth.accounts[0])");
            prtStrm.flush();

            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);

                if (s.contains("password will be")){
                    break;
                }
            }

            prtStrm.println("1234");
            prtStrm.flush();

            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                if (s.equals("true")) {
                    break;
                }
            }

            prtStrm.println("loadScript(\"" + workDirectory + "/simplestorage.js\")");
            prtStrm.println("\r\n");
            prtStrm.flush();

            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                int index = s.indexOf("Contract mined! address: ");
                if (index != -1) {
                    System.out.println("index:" + index);

                    break;
                }

                index = s.indexOf("exceeds block gas limit undefined");
                if (index != -1) {
                    System.out.println("index:" + index);

                    return "-1";
                }
            }

            s = s.substring(25, 67);

            return s;
        }
        catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}