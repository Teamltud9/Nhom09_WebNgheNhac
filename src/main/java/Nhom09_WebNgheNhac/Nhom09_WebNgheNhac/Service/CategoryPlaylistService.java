package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.CategoryPlaylist;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.CategoryPlaylistRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryPlaylistService {
    private final CategoryPlaylistRepository categoryPlaylistRepository;
    public List<CategoryPlaylist> getAllCategoryPlaylist(){return  categoryPlaylistRepository.findAll();}
    public void addCategoryPlaylist(CategoryPlaylist categoryPlaylist) {categoryPlaylistRepository.save(categoryPlaylist);}
    public Optional<CategoryPlaylist> getCategoryPlaylistById(int id) {
        return categoryPlaylistRepository.findById(id);
    }
    public boolean existsByName(String name) {return categoryPlaylistRepository.existsByCategoryPlaylistName(name);}

    public void updateCategory(@NotNull CategoryPlaylist categoryPlaylist) {
        CategoryPlaylist  ex_categoryPlaylist = categoryPlaylistRepository.findById((int)categoryPlaylist.getCategoryPlaylistId()).orElseThrow(() -> new IllegalStateException("Category-Playlist with ID " +
                categoryPlaylist.getCategoryPlaylistId() + " does not exist."));
        ex_categoryPlaylist.setCategoryPlaylistName(categoryPlaylist.getCategoryPlaylistName());
        categoryPlaylistRepository.save(ex_categoryPlaylist);
    }
    public void deleteCategoryPlaylistById(int id) {
        if (!categoryPlaylistRepository.existsById(id)) {
            throw new IllegalStateException("Category-Playlist with ID " + id + " does not exist.");
        }
        categoryPlaylistRepository.deleteById(id);
    }
}

