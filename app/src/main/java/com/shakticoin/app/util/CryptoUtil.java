package com.shakticoin.app.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

public class CryptoUtil {
    private static final String MASTER_ALIAS = "ShaktiMaster";

    /**
     * Return a key pair that is stored in the keystore as MASTER_ALIAS.
     */
    public static KeyPair getMasterKeyPair(Context context) {
        KeyPair keyPair = null;

        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            PrivateKey privateKey = (PrivateKey) keyStore.getKey(MASTER_ALIAS, null);
            Certificate cert = keyStore.getCertificate(MASTER_ALIAS);
            PublicKey publicKey = null;
            if (cert != null) {
                publicKey = keyStore.getCertificate(MASTER_ALIAS).getPublicKey();
            }
            if (privateKey != null && publicKey != null) {
                keyPair = new KeyPair(publicKey, privateKey);
            }

            if (keyPair == null) {
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    initGenerator(generator, MASTER_ALIAS);
                } else {
                    initGenerator(generator, MASTER_ALIAS, context);
                }
                keyPair = generator.generateKeyPair();

            }

        } catch (CertificateException e) {
            Debug.logException(e);
            Debug.logDebug("CertificateException: " + e.getMessage());
        } catch (IOException e) {
            Debug.logException(e);
            Debug.logDebug("IOException: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Debug.logException(e);
            Debug.logDebug("NoSuchAlgorithmException: " + e.getMessage());
        } catch (KeyStoreException e) {
            Debug.logException(e);
            Debug.logDebug("KeyStoreException: " + e.getMessage());
        } catch (UnrecoverableKeyException e) {
            Debug.logException(e);
            Debug.logDebug("UnrecoverableKeyException: " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            Debug.logException(e);
            Debug.logDebug("InvalidAlgorithmParameterException: " + e.getMessage());
        } catch (NoSuchProviderException e) {
            Debug.logException(e);
            Debug.logDebug("NoSuchProviderException: " + e.getMessage());
        }

        return keyPair;
    }

    public static String encryptShortString(@NonNull String source, @NonNull KeyPair keyPair)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] bytes = cipher.doFinal(source.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static String decryptShortString(@NonNull String source, @NonNull KeyPair keyPair)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] bytes = Base64.decode(source, Base64.DEFAULT);
        return new String(cipher.doFinal(bytes));
    }

    private static void initGenerator(KeyPairGenerator generator, String alias, Context context) throws InvalidAlgorithmParameterException {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 20);

        KeyPairGeneratorSpec.Builder builder = new KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSerialNumber(BigInteger.ONE)
                .setSubject(new X500Principal("CN=${alias} CA Certificate"))
                .setStartDate(startDate.getTime())
                .setEndDate(endDate.getTime());
        generator.initialize(builder.build());
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void initGenerator(KeyPairGenerator generator, String alias) throws InvalidAlgorithmParameterException {
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1);
        generator.initialize(builder.build());
    }

}
