package com.orp.controller.auth;

import com.orp.services.WorkingService;
import com.orp.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SecurityAdvice {
    @Autowired
    private WorkingService workingService;

    @ModelAttribute("hasAccess")
    public boolean addAccessAttribute(Authentication authentication) {
        String username = (authentication != null) ? authentication.getName() : null;
        return (username != null) && workingService.checkRecord(CurrentUserUtils.getStaffInfo().getId());
    }
}
