/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maht.id_service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maht_
 */
public class KeyStoreLocalHandler {
    private String alias;
    private String encodedPassword;
    private String path;
    private KeyStore keyStore;
    
    public KeyStoreLocalHandler(String path) {
        this.path = path;
    }
    
    public KeyPair getKeyPairFromKeyStore(){
        FileInputStream keyStoreData = null;
        KeyStore.PrivateKeyEntry privateKeyEntry = null;
        java.security.cert.Certificate cert = null;
        char[] keystorePassword = decodePassword().toCharArray();
        KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(keystorePassword); // Key password
        
        try {
            keyStoreData = new FileInputStream(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KeyStoreLocalHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException ex) {
            Logger.getLogger(KeyStoreLocalHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            keyStore.load(keyStoreData, keystorePassword); // Keystore password
        } catch (IOException | NoSuchAlgorithmException | CertificateException ex) {
            Logger.getLogger(KeyStoreLocalHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(getAlias(), keyPassword);
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException ex) {
            Logger.getLogger(KeyStoreLocalHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cert = keyStore.getCertificate(getAlias());
        } catch (KeyStoreException ex) {
            Logger.getLogger(KeyStoreLocalHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        PublicKey publicKey = cert.getPublicKey();
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();
        publicKey = cert.getPublicKey();
        privateKey = privateKeyEntry.getPrivateKey();

        return new KeyPair(publicKey, privateKey);
    }
    
    public String decodePassword(){
        String decodedPassword = null;
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(getEncodedPassword());
        decodedPassword = new String(bytes);
        return decodedPassword;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    private String getAlias() {
        return alias;
    }

    private String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }
    
}
