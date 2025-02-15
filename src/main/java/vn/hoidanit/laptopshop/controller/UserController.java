package vn.hoidanit.laptopshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
    //    List<User> users = this.userService.getAllUsersByEmail("cuquangtuoi11@gmail.com");
    //    System.out.println("Users: " + users);
        model.addAttribute("tuoi", "test");
        return "hello";
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
    @RequestMapping("/admin/user/create")
    public String getCreateUser(Model model) {
        model.addAttribute("createUser", new User());
        return "/admin/user/create";
    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String createCreateUser(Model model,  @ModelAttribute("createUser") User tuoi) {
        this.userService.saveUser(tuoi);
        return "redirect:/admin/user";
    }

    // View Data
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
    public String updateUpdateUser(Model model, @ModelAttribute("updateUser") User user) {
        User update = this.userService.getUserById(user.getId());
        if (update != null) {
            update.setFullName(user.getFullName()); 
            update.setPhone(user.getPhone());
            update.setAddress(user.getAddress());
        }
        this.userService.saveUser(user);
        return "redirect:/admin/user";
    }
}
