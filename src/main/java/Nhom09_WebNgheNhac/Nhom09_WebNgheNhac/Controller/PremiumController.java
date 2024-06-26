package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Category;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Premium;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.PremiumService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/premium")
public class PremiumController {

    @Autowired
    private PremiumService premiumService;

    @GetMapping("")
    public String listPremium(Model model)
    {
         model.addAttribute("premiums",premiumService.getAll());
         return "/premium/list-premiums";
    }

    @GetMapping("/add")
    public String showAddForm(Model model)
    {
        model.addAttribute("premium", new Premium());
        return "/premium/add-premium";
    }
    @PostMapping("/add")
    public String addPremium(@Valid Premium premium, BindingResult result) throws IOException {
        if(result.hasErrors())
        {
            return "/premium/add-premium";
        }
        premium.setDeleted(false);
        premiumService.add(premium);
        return "redirect:/premium";
    }
    @GetMapping("/edit/{premiumId}")
    public String showUpdateForm(@PathVariable("premiumId") int premiumId, Model model)
    {
        Premium premium = premiumService.findById(premiumId);
        model.addAttribute("premium", premium);
        return "/premium/update-premium";
    }

    @PostMapping("/edit/{premiumId}")
    public String updatePremium(@PathVariable("premiumId") int premiumId, @Valid Premium
            premium, BindingResult result, Model model) throws IOException
    {
        if(result.hasErrors())
        {
            premium.setPremiumId(premiumId);
            return "/premium/update-premium";
        }
        premiumService.update(premium);
        model.addAttribute("premiums", premiumService.getAll());
        return "redirect:/premium";
    }

    @GetMapping("/delete/{premiumId}")
    public String deletedPremium(@PathVariable("premiumId") int premiumId, Model model)
    {
        premiumService.deleteById(premiumId);
        model.addAttribute("premiums", premiumService.getAll());
        return "redirect:/premium";
    }

}
