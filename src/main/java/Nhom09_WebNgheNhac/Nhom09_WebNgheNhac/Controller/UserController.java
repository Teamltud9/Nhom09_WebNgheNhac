package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;


import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


public class UserController {

    @Autowired
    private UserService userService;

    public static int UserId;
    @GetMapping
    public String listUser(Model model) {
        model.addAttribute("users", userService.getAllUser());
        return "/home/list-user";
    }


    @GetMapping("/user/add")
    public String showAddForm(Model model) {
        UserId=-1;
        model.addAttribute("user", new User());
        return "/home/add-user";
    }

    @PostMapping("/user/add")
    public String addProduct(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "/home/add-user";
        }

        userService.addUser(user);
        return "redirect:/";
    }

    @GetMapping("/user/update/{userId}")
    public String showEditForm(@PathVariable int userId,  Model model) {

        try {
            UserId=userId;
            User user = userService.getUserId(userId).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + userId));
            model.addAttribute("user", user);
            return "/home/update-user";
        } catch (IllegalArgumentException e) {
            return "/home/error";
        }
    }

    @PostMapping("/user/update/{userId}")
    public String updateProduct( @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "/home/update-user";
        }
        userService.updateUser(user);
        return "redirect:/";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        userService.deleteUserById(id);
        return "redirect:/";
    }
}