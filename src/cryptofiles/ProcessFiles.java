/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cryptofiles;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 *
 * @author Migue
 */
public class ProcessFiles {
    
    static public void processFileEncrypt(Cipher cipher, FileInputStream inputFile, FileOutputStream outFile) throws IOException, IllegalBlockSizeException, BadPaddingException{
        byte[] buff = new byte[501];
        int length;
        while((length = inputFile.read(buff)) != -1){
            System.out.println(length);
            byte[] outBuff = cipher.doFinal(buff, 0, length);
            outFile.write(outBuff);
        }
    }
    
    static public void processFileDecrypt(Cipher cipher, FileInputStream inputFile, FileOutputStream outFile) throws IOException, IllegalBlockSizeException, BadPaddingException{
        byte[] buff = new byte[512];
        int length;
        while((length = inputFile.read(buff)) != -1){
            byte[] outBuff = cipher.doFinal(buff, 0, length);
            outFile.write(outBuff);
        }
    }
    }
