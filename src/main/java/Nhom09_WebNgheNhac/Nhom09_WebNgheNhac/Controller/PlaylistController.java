package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {
    @Autowired
    public PlayListService playListService;

    @GetMapping("")
    public String showPlaylistForm (Model model){
        model.addAttribute("playlist", playListService.getAllPlaylist());
        return "/list-playlist";
    }
}
