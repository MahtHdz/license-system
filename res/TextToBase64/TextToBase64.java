package res.TextToBase64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TextToBase64{

    public static void main(String args[]){
        System.out.print( base64Encode(args[0]));
    }
    
    private static String base64Encode(String value) {
        try {
            return Base64.getEncoder()
                        .encodeToString(value.getBytes(StandardCharsets.UTF_8.toString()));        
        } catch(UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
}