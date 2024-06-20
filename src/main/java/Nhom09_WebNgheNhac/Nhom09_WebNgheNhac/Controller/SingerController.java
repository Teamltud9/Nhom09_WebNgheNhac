package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Category;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Singer;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.SingerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/singer")
public class SingerController {
    @Autowired
    private SingerService singerService;

    @GetMapping("")
    public String listSinger(Model model) {
        model.addAttribute("singers", singerService.getAllSinger());
        return "/singer/list-singers";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("singer", new Singer());
        return "/singer/add-singer";
    }

    @PostMapping("/add")
    public String addSinger(@Valid Singer singer, BindingResult result, MultipartFile imageFile) throws IOException {
        if (result.hasErrors()) {
            return "/singer/add-singer";
        }
        singer.setImage(singerService.saveImage(imageFile));
        singerService.addCategory(singer);
        return "redirect:/singer";
    }

    @GetMapping("/edit/{singerId}")
    public String showUpdateForm(@PathVariable("singerId") int singerId, Model model) {
        Singer singer = singerService.getSingerById(singerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid singer Id:" + singerId));
        model.addAttribute("singer", singer);
        return "/singer/update-singer";
    }
    @PostMapping("/edit/{singerId}")
    public String updateSinger(@PathVariable("singerId") int singerId, @Valid Singer
            singer, BindingResult result, Model model,MultipartFile imageFile) throws IOException {
        if (result.hasErrors()) {
            singer.setSingerId(Math.toIntExact(singerId));
            return "/singer/update-singer";
        }

        if(!imageFile.isEmpty())
            singer.setImage(singerService.saveImage(imageFile));
        else{
            Optional<Singer> singer1  = singerService.getSingerById(singerId);
            singer.setImage(singer1.get().getImage());
        }

        singerService.updateSinger(singer);
        model.addAttribute("singers", singerService.getAllSinger());
        return "redirect:/singer";
    }

    @GetMapping("/delete/{singerId}")
    public String deleteSinger(@PathVariable("singerId") int singerId, Model model) {
        Singer singer = singerService.getSingerById(singerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid singer Id:" + singerId));
        singerService.deleteSingerById(singerId);
        model.addAttribute("categories", singerService.getAllSinger());
        return "redirect:/singer";
    }
}
