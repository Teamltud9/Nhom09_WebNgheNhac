package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Playlist;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Song;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Role;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.CategoryService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.PlaylistService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.SongService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.UserService;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import io.micrometer.common.lang.NonNull;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class SongController {
    @Autowired
    private SongService songService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private PlaylistService playlistService;


    @GetMapping
    public String listSong(Model model , String sapxep) {
        if(sapxep!=null){
            if(sapxep.equals("tangdan")){
                model.addAttribute("songs", songService.getAllSong().stream().filter(s -> !s.isDelete()).sorted(Comparator.comparing(Song::getLikeCount)).toList());
            }
            else
                model.addAttribute("songs", songService.getAllSong().stream().filter(s -> !s.isDelete()).sorted(Comparator.comparing(Song::getLikeCount).reversed()).toList());
        }
        else
            model.addAttribute("songs", songService.getAllSong().stream().filter(s -> !s.isDelete()).toList());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Integer> songIds = new ArrayList<>();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User user = (User) authentication.getPrincipal();
            Optional<User> user1 = userService.getUserId(user.getUserId());
            UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user1.get(), authentication.getCredentials(), user1.get().getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);


            Playlist playlist = playlistService.likePlaylist(user.getUserId(),1);

             songIds = playlist.getSongPlaylist()
                    .stream()
                    .map(Song::getSongId)
                    .toList();

            List<Playlist> playlists = playlistService.getPlaylistsByUser(user).stream().filter(p->p.getCategoryPlaylist().getCategoryPlaylistId()!=1).toList();
            model.addAttribute("playlists", playlists);
        }


        model.addAttribute("songIds", songIds);
        return "/song/list-song";
    }

    @GetMapping("/song/add")
    public String showAddForm(Model model) {
        model.addAttribute("song", new Song());
        model.addAttribute("categories", categoryService.getAlCatologies()
                                                        .stream().filter(c -> !c.isDelete()).toList());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<User> singers = userService.getAllUser().stream()
                .filter(p -> !p.getUserId().equals(user.getUserId()))
                .filter(p -> p.getRoles().stream().anyMatch(role -> role.getRoleName().equals("SINGER")))
                .collect(Collectors.toList());
        model.addAttribute("singers", singers);

        return "/song/add-song";
    }

    @PostMapping("/song/add")
    public String addProduct(@Valid Song song, BindingResult result,MultipartFile imageFile,MultipartFile fileMp3, String selectedNgheSiList,Model model) throws InvalidDataException, UnsupportedTagException, IOException {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAlCatologies()
                                                .stream().filter(c -> !c.isDelete()).toList());
            return "/song/add-song";
        }
        song.setTime(songService.timeMusic(fileMp3));
        song.setImage(songService.saveImage(imageFile));
        song.setFilePath(songService.saveMusic(fileMp3));
        songService.addSong(song,selectedNgheSiList);
        return "redirect:/";
    }

    @GetMapping("/song/detail/{songId}")
    public String detailSong(@PathVariable("songId") int songId, Model model) {
        Song song = songService.getSongId(songId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid song Id:" + songId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean check;

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User user = (User) authentication.getPrincipal();
            if(!user.getUserId().equals(song.getCreateByUser()) && !user.getRoles().stream()
                    .anyMatch(p -> p.getRoleId().equals(Role.ADMIN.value)) ){
                if(song.isPremium()){
                    if(user.isPremium())
                        check= true;
                    else
                        check = false;
                }
                else
                    check = true;
            }
            else
                check = true;
        }
        else{
            if(song.isPremium()){
                check = false;
            }
            else
                check = true;
        }

        model.addAttribute("check", check);
        model.addAttribute("song", song);
        return "/song/detail-song";
    }

    @GetMapping("/song/edit/{songId}")
    public String showUpdateForm(@PathVariable("songId") int songId, Model model) {
        Song song = songService.getSongId(songId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid song Id:" + songId));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();
        if (!song.getCreateByUser().equals(userLogin.getUserId())) {
            return "redirect:/error";
        }
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
    public String deleteSong(@PathVariable("songId") int songId, Model model) {
        Song song = songService.getSongId(songId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid song Id:" + songId));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();
        if (!song.getCreateByUser().equals(userLogin.getUserId())) {
            return "redirect:/error";
        }
        songService.deleteSongById(songId);
        return "redirect:/";
    }


    @GetMapping("/search")
    public String Search(@NonNull Model model, String query , String sapxep) {
        if(sapxep!=null){
            if(sapxep.equals("tangdan")){
                model.addAttribute("songs", songService.searchSong(query).stream().sorted(Comparator.comparing(Song::getLikeCount)));
            }
            else
                model.addAttribute("songs", songService.searchSong(query).stream().sorted(Comparator.comparing(Song::getLikeCount).reversed()));
        }
        else
            model.addAttribute("songs", songService.searchSong(query));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Integer> songIds = new ArrayList<>();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User user = (User) authentication.getPrincipal();

            Playlist playlist = playlistService.likePlaylist(user.getUserId(),1);

            songIds = playlist.getSongPlaylist()
                    .stream()
                    .map(Song::getSongId)
                    .toList();
        }

        model.addAttribute("songIds", songIds);
        return "/song/list-song";
    }

    @GetMapping("/SearchSuggestions")
    @ResponseBody
    public List<String> searchSuggestions(String query) {
        return songService.SearchSuggestions(query);
    }


    @GetMapping("/song/manage")
    public String listQuanLi(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("songs", songService.getAllSong().stream().filter(p -> p.getCreateByUser().equals(user.getUserId())));
        return "/song/manage-song";
    }

    @GetMapping("/song/like/{songId}")
    public String likeSong(@PathVariable("songId") int songId, Model model) {
        Song song = songService.getSongId(songId).stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid song Id:" + songId));


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Playlist playlist = playlistService.likePlaylist(user.getUserId(),1);
        Set<Song> listSong = playlist.getSongPlaylist();
        if(listSong.stream().anyMatch(p->p.getSongId() == song.getSongId())){
            listSong.removeIf(p ->p.getSongId() == song.getSongId());
            song.setLikeCount(song.getLikeCount()-1);
        }
        else{
            listSong.add(song);
            song.setLikeCount(song.getLikeCount()+1);
        }

        songService.updateSong(song);
        playlist.setSongPlaylist(listSong);
        playlistService.update(playlist);
        return "redirect:/";
    }

    @GetMapping("/error")
    public String error(){
        return "/error";
    }

}
