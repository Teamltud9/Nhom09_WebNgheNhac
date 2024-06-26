package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Invoice;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    public List<Invoice> getAll()
    {
        return invoiceRepository.findAll();
    }
    public Invoice getById(int id)
    {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Invoice with does not exist."));
    }

    public void add(Invoice invoice)
    {
        invoiceRepository.save(invoice);
    }
}
