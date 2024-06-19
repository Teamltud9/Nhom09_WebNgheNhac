package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Song;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.CategoryService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.SongService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SongController {
    @Autowired
    private SongService songService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listSong(Model model) {
        model.addAttribute("songs", songService.getAllSong());
        return "/song/list-song";
    }

    @GetMapping("/song/add")
    public String showAddForm(Model model) {
        model.addAttribute("song", new Song());
        model.addAttribute("categories", categoryService.getAlCatologies());
        return "/song/add-song";
    }
}
