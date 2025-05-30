package org.zionusa.management.util;

import org.zionusa.base.enums.EUserAccess;
import org.zionusa.base.enums.EUserRole;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SecurityUtil {
    
    public static String encrypt(String field, String key) {
        String data = "";

        if (field != null) {
            try {
                SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
                Cipher cipher = Cipher.getInstance("Blowfish");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                byte[] encrypted = cipher.doFinal(field.getBytes());
                data = Base64.getEncoder().withoutPadding().encodeToString(encrypted);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String decrypt(String encryptedField, String key) {
        String data = "";

        if (encryptedField != null) {
            try {
                SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "Blowfish");
                Cipher cipher = Cipher.getInstance("Blowfish");
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                byte[] decrypted = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedField));
                data = new String(decrypted);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static boolean isAuthenticatedUserRoleHigher(String authAccess, String userRole) {
        if (EUserAccess.ADMIN.is(authAccess)) {
            return true;
        } else if (EUserAccess.OVERSEER.is(authAccess)) {
            return !EUserRole.ADMIN.is(userRole) && !EUserRole.OVERSEER.is(userRole);
        } else if (EUserAccess.CHURCH.is(authAccess)) {
            return !EUserRole.ADMIN.is(userRole) && !EUserRole.OVERSEER.is(userRole) && !EUserRole.CHURCH_LEADER.is(userRole);
        } else if (EUserAccess.GROUP.is(authAccess)) {
            return EUserRole.TEAM_LEADER.is(userRole) || EUserRole.MEMBER.is(userRole);
        } else if (EUserAccess.TEAM.is(authAccess)) {
            return EUserRole.MEMBER.is(userRole);
        }
        return false;
    }

    public static boolean isAuthenticatedUserAccessHigher(String authAccess, String userAccess) {
        if (EUserAccess.ADMIN.is(authAccess)) {
            return true;
        } else if (EUserAccess.OVERSEER.is(authAccess)) {
            return !EUserAccess.ADMIN.is(userAccess);
        } else if (EUserAccess.CHURCH.is(authAccess)) {
            return !EUserAccess.ADMIN.is(userAccess) && !EUserAccess.OVERSEER.is(userAccess);
        } else if (EUserAccess.GROUP.is(authAccess)) {
            return !EUserAccess.ADMIN.is(userAccess) && !EUserAccess.OVERSEER.is(userAccess) && !EUserAccess.CHURCH.is(userAccess);
        } else if (EUserAccess.TEAM.is(authAccess)) {
            return EUserAccess.MEMBER.is(userAccess) || EUserAccess.TEAM.is(userAccess);
        }
        return false;
    }
}
