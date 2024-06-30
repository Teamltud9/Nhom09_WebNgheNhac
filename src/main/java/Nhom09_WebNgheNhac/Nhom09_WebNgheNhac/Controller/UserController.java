package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;


import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Song;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.RoleRepository;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Role;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.SongService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.UserService;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Role;
@Controller // Đánh dấu lớp này là một Controller trong Spring MVC.
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private SongService songService;

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

    @GetMapping("/edit/{userId}")
    public String showUpdateForm(@PathVariable("userId") long userId, Model model) {
        User user = userService.getUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/edit/{userId}")
    public String editUser(@PathVariable("userId") Long userId, @Valid User user, BindingResult result, MultipartFile imageFile, HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidDataException, UnsupportedTagException {

        if (result.hasErrors()) {
            int errorCount = result.getErrorCount();
            if (result.getFieldError("country") != null) {
                result.rejectValue("country", "ignore");
            }
            if(errorCount>1){
                user.setUserId(Long.valueOf(Math.toIntExact(userId)));
                return "/users/edit";
            }


        }

        Optional<User> user1 = userService.getUserId(userId);
        if(!imageFile.isEmpty())
            user.setImage(userService.saveImage(imageFile));
        else{
            user.setImage(user1.get().getImage());
        }




        userService.editUser(user);

        return "redirect:/";

    }


/*
*
*
*
*
* */



}