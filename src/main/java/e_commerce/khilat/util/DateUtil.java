package e_commerce.khilat.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	
//	public static void main(String argd[]) {
//		
//		LocalDateTime createdAt = LocalDateTime.of(
//                2026, 3, 3,
//                21, 34, 23, 158370000   
//        );
//		
//		DateUtil.dateConverterToLong(createdAt);
//		
//		
//	}
	
	
	public static Long dateConverterToLong(LocalDateTime timestamp) {
		
		String formattedDate = timestamp.format(
                DateTimeFormatter.ofPattern("yyyyMMdd")
        );

        Long result = Long.valueOf(formattedDate);
        
        return result;
    }

}
