/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Gradebook;

import java.io.IOException;
import java.io.OutputStream;
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
public class CryptoOutputStream extends OutputStream {
    
    private OutputStream m_stream = null;
    private String m_password = "";
    private byte[] m_buffer = null;
    private int m_bufferIndex = 0;
    
    public CryptoOutputStream(OutputStream stream, String password) {
        m_stream = stream;
        m_buffer = new byte[1048576];
        m_password = password;
    }

    @Override
    public void write(int b) throws IOException {
        m_buffer[m_bufferIndex++] = (byte)b;
        if (m_bufferIndex >= m_buffer.length) {
            flush();
        }
    }
    
    @Override
    public void flush() throws IOException {
        
        try {
            if (m_bufferIndex > 0) {
                Cipher cipher = makeCipher(m_password);

                byte[] encryptedData = cipher.doFinal(m_buffer, 0, m_bufferIndex);
                m_stream.write(encryptedData);
            }
        } 
        catch (Exception e) {
        }
        
        m_bufferIndex = 0;
        m_stream.flush();
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
        cipher.init(Cipher.ENCRYPT_MODE, key, pbeParamSpec);

        return cipher;
    }
}
