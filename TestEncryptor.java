import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
import java.io.UnsupportedEncodingException;
 import java.security.GeneralSecurityException;
 import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 import javax.crypto.Cipher;
 import javax.crypto.SecretKey;
 import javax.crypto.SecretKeyFactory;
 import javax.crypto.spec.DESedeKeySpec;
 import javax.crypto.spec.IvParameterSpec;

 public class TestEncryptor {

     public static void main(String... args) {
         try {
             String KEY_STRING = "asdasdasd";
             byte[] key = getEnKey(KEY_STRING);
             String pFilePath = "C:/Users/Shree/Desktop/Crypt/file10.txt";
             String pFilePathEncryp = "C:/Users/Shree/Desktop/Crypt/Book2.xlsx";
             byte[] archivoDecrypt = encryptFile(pFilePath, key);

             try (FileOutputStream fos = new FileOutputStream(pFilePathEncryp)) {
                 fos.write(archivoDecrypt);
             }
         } catch (IOException e) {
         } catch (GeneralSecurityException e) {
         }

     }

     public static byte[] encryptFile(String pFilePath, byte[] pKey) throws GeneralSecurityException, IOException {
         File file = new File(pFilePath);
         long length = file.length();
         InputStream is = new FileInputStream(file);

         // You cannot create an array using a long type.
         // It needs to be an int type.
         // Before converting to an int type, check
         // to ensure that file is not larger than Integer.MAX_VALUE.
         if (length > Integer.MAX_VALUE) {
             // File is too large
         }

         // Create the byte array to hold the data
         byte[] bytes = new byte[(int) length];

         // Read in the bytes
         int offset = 0;
         int numRead = 0;
         while (offset < bytes.length
                 && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
             offset += numRead;
         }

         // Close the input stream and return bytes
         is.close();

         // Ensure all the bytes have been read in
         if (offset < bytes.length) {

             throw new IOException("Could not completely read file " + file.getName());
         }

         SecretKeyFactory lDESedeKeyFactory = SecretKeyFactory.getInstance("DESede");
         SecretKey kA = lDESedeKeyFactory.generateSecret(new DESedeKeySpec(pKey));

         IvParameterSpec lIVSpec = new IvParameterSpec(new byte[8]);
         Cipher desedeCBCCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

         //desedeCBCCipher.init(Cipher.ENCRYPT_MODE, kA, lIVSpec);
         desedeCBCCipher.init(Cipher.DECRYPT_MODE, kA, lIVSpec);
         byte[] encrypted = desedeCBCCipher.doFinal(bytes);

         return encrypted;
     }

     private static byte[] getEnKey(String spKey) {
         byte[] desKey = null;
         try {
             byte[] desKey1 = md5(spKey);
             desKey = new byte[24];
             int i = 0;
             while (i < desKey1.length && i < 24) {
                 desKey[i] = desKey1[i];
                 i++;
             }
             if (i < 24) {
                 desKey[i] = 0;
                 i++;
             }
         } catch (Exception e) {
             e.printStackTrace();
         }

         return desKey;
     }

     private static byte[] md5(String strSrc) {
         byte[] returnByte = null;
         try {
             MessageDigest md5 = MessageDigest.getInstance("MD5");
             returnByte = md5.digest(strSrc.getBytes("GBK"));
         } catch (UnsupportedEncodingException e) {
         } catch (NoSuchAlgorithmException e) {
         }
         return returnByte;
     }
 }