package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Category;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Song;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.CategoryService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.SongService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.UserService;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import io.micrometer.common.lang.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @PostMapping("/song/add")
    public String addProduct(@Valid Song song, BindingResult result,MultipartFile imageFile,MultipartFile fileMp3,Model model) throws InvalidDataException, UnsupportedTagException, IOException {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAlCatologies());
            return "/song/add-song";
        }
        song.setTime(songService.timeMusic(fileMp3));
        song.setImage(songService.saveImage(imageFile));
        song.setFilePath(songService.saveMusic(fileMp3));
        songService.addSong(song);
        return "redirect:/";
    }


    @GetMapping("/song/edit/{songId}")
    public String showUpdateForm(@PathVariable("songId") int songId, Model model) {
        Song song = songService.getSongId(songId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid song Id:" + songId));
        model.addAttribute("categories", categoryService.getAlCatologies());
        model.addAttribute("song", song);
        return "/song/update-song";
    }

    @PostMapping("/song/edit/{songId}")
    public String updateProduct(@PathVariable("songId") int songId, @Valid Song song, BindingResult result,MultipartFile imageFile,MultipartFile fileMp3) throws IOException, InvalidDataException, UnsupportedTagException {
        if (result.hasErrors()) {
            song.setSongId(Math.toIntExact(songId));
            return "/products/update-product";
        }

        Optional<Song> song1 = songService.getSongId(songId);
        if(!imageFile.isEmpty())
            song.setImage(songService.saveImage(imageFile));
        else{
            song.setImage(song1.get().getImage());
        }

        if(!fileMp3.isEmpty()){
            song.setTime(songService.timeMusic(fileMp3));
            song.setFilePath(songService.saveMusic(fileMp3));
        }
        else{
            song.setTime(song1.get().getTime());
            song.setFilePath(song1.get().getFilePath());
        }


        songService.updateSong(song);
        return "redirect:/";
    }

    @GetMapping("/song/delete/{songId}")
    public String deleteCategory(@PathVariable("songId") int songId, Model model) {
        Song song = songService.getSongId(songId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid song Id:" + songId));
        songService.deleteSongById(songId);
        return "redirect:/";
    }


    @GetMapping("/search")
    public String Search(@NonNull Model model, String query) {

        List<Song> songs = songService.getAllSong();
        model.addAttribute("songs", songs.stream()
                .filter(title -> title.getSongName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList()));
        return "/song/list-song";
    }



}
