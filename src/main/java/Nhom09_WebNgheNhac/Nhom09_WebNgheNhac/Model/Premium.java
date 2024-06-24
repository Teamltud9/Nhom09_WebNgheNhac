package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "Premium")
public class Premium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceId;

    @NotEmpty(message = "Service Name không được để trống")
    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    @NotNull(message = "Price không được để trống")
    @Positive(message = "Price phải là số dương")
    private double price;

    @OneToMany(mappedBy = "service")
    private Set<Invoice> invoices;
}
