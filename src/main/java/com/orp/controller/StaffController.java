package com.orp.controller;

import com.orp.dto.StaffDetailInfoDTO;
import com.orp.dto.StaffIdNameDTO;
import com.orp.model.Department;
import com.orp.model.Role;
import com.orp.model.Staff;
import com.orp.services.DepartmentService;
import com.orp.services.RoleService;
import com.orp.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
        createAndUpdateStaffExtract(model, staff);
        return "view/staff/create";
    }

    @PostMapping("/create")
    public String createStaffDB(
            @ModelAttribute(name = "staff") Staff staff,
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
            createAndUpdateStaffExtract(model, staff);
            model.addAttribute("errorMsg", "There are a few errors during employee creation!");
            return "view/staff/create";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Create a new Staff successfully!");
        return "redirect:/staff/view";
    }

    @GetMapping("/view")
    public String viewStaff(Model model) {
        List<StaffIdNameDTO> staffNameList = staffService.findAllName();
        if (!staffNameList.isEmpty()) {
            model.addAttribute("nameList", staffNameList);
        }
        return "view/staff/view";
    }

    @GetMapping("/viewDetail")
    public String staffDetailInfo(
            @RequestParam(name = "id", required = false) Integer id,
            Model model
    ) {
        if (id != null) {
            StaffDetailInfoDTO staffDetailInfoDTO = staffService.findStaffDetailInfoById(id);
            if (staffDetailInfoDTO != null) {
                model.addAttribute("staffDetail", staffDetailInfoDTO);
            }
        }
        return "view/staff/viewDetail";
    }

    @GetMapping("/update")
    public String updateStaffUI(
            @RequestParam(name = "id") Integer id,
            Model model
    ) {
        Staff updateStaff = staffService.findById(id);
        createAndUpdateStaffExtract(model, updateStaff);
        return "view/staff/update";
    }

    @PostMapping("/update")
    public String updateStaffDB(
            @ModelAttribute(name = "staff") Staff staff,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ){
        boolean isError = false;
        if (result.hasErrors()) {
            isError = true;
        } else {
            Staff staffDB = staffService.update(staff);
            if (staffDB == null) {
                isError = true;
            }
        }

        if (isError) {
           createAndUpdateStaffExtract(model, staff);
            model.addAttribute("errorMsg", "There are a few errors during employee update process!");
            return "view/staff/update";
        }

        redirectAttributes.addFlashAttribute("successMsg", "Update the Staff successfully!");
        return "redirect:/staff/view";
    }

    private void createAndUpdateStaffExtract(Model model, Staff staff) {
        List<Department> departments = departmentService.findAll();
        List<Role> roles = roleService.findAll();

        model.addAttribute("staff", staff);
        model.addAttribute("departments", departments);
        model.addAttribute("roles", roles);
    }
}
