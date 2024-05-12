package com.orp.controller;

import com.orp.utils.CurrentUserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {
        String currentStaffName = CurrentUserUtils.getStaffInfo().getName();
        model.addAttribute("staffName", currentStaffName);
        return "index";
    }
}
