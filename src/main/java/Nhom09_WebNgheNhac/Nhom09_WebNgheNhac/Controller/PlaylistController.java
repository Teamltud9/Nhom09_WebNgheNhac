package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;


import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Playlist;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Song;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.CategoryPlaylistRepository;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Role;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.PlaylistService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryPlaylistRepository categoryPlaylistRepository;

    @GetMapping("")
    public String showUserPlaylists(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            List<Playlist> userPlaylists = playlistService.getPlaylistsByUser(user);
            model.addAttribute("playlists", userPlaylists.stream().filter(p->p.getCategoryPlaylist().getCategoryPlaylistId()==2));
            return "playlist/list-playlist";
        }
        return "redirect:/login";
    }

/*
*
*
*
* */



    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("playlist", new Playlist());
        return "playlist/add-playlist";
    }

    @PostMapping("/add")
    public String addPlaylist(@Valid Playlist playlist, BindingResult result, MultipartFile imageFile) throws IOException {
        if (result.hasErrors()) {
            return "/playlist/add-playlist";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        playlist.setUser(userLogin);
        playlist.setImage(playlistService.saveImage(imageFile));
        playlist.setDelete(false);
        playlist.setQuantity(0);
        if(userLogin.getRoles().stream().anyMatch(p -> p.getRoleId().equals(Role.SINGER.value)))
        {
            playlist.setCategoryPlaylist(categoryPlaylistRepository.findById(3).get());
        }
        else
        {
            playlist.setCategoryPlaylist(categoryPlaylistRepository.findById(2).get());
        }
        playlistService.add(playlist);

        if(userLogin.getRoles().stream().anyMatch(p -> p.getRoleId().equals(Role.SINGER.value)))
            return "redirect:/playlist/album";
        return "redirect:/playlist";
    }


    @GetMapping("/edit/{playlistId}")
    public String showUpdateForm(@PathVariable("playlistId") int playlistId, Model model, Authentication authentication) {
        Playlist playlist = playlistService.getPlaylistById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + playlistId));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = (User) userService.loadUserByUsername(userDetails.getUsername());
        if (!playlist.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("You don't have permission to edit this playlist");
        }
        model.addAttribute("playlist", playlist);
        return "/playlist/update-playlist";
    }

    @PostMapping("/edit/{playlistId}")
    public String updatePlaylist(@PathVariable("playlistId") int playlistId, @Valid Playlist
            playlist, BindingResult result, Model model, MultipartFile imageFile) throws IOException {
        if (result.hasErrors()) {
            playlist.setPlaylistId(Math.toIntExact(playlistId));
            return "/playlist/update-playlist";
        }

        if (!imageFile.isEmpty())
            playlist.setImage(playlistService.saveImage(imageFile));
        else {
            Optional<Playlist> playlist1 = playlistService.getPlaylistById(playlistId);
            playlist.setImage(playlist1.get().getImage());
        }

        playlistService.updatePlaylist(playlist);
        return "redirect:/playlist";
    }

    @GetMapping("/delete/{playlistId}")
    public String deletePlaylist(@PathVariable("playlistId") int playlistId, Authentication authentication) {
        Playlist playlist = playlistService.getPlaylistById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + playlistId));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = (User) userService.loadUserByUsername(userDetails.getUsername());
        if (!playlist.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("You don't have permission to delete this playlist");
        }
        playlistService.deletedById(playlistId);
        return "redirect:/playlist";
    }

    @PostMapping("/add-song")
    public String addSongToPlaylist(@RequestParam int playlistId,
                                    @RequestParam int songId,
                                    RedirectAttributes redirectAttributes,
                                    Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = (User) userService.loadUserByUsername(userDetails.getUsername());

            playlistService.addSongToPlaylist(playlistId, songId, currentUser);
            redirectAttributes.addFlashAttribute("message", "Song added to playlist successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/playlist";
    }

    @GetMapping("/detail/{playlistId}")
    public String getPlaylistDetails(@PathVariable("playlistId") int playlistId, Model model) {
        Optional<Playlist> playlistOptional = playlistService.getPlaylistById(playlistId);
        if (playlistOptional.isPresent()) {
            Playlist playlist = playlistOptional.get();

            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // Kiểm tra quyền truy cập
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                User currentUser = (User) userService.loadUserByUsername(userDetails.getUsername());
                if (playlistOptional.get().getCategoryPlaylist().getCategoryPlaylistId() != 3)
                {
                    if (!playlist.getUser().getUserId().equals(currentUser.getUserId()))
                    {
                        throw new AccessDeniedException("You don't have permission to view this playlist");
                    }
                }
            }
            else{
                if (playlistOptional.get().getCategoryPlaylist().getCategoryPlaylistId() != 3)
                {

                       return "/error";

                }
            }

            Set<Song> songs = playlist.getSongPlaylist().stream().filter(p->!p.isDelete()).collect(Collectors.toSet());
            model.addAttribute("playlist", playlist);
            model.addAttribute("songs", songs);
            return "playlist/detail-playlist";
        } else {
            throw new IllegalArgumentException("Invalid playlist Id:" + playlistId);
        }
    }

    @PostMapping("/remove-song/{playlistId}/{songId}")
    public String removeSongFromPlaylist(@RequestParam int playlistId,
                                         @RequestParam int songId,
                                         RedirectAttributes redirectAttributes,
                                         Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = (User) userService.loadUserByUsername(userDetails.getUsername());

            playlistService.removeSongFromPlaylist(playlistId, songId, currentUser);
            redirectAttributes.addFlashAttribute("message", "Song removed from playlist successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/playlist/detail/" + playlistId;
    }

    @GetMapping("/likeplaylist")
    public String playlistLike(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();

        Playlist playlist = playlistService.likePlaylist(userLogin.getUserId(),1);

        return "redirect:/playlist/detail/"+playlist.getPlaylistId();
    }
    @GetMapping("/album/{userId}")
    public String showAlbums(@PathVariable("userId") int userId,Model model)
    {
        model.addAttribute("albums", playlistService.getAlbum()
                .stream().filter(p -> !p.isDelete())
                .filter(p->p.getUser().getUserId()==userId&& p.getCategoryPlaylist().getCategoryPlaylistId()==3)
                .toList());
        model.addAttribute("user",userId);
        return "playlist/list-album";
    }
}

