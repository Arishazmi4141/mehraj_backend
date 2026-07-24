package e_commerce.khilat.util;

import java.security.SecureRandom;

public class Utility {
    
    /**
     * Generates a unique tracking key string.
     * @param length The length of the random string part.
     * @return A string like "TRK-7K2P9W8X"
     */
    
    public static String generateTrackingKey() {
    	
    	int length = 13;
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            int index = CommonConstant.RANDOM.nextInt(CommonConstant.ALPHABET.length());
            sb.append(CommonConstant.ALPHABET.charAt(index));
        }
        
        return  sb.toString();
    }

    

}
