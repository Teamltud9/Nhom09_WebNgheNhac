package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "Report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportId;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long createByUser ;

    @ManyToOne
    @JoinColumn(referencedColumnName = "songId", name = "songId")
    private Song song;
}

