
package cryptofiles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class KeyManager {
    
    private PrivateKey PrivKey;
    private PublicKey PubKey;
    
    public KeyManager() throws NoSuchAlgorithmException {
        KeyPairGenerator keysInstance = KeyPairGenerator.getInstance("RSA");
        keysInstance.initialize(4096);
        KeyPair pair = keysInstance.generateKeyPair();
        this.PrivKey = pair.getPrivate();
        this.PubKey = pair.getPublic();
    }
    
    public PrivateKey getPrivKey() {
        return PrivKey;
    }

    public PublicKey getPubKey() {
        return PubKey;
    }
    
    public static PublicKey getPubKey(String PathPublicKey) throws IOException{
        PublicKey publicKey = null;
        try{
//            Obtengo la llave Publica a partir del directorio de la llave 
            String publicKeyContent = new String(Files.readAllBytes(Paths.get(PathPublicKey)));
//            formatea la llave publica
            publicKeyContent = publicKeyContent.replaceAll("\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
//            para las especificaciones de la clave es necesario un array de bytes, asi que lo hacemos directamente aqui
            byte[] encoded = Base64.getMimeDecoder().decode(publicKeyContent);
           
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            publicKey = keyFactory.generatePublic(keySpec);
//            retornamos la llave de una vez
            return publicKey;
        }  catch (NoSuchAlgorithmException e) {
            System.out.println("error "+ e.getMessage());
        } catch (InvalidKeySpecException e) {
            System.out.println("error "+e.getMessage());
        }
        return publicKey;
    }
    
    public static PrivateKey getPrivKey(String PathPrivateKey) throws IOException{
        PrivateKey privateKey = null;
//        Obtengo la llave Privada a partir del directorio de la llave 
        String privateKeyContent = new String(Files.readAllBytes(Paths.get(PathPrivateKey)));
//        hacemos lo mismo de formatear la llave 
        privateKeyContent = privateKeyContent.replaceAll("\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");
        
        byte[] privateKeyEncoded = Base64.getMimeDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyEncoded);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("error "+ e.getMessage());
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            System.out.println("error "+ e.getMessage());
        }
//        devolvemos la llave
        return privateKey;
    }
   
    
    public static void encrypt(String publicKey, String inFile, String outFile) throws IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, FileNotFoundException, IOException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PK CS1Padding");
        
        cipher.init(Cipher.ENCRYPT_MODE, getPubKey(publicKey));
        
        try (FileInputStream in = new FileInputStream(inFile);
                FileOutputStream out = new FileOutputStream(outFile)){
            ProcessFiles.processFileEncrypt(cipher, in, out);
            System.out.println("Se ha cifrado correctamente");
        }
        
    }
    
    
    public static void decrypt(String privateKey, String inFile, String outFile) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, IOException{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        
        cipher.init(Cipher.DECRYPT_MODE, getPrivKey(privateKey));
        
        try (FileInputStream in = new FileInputStream(inFile);
                FileOutputStream out = new FileOutputStream(outFile)){
            ProcessFiles.processFileDecrypt(cipher, in, out);
            System.out.println("Se ha decifrado correctamente");
        }
       
    }
    
}
    
//  public static void decrypt(String base64PrivateKey, String infile, String outfile) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
//      decrypt(getPrivKey(base64PrivateKey), infile, outfile);
//  }
    
    
    
   
