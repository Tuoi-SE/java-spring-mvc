package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import vn.hoidanit.laptopshop.domain.Role;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    // Display Data
    @RequestMapping("/admin/user")
    public String getTableUser(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("displayUser", users);
        // model.addAttribute("createUser", new User());
        return "/admin/user/home";
    }

    // Create Data
    @GetMapping("/admin/user/create")
    public String getCreateUser(Model model) {
        model.addAttribute("createUser", new User());
        return "/admin/user/create";
    }

    @PostMapping(value = "/admin/user/create")
    public String createCreateUser(Model model,
            @ModelAttribute("createUser") @Valid User tuoi, BindingResult newUserBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {
        List<FieldError> errors = newUserBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }
        // validate
        if (newUserBindingResult.hasErrors()) {
            return "/admin/user/create";
        }
        String avatar = this.uploadService.savaUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(tuoi.getPassword());
        tuoi.setAvatar(avatar);
        tuoi.setPassword(hashPassword);
        tuoi.setRole(this.userService.getRoleName(tuoi.getRole().getName()));
        this.userService.saveUser(tuoi);
        return "redirect:/admin/user";
    }

    // View Detail Data
    @RequestMapping("/admin/user/{id}")
    public String getDetailUser(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "/admin/user/detail";
    }

    // Update Data
    @RequestMapping("/admin/user/update/{id}")
    public String getUpdateUser(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("updateUser", user);
        return "/admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String updateUpdateUser(Model model, @ModelAttribute("createUser") User user,
            @RequestParam("hoidanitFile") MultipartFile file) {
        User update = this.userService.getUserById(user.getId());
        if (update != null) {
            update.setFullName(user.getFullName());
            update.setPhone(user.getPhone());
            update.setAddress(user.getAddress());
            Role role = this.userService.getRoleName(user.getRole().getName());
            update.setRole(role);
            update.setAvatar(user.getAvatar());
        }
        this.userService.saveUser(update);
        return "redirect:/admin/user";
    }

    // Delete Data
    @RequestMapping("/admin/user/delete/{id}")
    public String getDeleteUser(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("deleteUser", new User());
        return "/admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String deleteAUser(Model model, @ModelAttribute("deleteUser") User user) {
        this.userService.deleteUserById(user.getId());
        return "redirect:/admin/user";
    }
}
