package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Category;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Song;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.CategoryService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.SongService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("")
    public String listCategory(Model model) {
        model.addAttribute("categories", categoryService.getAlCatologies());
        return "/category/list-category";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "/category/add-category";
    }

    @PostMapping("/add")
    public String addCategory(@Valid Category category, BindingResult result, MultipartFile imageFile) throws IOException {
        if (result.hasErrors()) {
            return "/category/add-category";
        }
        category.setImage(categoryService.saveImage(imageFile));
        category.setDelete(false);
        categoryService.addCategory(category);
        return "redirect:/category";
    }

    @GetMapping("/edit/{categoryId}")
    public String showUpdateForm(@PathVariable("categoryId") int categoryId, Model model) {
        Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
        model.addAttribute("category", category);
        return "/category/update-category";
    }
    @PostMapping("/edit/{categoryId}")
    public String updateCategory(@PathVariable("categoryId") int categoryId, @Valid Category
            category, BindingResult result, Model model,MultipartFile imageFile) throws IOException {
        if (result.hasErrors()) {
            category.setCategoryId(Math.toIntExact(categoryId));
            return "/category/update-category";
        }


        if(!imageFile.isEmpty())
            category.setImage(categoryService.saveImage(imageFile));
        else{
            Optional<Category> category1 = categoryService.getCategoryById(categoryId);
            category.setImage(category1.get().getImage());
        }

        categoryService.updateCategory(category);
        model.addAttribute("categories", categoryService.getAlCatologies());
        return "redirect:/category";
    }

    @GetMapping("/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") int categoryId, Model model) {
        Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
        categoryService.deleteCategoryById(categoryId);
        model.addAttribute("categories", categoryService.getAlCatologies());
        return "redirect:/category";
    }
    @GetMapping("/detail/{categoryId}")
    public String viewCategoryDetail(@PathVariable("categoryId") int categoryId, Model model) {
        Optional<Category> category = categoryService.getCategoryById(categoryId);
        Set<Song> songs = categoryService.getCategoryDetailById(categoryId);
        model.addAttribute("category", category.get());
        model.addAttribute("songs", songs);
        return "/category/detail-category";
    }
}
