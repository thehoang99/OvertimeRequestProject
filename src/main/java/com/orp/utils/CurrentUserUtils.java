package com.orp.utils;

import com.orp.model.Staff;
import com.orp.utils.auth.CustomUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserUtils {
    public static Staff getStaffInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetail userDetail) {
            return userDetail.getStaffInfo();
        }
        throw new IllegalStateException("User is not authenticated or user details are not an instance of CustomUserDetail.");
    }
}
