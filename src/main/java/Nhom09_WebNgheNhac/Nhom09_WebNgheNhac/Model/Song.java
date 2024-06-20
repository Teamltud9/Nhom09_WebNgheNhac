package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity
@Table(name = "Song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int songId;

    @Column(nullable = false)
    @NotEmpty(message = "kkhfihf")
    private String songName;

    @Column(nullable = false)
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate releaseDate;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String image;


    @Column(nullable = false)
    private String filePath;

    @ManyToOne
    @JoinColumn(referencedColumnName = "categoryId")
    private Category category;


    @ManyToOne
    @JoinColumn(referencedColumnName = "singerId")
    private Singer singer;
}
