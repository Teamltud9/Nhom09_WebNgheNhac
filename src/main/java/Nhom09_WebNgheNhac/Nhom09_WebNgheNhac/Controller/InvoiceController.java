package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Premium;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.InvoiceService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.PremiumService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private PremiumService premiumService;
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String listInvoice(Model model)
    {
        model.addAttribute("invoices", invoiceService.getAll());
        return "/invoice/list-invoices";
    }

/*    @GetMapping("/invoice/add/{premiumId}/{userId}")
    public String ShowAddForm(@PathVariable int premiumId, @PathVariable Long userId, Model model)
    {
        Premium premium = premiumService.findById(premiumId);
        User user = userService.f

        return "";
    }*/
}
