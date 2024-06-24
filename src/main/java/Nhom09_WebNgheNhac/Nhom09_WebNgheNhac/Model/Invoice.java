package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "Invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int invoiceId;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private double totalAmount;

    @ManyToOne
    @JoinColumn(referencedColumnName = "serviceId", name = "serviceId")
    private Service service;

    @ManyToOne
    @JoinColumn(referencedColumnName = "userId" , name = "userId")
    private User user;
}
