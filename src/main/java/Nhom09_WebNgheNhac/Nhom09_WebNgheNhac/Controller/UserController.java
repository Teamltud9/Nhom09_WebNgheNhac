package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;


import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller // Đánh dấu lớp này là một Controller trong Spring MVC.
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/login")
    public String login() {
        return "users/login";
    }



    @GetMapping("/register")
    public String register(@NotNull Model model) {

        model.addAttribute("user", new User()); // Thêm một đối tượng User mới vào
        //model
        return "users/register";
    }
    @PostMapping("/register")
    public String register(@Valid User user,
                           @NotNull BindingResult bindingResult, Model model, MultipartFile imageFile) throws IOException {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "users/register"; // Trả về lại view "register" nếu có lỗi
        }
        userService.save(user,imageFile); // Lưu người dùng vào cơ sở dữ liệu
/*        userService.setDefaultRole(user.getUsername()); // Gán vai trò mặc định cho người dùng*/
        return "redirect:/user/login"; // Chuyển hướng người dùng tới trang "login"
    }
}