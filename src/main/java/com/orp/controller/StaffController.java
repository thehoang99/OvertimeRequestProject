package com.orp.controller;

import com.orp.model.Department;
import com.orp.model.Role;
import com.orp.model.Staff;
import com.orp.services.DepartmentService;
import com.orp.services.RoleService;
import com.orp.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/create")
    public String createStaffUI(Model model) {
        Staff staff = new Staff();
        createStaffExtract(model, staff);
        return "view/staff/create";
    }

    @PostMapping("/create")
    public String createStaffDB(
            @ModelAttribute(name = "newStaff") Staff staff,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        boolean isError = false;
        if (result.hasErrors()) {
            isError = true;
        } else {
            Staff staffDB = staffService.save(staff, result);
            if (staffDB == null) {
                isError = true;
            }
        }

        if (isError) {
            createStaffExtract(model, staff);
            model.addAttribute("errorMsg", "Create a new Staff is failed!");
            return "view/staff/create";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Create a new Staff successfully!");
        return "redirect:/staff/view";
    }

    private void createStaffExtract(Model model, Staff staff) {
        List<Department> departments = departmentService.findAll();
        List<Role> roles = roleService.findAll();

        model.addAttribute("newStaff", staff);
        model.addAttribute("departments", departments);
        model.addAttribute("roles", roles);
    }
}
