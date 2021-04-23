package key;

import java.security.*;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import dbConn.MySQLConn;

import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KeyGen {
    //
    //
    private Cipher cipher = null;
    private SecretKey skey = null;
    private KeyPair keyPair = null;
    private PublicKey publicKey = null;
    private PrivateKey privateKey = null;
    private IvParameterSpec ivspec = null;
    private String keyFile = "license";
    private String AESKeyFile = "res\\magicSecret";
    private String licenseKey = "";
    // private KeyPairGenerator generator = null;

    public KeyGen() {
    }
    
    public void init_exec() throws Exception {
        genLicenseKey();
        //uploadToServer();
        keyPair = getKeyPairFromKeyStore();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        //genAESKey();
        printKey();
        encrypt();
        decrypt();
    }

    public void uploadToServer(){
        MySQLConn mysql = new MySQLConn();
        mysql.insertLicense(licenseKey);
    }
    
    @SuppressWarnings("unused")
	private void genAESKey() throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        // Generate AES key
        skey = kgen.generateKey();
        byte[] iv = new byte[128 / 8];
        // Generate rand IV
        new SecureRandom().nextBytes(iv);
        ivspec = new IvParameterSpec(iv);
        // Encrypt the key and save it in a file
        FileOutputStream out = new FileOutputStream(AESKeyFile + ".sys");
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] key = cipher.doFinal(skey.getEncoded());
        // Write the key
        out.write(key);
        // Write the IV
        out.write(iv);
        out.close();
    }

    private void getAESKey() throws Exception {
        // Call and decrypt the AESKeyFile
        FileInputStream in = new FileInputStream(AESKeyFile + ".sys");
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] AESkeyFileBytes = new byte[256];
        // Save the key in a byte array
        in.read(AESkeyFileBytes);
        byte[] keyb = cipher.doFinal(AESkeyFileBytes);
        skey = new SecretKeySpec(keyb, "AES");
        byte[] iv = new byte[128 / 8];
        // Save the IV in a byte array
        in.read(iv);
        in.close();
        ivspec = new IvParameterSpec(iv);
    }

    private void encrypt() throws Exception{
        /* Encrypt and Sign */
        getAESKey();
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey); // Sign using A's private key
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey, ivspec);
        FileOutputStream out = new FileOutputStream(keyFile + ".key");
        byte[] licenseKeyBytes = licenseKey.getBytes();
        sign.update(licenseKeyBytes, 0, licenseKeyBytes.length);

        byte[] outBuf = cipher.doFinal(licenseKeyBytes);
        if (outBuf != null)
            out.write(outBuf);
        out.close();

    }

    private void decrypt() throws Exception{
        /* Decrypt and Authenticate */
        getAESKey();
        Signature ver = Signature.getInstance("SHA256withRSA");
        ver.initVerify(publicKey);
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey, ivspec);
        //FileInputStream in = new FileInputStream(keyFile + ".key");
        byte[] obuf = cipher.doFinal(Files.readAllBytes(Paths.get(keyFile + ".key")));
        String license = new String(obuf, StandardCharsets.UTF_8.name());
        System.out.println(" Recovered key: " + license);
        //in.close();
    }

    private void genLicenseKey() {
        // create a string of all characters
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        // create an object of Random class
        Random random = new Random();
        // specify length of random string
        int length = 5;

        for (int i = 0; i < length; i++) {
            // create random string builder
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < length; j++) {
                // generate random index number
                int index = random.nextInt(alphabet.length());
                // get character specified by index from the string
                char randomChar = alphabet.charAt(index);
                // append the character to string builder
                sb.append(randomChar);
            }
            if (i == length - 1)
                licenseKey += sb.toString();
            else
                licenseKey += sb.toString() + "-";
        }
    }

    public KeyPair getKeyPairFromKeyStore() throws Exception {
        String path = null;
        char[] keyStorePassword = "$t0r#Br34k3r".toCharArray();
        path = System.getProperty("user.dir");     
        path += "\\res\\keystore.jks";
        FileInputStream keyStoreData = new FileInputStream(path);
        
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(keyStoreData, keyStorePassword); // Keystore password
        KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection("$t0r#Br34k3r".toCharArray()); // Key password
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("client", keyPassword);
        java.security.cert.Certificate cert = keyStore.getCertificate("client");     
        PublicKey publicKey = cert.getPublicKey();
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();
        publicKey = cert.getPublicKey();
        privateKey = privateKeyEntry.getPrivateKey();

        return new KeyPair(publicKey, privateKey);
    }
    
    public void printKey() throws Exception {
        System.out.println(" Generated key: " + licenseKey);
    }

}