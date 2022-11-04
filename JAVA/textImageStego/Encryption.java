package textImageStego;

import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class Encryption {
    private static final String EncryptionScheme = "DESede";
    private static final String UnicodeFormat = "UTF8";
    //private Cipher ciper;
    //private SecretKeyFactory skf;
   // SecretKey Mkey;
    public String Encrypt(String UserPass,String UserString)
    {
        String EncryptedString=null;
        try {
            byte[] arrayBytes = UserPass.getBytes(UnicodeFormat);
            KeySpec ks = new DESedeKeySpec(arrayBytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(EncryptionScheme);
            Cipher ciper = Cipher.getInstance(EncryptionScheme);
            SecretKey Mkey = skf.generateSecret(ks);
            ciper.init(Cipher.ENCRYPT_MODE, Mkey);
            byte[] plainText = UserString.getBytes(UnicodeFormat);
            byte[] encryptedText = ciper.doFinal(plainText);
            EncryptedString = new String(Base64.getEncoder().encodeToString(encryptedText));
            
        } catch (Exception e) {
            System.out.println("ERROR:"+e);
        }
        System.out.println(EncryptedString);
        return EncryptedString;
    }  
    
    public String Decrypt(String UserPass,String encodedString)
    {
        String decryptedString=null;
        try{
            byte[] arrayBytes = UserPass.getBytes(UnicodeFormat);
            KeySpec ks = new DESedeKeySpec(arrayBytes);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(EncryptionScheme);
            SecretKey Mkey = skf.generateSecret(ks);
            Cipher ciper = Cipher.getInstance(EncryptionScheme);
            ciper.init(Cipher.DECRYPT_MODE, Mkey);
            byte[] encryptedText = Base64.getDecoder().decode(encodedString);
            byte[] plainText = ciper.doFinal(encryptedText);
            decryptedString= new String(plainText);
        }
        catch(Exception e)
        {
            System.out.println("ERROR:"+e);
        }
        return decryptedString;
    }
}
