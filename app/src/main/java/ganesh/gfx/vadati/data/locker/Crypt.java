package ganesh.gfx.vadati.data.locker;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;

public class Crypt {

    private PrivateKey privateChavi;
    private PublicKey publicChavi;
    private Cipher cipher;

    public Crypt(Chabi chabi) {
       privateChavi = chabi.getPrivateChavi();
       publicChavi = chabi.getPublicChavi();
    }
    public Crypt() {
    }
    public byte[] code(String plainMessage,PublicKey publicKey) throws Exception{
        byte[] messageToByte = plainMessage.getBytes();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);

        //SealedObject myEncryptedMessage = new SealedObject(plainMessage, cipher);
        //SealedObject myEncryptedMessage= new SealedObject( myMessage, c);

        byte[] coded = cipher.doFinal(messageToByte);

        return coded;

        //return Base64.getEncoder().encodeToString(coded);

    }

    public String deCode(byte[] codedMessage) throws Exception{

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE,privateChavi);
        byte[] deCodedMessageBytes = cipher.doFinal(codedMessage);

        return new String(deCodedMessageBytes,"UTF8");
    }
}