package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Report;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {
    private final ReportRepository reportRepository;

    public List<Report> getAll()
    {
        return reportRepository.findAll();
    }

    public Report findById(int id)
    {
        return  reportRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Report does not exist."));
    }

    public void add(Report report)
    {
        reportRepository.save(report);
    }

    public void deleteById(int id)
    {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Report does not exist."));
        report.setDeleted(true);
        reportRepository.save(report);
    }
}
