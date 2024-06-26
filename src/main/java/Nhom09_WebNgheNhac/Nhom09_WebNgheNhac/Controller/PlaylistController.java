package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;


import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Playlist;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.PlaylistService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String showUserPlaylists(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Optional<User> userOptional = userService.findByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                List<Playlist> userPlaylists = playlistService.getPlaylistsByUser(user);
                model.addAttribute("playlist", userPlaylists);
                return "playlist/list-playlist";
            }
        }
        return "redirect:/login";
    }
}
