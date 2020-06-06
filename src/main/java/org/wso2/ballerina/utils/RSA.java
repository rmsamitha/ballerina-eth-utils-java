package org.wso2.ballerina.utils;

import com.sun.jersey.core.util.Base64;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA Helper class
 * Generate private/public keys with PEM format
 * Encrypt/Decrypt with public/private key in PEM format
 */

public class RSA {

    /**
     * @param text
     * @return encrypted string
     */
    public static String encrypt(String text, String publicKeyContent) {

        try {
            /*publicKeyContent = new String(Files.readAllBytes(Paths.get("/Users/samithac/Documents/MSC/MCS" +
                    "-Project/Implementation/TEST-KEYS/samitha-public-3.pem")));*/

     /*   String publicKeyContent = new String("-----BEGIN PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUlGPkLobSLz1UKvdowGLH9d67\n" +
                "9z5y/aMRiurXOstqHKmhxMmncH+HUAo0J54i6ZGhsbXkKW0Je7ZazE0GQqu9OjR/\n" +
                "icA6Y0K/crxFdHEPewHzyrotEiBnnHIxM5QLivh9Kp/bNnyPMGweBSlK0vJgKCD0\n" +
                "PlrK6zUE8QAmz+IwvQIDAQAB\n" +
                "-----END PUBLIC KEY-----");*/

            // Modify the private key content format

            if (!publicKeyContent.contains("-----BEGIN PUBLIC KEY-----\n")) {
                String step1 = publicKeyContent.replace("-----BEGIN PUBLIC KEY----- ", "-----BEGIN PUBLIC KEY-----\n");
                if (!step1.contains("\n-----END PUBLIC KEY-----")) {
                    publicKeyContent = step1.replace(" -----END PUBLIC KEY-----", "\n-----END PUBLIC KEY-----");
                }
            }
            System.out.println("Public key to use:" + publicKeyContent);

            // Read PEM Format
            PemReader pemReader = new PemReader(new StringReader(publicKeyContent));
            byte[] content = pemReader.readPemObject().getContent();
            // Get X509EncodedKeySpec format
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(content);

            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKeySecret = kf.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKeySecret);
            byte[] encryptedBytes = cipher.doFinal(text.getBytes());

            return new String(Base64.encode(encryptedBytes));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @return decrypted string
     */
    public static String decrypt(String stringToDecrypt, String privateKeyContent) {

        try {

/*
            privateKeyContent = new String(Files.readAllBytes(Paths.get("/Users/samithac/Documents/MSC/MCS-Project/Implementation/TEST-KEYS/samitha-private-3.pem")));
*/


      /*  String privateKeyContent = new String("-----BEGIN PRIVATE KEY-----\n" +
                "MIICXAIBAAKBgQCUlGPkLobSLz1UKvdowGLH9d679z5y/aMRiurXOstqHKmhxMmncH+HUAo0J54i6ZGhsbXkKW0Je7ZazE0GQqu9OjR/icA6Y0K/crxFdHEPewHzyrotEiBnnHIxM5QLivh9Kp/bNnyPMGweBSlK0vJgKCD0PlrK6zUE8QAmz+IwvQIDAQABAoGAKgaC7qK+Xasg9LKmgkmQZzDDTHVVg578RdT+MdyedqgezvPjeenXGIXqLPBbyVI5b/vdD+krvzfvz2TkEHZBLvjtbKTpMzYYG05a0INJaKYD5GLVy0Ic06/2HJQXucn4lTCje8q0yJQjQDLE1U5qT6nZ0wqob/iivSThPdlvM/0CQQDXXZ5stMimVjF5EOl8X+Hl8QOMTyiR3k39NegZiSh6G0NvdXkjR0lfR25hiKbwdp+42PjupIGgNkTgckIrEIcHAkEAsJzwBhura1CoydC3vIvQTV6vNxwlaXvmCzTgNdaaZpQ8fXdM0YhWVcdryg+XJfOcB1pBC9sYfJPq3ifMRfS1GwJAKfKtMeVAk82EE12s7LHMUTpRg5nDgC35qNFAJQEnJil7SBFsh+eRrgI+yLaSNZIVaC6yxeC68ta/MaxvoO22LwJBAKp3T9UVb+cs+z2QD0gVSwDimwv+Rr4BsJCwgJh2gqDwA1K2i5bSmUJCMW/ejG09LSSLoKzo1EGqI9A5aZk8EHECQDOgRVoigQeyh8gq2qC3Pz0bTBQGujorR380lYsG4WT4kTkCl7MnaNUYbUsfYzTYNpXaf4EHiaLUgzyKlOW+qOI=\n" +
                "-----END PRIVATE KEY-----");*/
            System.out.println("Private Key:" + privateKeyContent);

            // Modify the private key content format

            if (!privateKeyContent.contains("-----BEGIN PRIVATE KEY-----\n")) {
                String step1 = privateKeyContent.replace("-----BEGIN PRIVATE KEY-----", "-----BEGIN PRIVATE KEY-----\n");
                if (!step1.contains("\n-----END PRIVATE KEY-----")) {
                    privateKeyContent = step1.replace("-----END PRIVATE KEY-----", "\n-----END PRIVATE KEY-----");
                }
            }


            // Read PEM Format
            PemReader pemReader = new PemReader(new StringReader(privateKeyContent));
            PemObject pemObject = pemReader.readPemObject();
            pemReader.close();

            // Get PKCS8EncodedKeySpec for decrypt
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pemObject.getContent());
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKeySecret = kf.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKeySecret);
            return new String(cipher.doFinal(Base64.decode(stringToDecrypt)), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "";
    }

    // keys need to be generated from https://www.devglan.com/online-tools/rsa-encryption-decryption and save in
    // files with below format
    //
    // -----BEGIN PUBLIC KEY-----\n
    // key-without-any-new lines \n
    // -----END PUBLIC KEY-----
    // They are not having any new line characters. So no issues
    public static void main(String[] args) {

        try {
            System.out.println("ffff ggg ee");
/*
          String publicKeyContent = new String("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDiXpPqyGslJr7f7X1K/pRMyujlXTEZIek/jbU0TRavh9MwDvHlJKYxv7jaahlq9WITc1/4s/Ukz7qKSt6LUGxh6EWtlYareEVrZgoXmg0h1fzRFe/ejeamE04ZRSrrjvgebq6O9wqhIj3jX4PZ0WDDaQNleYLi5gq11BhfchgT5lVWYJkEVFNBAjgsMcJLbDZ2m59g9Roa/fj4yOanoQnxZpkR01FvKdP+wXOSCyCSDA7Q0IRqxrJP9I7Qpy0BCJAgQs+e4Sa9yLXVZQo8+6e1DUzJfPCGp3yjt8D8vLN6GESf4Lo6yQgNiioCRJifR4Ml+MLK6A7t5EfCT/vDIfWP qsandbox");
*/
            String publicKeyContent = new String(Files.readAllBytes(Paths.get("/Users/samithac/Documents/MSC/MCS" +
                    "-Project/Implementation/TEST-KEYS/samitha-public-3.pem")));
            String encrypted = encrypt("Samitha Hasarali eee", publicKeyContent);
            System.out.println("encrypted:" + encrypted);

//            String privateKeyContent = new String("-----BEGIN PRIVATE KEY-----\n" +
//                    "MIICXAIBAAKBgQCUlGPkLobSLz1UKvdowGLH9d679z5y/aMRiurXOstqHKmhxMmncH+HUAo0J54i6ZGhsbXkKW0Je7ZazE0GQqu9OjR/icA6Y0K/crxFdHEPewHzyrotEiBnnHIxM5QLivh9Kp/bNnyPMGweBSlK0vJgKCD0PlrK6zUE8QAmz+IwvQIDAQABAoGAKgaC7qK+Xasg9LKmgkmQZzDDTHVVg578RdT+MdyedqgezvPjeenXGIXqLPBbyVI5b/vdD+krvzfvz2TkEHZBLvjtbKTpMzYYG05a0INJaKYD5GLVy0Ic06/2HJQXucn4lTCje8q0yJQjQDLE1U5qT6nZ0wqob/iivSThPdlvM/0CQQDXXZ5stMimVjF5EOl8X+Hl8QOMTyiR3k39NegZiSh6G0NvdXkjR0lfR25hiKbwdp+42PjupIGgNkTgckIrEIcHAkEAsJzwBhura1CoydC3vIvQTV6vNxwlaXvmCzTgNdaaZpQ8fXdM0YhWVcdryg+XJfOcB1pBC9sYfJPq3ifMRfS1GwJAKfKtMeVAk82EE12s7LHMUTpRg5nDgC35qNFAJQEnJil7SBFsh+eRrgI+yLaSNZIVaC6yxeC68ta/MaxvoO22LwJBAKp3T9UVb+cs+z2QD0gVSwDimwv+Rr4BsJCwgJh2gqDwA1K2i5bSmUJCMW/ejG09LSSLoKzo1EGqI9A5aZk8EHECQDOgRVoigQeyh8gq2qC3Pz0bTBQGujorR380lYsG4WT4kTkCl7MnaNUYbUsfYzTYNpXaf4EHiaLUgzyKlOW+qOI=\n" +
//                    "-----END PRIVATE KEY-----");

            String privateKeyContent = new String(Files.readAllBytes(Paths.get("/Users/samithac/Documents/MSC/MCS-Project/Implementation/TEST-KEYS/samitha-private-3.pem")));
            String decrypted = decrypt(encrypted, privateKeyContent);
            System.out.println("\n Decrypted:" + decrypted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}