package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Category;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Invoice;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Premium;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.InvoiceService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.PremiumService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @GetMapping("/add/{premiumId}/{userId}")
    public String ShowAddForm(@PathVariable("premiumId") int premiumId, @PathVariable("userId") Long userId, Model model)
    {
        Premium premium = premiumService.findById(premiumId);
        User user = userService.getUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        LocalDateTime now = LocalDateTime.now();
        Invoice invoice = new Invoice();
        invoice.setTotalAmount(premium.getPrice());
        invoice.setPremium(premium);
        invoice.setPaymentDate(now);
        invoice.setUser(user);
        model.addAttribute("formattedPaymentDate", now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        model.addAttribute("invoice", invoice);
        return "/invoice/add-invoice";
    }
    @PostMapping("/add/{premiumId}/{userId}")
    public String addInvoice(@PathVariable("premiumId") int premiumId, @PathVariable("userId") Long userId,
                             @Valid Invoice invoice, BindingResult result, Model model)
    {
        if (result.hasErrors()) {
            return "/invoice/add-invoice";
        }
        Premium premium = premiumService.findById(premiumId);
        User user = userService.getUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        invoice.setTotalAmount(premium.getPrice());
        invoice.setPremium(premium);
        invoice.setUser(user);
        invoiceService.add(invoice);
        return "redirect:/invoice";
    }
}
