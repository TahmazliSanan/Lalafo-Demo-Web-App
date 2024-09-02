package org.pronet.lalafodemo.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TokenUtil {
    public static String generateResetToken() {
        return UUID
                .randomUUID()
                .toString();
    }

    public static Date calculateTokenExpiryDate(Integer expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return calendar.getTime();
    }
}
