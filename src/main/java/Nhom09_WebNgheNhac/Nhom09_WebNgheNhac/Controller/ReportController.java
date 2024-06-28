package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Controller;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Invoice;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Report;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Song;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.ReportService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.SongService;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private SongService songService;
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String listReport(Model model)
    {
        model.addAttribute("reports", reportService.getAll()
                                        .stream().filter(r -> !r.isDeleted()).toList());
        return "/report/list-reports";
    }

    @GetMapping("/add/{songId}/{userId}")
    public String showAddForm(@PathVariable("songId") int songId,@PathVariable("userId") Long userId, Model model)
    {
        Song song = songService.getSongId(songId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid song"));
        User user = userService.getUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User"));
        Report report = new Report();
        report.setCreationDate(LocalDateTime.now());
        report.setCreateByUser(userId);
        report.setSong(song);
        model.addAttribute("report", report);
        return "/report/add-report";
    }

    @PostMapping("/add/{songId}/{userId}")
    public String addReport(@PathVariable("songId") int songId, @PathVariable("userId") Long userId,
                             @Valid Report report, BindingResult result, Model model)
    {
        if(result.hasErrors())
        {
            return "/report/add-report";
        }
        reportService.add(report);
        return "redirect:/";
    }

    @GetMapping("/delete/{reportId}/{userId}")
    public String deletedReport(@PathVariable("reportId") int reportId,  @PathVariable("userId") Long userId, Model model)
    {
        User reportedUser = userService.getUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User"));
        Report report = reportService.findById(reportId);
        reportedUser.setCountReport(reportedUser.getCountReport() + 1);
        userService.editUser(reportedUser);
        songService.deleteSongById(report.getSong().getSongId());
        reportService.deleteById(reportId);
        model.addAttribute("reports", reportService.getAll()
                                .stream().filter(r -> !r.isDeleted()).toList());
        return "redirect:/report";
    }

    @GetMapping("/denied/{reportId}")
    public String deniedReport(@PathVariable("reportId") int reportId, Model model)
    {

        Report report = reportService.findById(reportId);
        reportService.deleteById(reportId);
        model.addAttribute("reports", reportService.getAll()
                .stream().filter(r -> !r.isDeleted()).toList());
        return "redirect:/report";
    }

}
