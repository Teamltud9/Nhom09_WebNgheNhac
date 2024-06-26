package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Category;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.CategoryPlaylist;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.CategoryPlaylistService;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/categoryplaylist")
public class CategoryPlaylistController {
    @Autowired
    private CategoryPlaylistService categoryPlaylistService;

    @GetMapping("")
    public String listCategoryPlaylist(Model model) {
        model.addAttribute("categoryplaylist", categoryPlaylistService.getAllCategoryPlaylist());
        return "categoryplaylist/list-categoryplaylist";
    }
    @PostConstruct
    private void addDefaultCategoryPlaylists() {
        List<String> defaultNames = List.of("Favorite-List", "Playlist", "Album");
        for (String name : defaultNames) {
            if (!categoryPlaylistService.existsByName(name)) {
                CategoryPlaylist categoryPlaylist = new CategoryPlaylist();
                categoryPlaylist.setCategoryPlaylistName(name);
                categoryPlaylistService.addCategoryPlaylist(categoryPlaylist);
            }
        }
    }

    @GetMapping("/add")
    public String showAddPage(Model model){
        model.addAttribute("categoryplaylist", new CategoryPlaylist());
        return "categoryplaylist/add-categoryplaylist";
    }

    @PostMapping("/add")
    public String addCategoryPlaylist(@Valid @ModelAttribute("categoryplaylist") CategoryPlaylist categoryPlaylist, BindingResult result, Model model) throws InvalidDataException, UnsupportedTagException, IOException {
        if (result.hasErrors()) {
            return "categoryplaylist/add-categoryplaylist";
        }
        categoryPlaylistService.addCategoryPlaylist(categoryPlaylist);
        return "redirect:/categoryplaylist";
    }

    @GetMapping("/edit/{categoryplaylistId}")
    public String showUpdateForm(@PathVariable("categoryplaylistId") int categoryplaylistId, Model model ){
        CategoryPlaylist ex_categoryplaylist = categoryPlaylistService.getCategoryPlaylistById(categoryplaylistId).orElseThrow(() -> new IllegalArgumentException("Invalid Category-Playlist Id:" + categoryplaylistId));
        model.addAttribute("categoryplaylist",ex_categoryplaylist);
        return "categoryplaylist/update-categoryplaylist";
    }

    @PostMapping("/edit/{categoryplaylistId}")
    public String updateCategoryPlaylist(@PathVariable("categoryplaylistId") int categoryplaylistId,
                                         @Valid CategoryPlaylist categoryPlaylist,
                                         BindingResult result,
                                         Model model) throws IOException {
        if (result.hasErrors()) {
            return "categoryplaylist/update-categoryplaylist";
        }
        CategoryPlaylist existingCategoryPlaylist = categoryPlaylistService.getCategoryPlaylistById(categoryplaylistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Category-Playlist Id:" + categoryplaylistId));

        existingCategoryPlaylist.setCategoryPlaylistName(categoryPlaylist.getCategoryPlaylistName());
        categoryPlaylistService.updateCategory(existingCategoryPlaylist);
        return "redirect:/categoryplaylist";
    }

    @GetMapping("/delete/{categoryplaylistId}")
    public String deleteCategory(@PathVariable("categoryplaylistId") int categoryplaylistId, Model model) {
        CategoryPlaylist categoryPlaylist = categoryPlaylistService.getCategoryPlaylistById(categoryplaylistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category-playlist Id:" + categoryplaylistId));
        categoryPlaylistService.deleteCategoryPlaylistById(categoryplaylistId);
        model.addAttribute("categories", categoryPlaylistService.getAllCategoryPlaylist());
        return "redirect:/categoryplaylist";
    }
}
