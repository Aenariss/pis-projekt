/**
 * PIS Projekt 2024
 * Request.java
 * @author Vojtech Fiala <xfiala61>
 */


package pis.api.dto;

import java.lang.reflect.Modifier;
import java.lang.reflect.Field;

/**
 * Class to extend all child requests classes - proviodes the valid() methodd to check if none fields are equal to null
 */
public class Request {

    public Request() {}

    public boolean valid() {
        for (Field field : this.getClass().getDeclaredFields()) {
            int access = field.getModifiers();
            boolean revert = false;
            if (Modifier.isPrivate(access)) {
                revert = true;
            }

            field.setAccessible(true); // cant access if private, allow it temporarily
            try {
                Object value = field.get(this);
                if (value == null) {
                    return false;
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }
            if (revert) {
                field.setAccessible(false);
            }
        }
        return true;
    }
}
