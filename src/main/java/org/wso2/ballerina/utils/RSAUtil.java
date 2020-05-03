package org.wso2.ballerina.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.interfaces.RSAPublicKey;
public class RSAUtil {

//    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
    private static String publicKey =
        "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxjA4bZ0UENjhHMTgBR27Re7I+Ge836dWYkGSuXukwTdjF0OyQT/tEO43RJuQL8L3tgbDkZQ3wwX1I4Ail/u9sIINnONKVppzUASzCPWuEYy2aMvfcrqn0+PwkpwkAaCOEh9cbDQ+w/CtNdU+rrmabcVSR96ki5jAoIGsQ1q3sBVKrSr+jKIprNeTkvyyCwBEZrHsX+vrc7bXTXpeAlT7zeEisFLXuKBiELLHC4aopbxzFBj1NhvWs0krmWnhi6hP+EvsLHGx7mjgPaKGRmV49XYIPo7qX9DF0w89zINpJ2rkXF36q8aTq9Tddje/bgAugyBWYy1mIYAsVsMpw1BsVQIDAQAB";
//    private static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAUZV+tjiNBKhlBZbKBnzeugpdYPhh5PbHanjV0aQ+LF7vetPYhbTiCVqA3a+Chmge44+prlqd3qQCYra6OYIe7oPVq4mETa1c/7IuSlKJgxC5wMqYKxYydb1eULkrs5IvvtNddx+9O/JlyM5sTPosgFHOzr4WqkVtQ71IkR+HrAgMBAAECgYAkQLo8kteP0GAyXAcmCAkA2Tql/8wASuTX9ITD4lsws/VqDKO64hMUKyBnJGX/91kkypCDNF5oCsdxZSJgV8owViYWZPnbvEcNqLtqgs7nj1UHuX9S5yYIPGN/mHL6OJJ7sosOd6rqdpg6JRRkAKUV+tmN/7Gh0+GFXM+ug6mgwQJBAO9/+CWpCAVoGxCA+YsTMb82fTOmGYMkZOAfQsvIV2v6DC8eJrSa+c0yCOTa3tirlCkhBfB08f8U2iEPS+Gu3bECQQCrG7O0gYmFL2RX1O+37ovyyHTbst4s4xbLW4jLzbSoimL235lCdIC+fllEEP96wPAiqo6dzmdH8KsGmVozsVRbAkB0ME8AZjp/9Pt8TDXD5LHzo8mlruUdnCBcIo5TMoRG2+3hRe1dHPonNCjgbdZCoyqjsWOiPfnQ2Brigvs7J4xhAkBGRiZUKC92x7QKbqXVgN9xYuq7oIanIM0nz/wq190uq0dh5Qtow7hshC/dSK3kmIEHe8z++tpoLWvQVgM538apAkBoSNfaTkDZhFavuiVl6L8cWCoDcJBItip8wKQhXwHp0O3HLg10OEd14M58ooNfpgt+8D8/8/2OOFaR0HzA+2Dm";
    private static String privateKey =
        "TmDRdmnPEeTVzsQkntj4hw926DY5Yik9IjYvuKiS3FvUAfcOmetO1tfHDqlFBOEjRgdlY5BLic3c4QEkmVgDxV0nd1i2S902cqMgtDYt7ydOq83ecgOgijQMzrnb+ADdeQSoE/A63Y/Rr0/wEhgzXX141RlN4QAB6/MgFPCZPf9KVGq6TRYWJPIMBnnJcvn16Zx7WN/b2KpO5PXgWViqW3JPOu57okd6sDKWcsoQNMl4ZNX4OnzKMgWLHm3cqPJZ0Qrdwgsl0BeUXAd+ZevpftB7F6Dyr1rijG0DDrdZRloqpH6U1TPLgKjlHxgbWGy5SwdtgjoRy77QzR6OLnp9yT5R/BuDXyKeCbOR5VHf+O8qhETrGqPpBLtDoiHNKgOwU3vzUrzSnHgFh3ioYXPRHZk2AxgwxITUJI+WTVxNysOK3LSQ7g0pHnwjnElY0pZF93njXFaSKFpDuf9izd5aBfeAsw/kcQrCnMp6kzrH4Fx1BT7IPTNiiYo7kY/NoJP5wZukalb7s2PgPIGl5sl2bcGwnfv6dongBEq4iQxcDZqSosvi/rRYqjIQy/j54Z6iEn/wSPJwCszhh/R8HxQCt3IpQkMIOT2zfP+m0gnLaF3OFH6H7B9I4wdGc5UQHcNkj5xqkbtjJ9btswUAK6kAY1AAMAbwUcPhoAqyI7lYBOw7giA/PeDEW1Wy6s9KWX4fX0PekDomiiBagOeE+822X48eCyDMmXoGyMUBArQUutE2Mr+GxETRvVjgdQ0Cxv/urFMMQD10eCa04qxmdGISuyGtCIswxksGhRoI9nYFV3upB58iKrMrcqQmmmbzN8jzmhiEjy02Ac1fiOmtDujVOtks5mpcVJozIrPcmBn8dLWwoE7uukaU8UT1fnyiO1nre3mpNJOKfCewiN+4RdkvS/BdzyuPEIJPlrZbjXVUqwoETdIj7UMll8zoz4eDct9yLeMWnqm9lUvnUJf/VichSq/86vFb0qvS/W5Q796vQYGJMgdnhA3F3t9qThfwftYIYRxeMGkC0m/DkHgkc9JDulHwwA1Ul5uTeJPX1zNukr5chiG9Sb4Uwi7Kx9m9OIvSDCT5kytD/19MdInolb0PtDVccmXj8Zf46Y0lsS8I/LaskGYO0EKXh/apGogXMnmZ6WbyvQz8xJAc4Pf1OkeiCU4yp5oYLOvNu3xr4jJTG9J+EwW/oNHX0fuyuhTJ0VgwZwJ5LKaNRYQMctsVz2chzL45Wh7X/b7sSHbbGRMJUidaT2JoUjss0Ja9Cw9Nl4j57e0+bi5/Qpiaa6sOVKsLtt5u39XEoCdaOQuTxIl5cm0xsDTydlV4OJ7jsNmSH2AxW6Lazb4ZN4DPUgePQyZe3GWBPj6aLxjRB49IlZozItj5Q6ACw/J8xmb/cG+YfGS5JOo8YMxBs2vaAwoyavhUqnjGLhh3LuoCsrvFQlNdOfbwAMBbZgNDQD6KsCV6rZzS2aFKksnJVH7iwxYtfKWLgQYiPBKqu/V/+kIjGXrX7mrP18DDgdPE+Sx/CUCdhL62lzAXCI2F+rp9OZCy/5u72tytnZTzIjeyDI9mF6R4IDs9kzlDDxOhom6UzU4gzxri" ;

    public  static void convertKeys() throws NoSuchAlgorithmException, InvalidKeySpecException, URISyntaxException, IOException {
        String privateKeyContent = new String(Files.readAllBytes(Paths.get("/Users/samithac/Documents/MSC/MCS-Project" +
                "/Implementation/TEST-KEYS/samitha-new-private.pem")));
        String publicKeyContent = new String(Files.readAllBytes(Paths.get("/Users/samithac/Documents/MSC/MCS-Project" +
                "/Implementation/TEST-KEYS/samitha-new-public.pem")));

        privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
        publicKeyContent = publicKeyContent.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");;

        KeyFactory kf = KeyFactory.getInstance("RSA");

        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
        PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);

        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);

        System.out.println(privKey);
        System.out.println(pubKey);
    }


    public static PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
    }

    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
        try {
            convertKeys();
            String encryptedString = Base64.getEncoder().encodeToString(encrypt("Dhiraj is the author", publicKey));
            System.out.println(encryptedString);
            String decryptedString = RSAUtil.decrypt(encryptedString, privateKey);
            System.out.println(decryptedString);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | URISyntaxException | IOException e) {
            System.err.println(e.getMessage());
        }

    }
}