package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

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
    @NotNull
    private String songName;

    @Column(nullable = false)
    @Past
    private Date releaseDate;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String image;


    @Column(nullable = false)
    @NotNull
    private String filePath;

    @ManyToOne
    @JoinColumn(referencedColumnName = "categoryId")
    private Category category;
}
