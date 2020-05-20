/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Gradebook;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 *
 * @author Eric
 */
public class CryptoInputStream extends InputStream {
    
    private InputStream m_stream = null;
    private String m_password = "";
    private byte[] m_decryptedBuffer;
    private int m_bytesRead = 0;
    private int m_bufferIndex = 0;
    
    public CryptoInputStream(InputStream stream, String password) {
       
        try {
            m_stream = stream;
            byte[] buffer = new byte[1048576];
            m_bytesRead = m_stream.read(buffer);
            m_password = password;
            Cipher cipher = makeCipher(m_password);
            
            m_decryptedBuffer = cipher.doFinal(buffer, 0, m_bytesRead);
        } 
        catch (Exception e) {
        }
    }

    @Override
    public int read() throws IOException {
        
        if (m_bufferIndex < m_bytesRead) {
            return m_decryptedBuffer[m_bufferIndex++];
        }
        return -1;
    }
    
    private final byte[] salt = {
        (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,
        (byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17 
    };
    
    private Cipher makeCipher(String password) throws GeneralSecurityException{

        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 42);
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.DECRYPT_MODE, key, pbeParamSpec);

        return cipher;
    }
}
