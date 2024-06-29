package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Song")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int songId;

    @Column(nullable = false)
    @NotEmpty(message = "Song Name không được để trống")
    private String songName;

    @Column(nullable = false)
    @Past(message = "Release Date phải là ngày trong quá khứ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message="Release Date không được để trống")
    private LocalDate releaseDate;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String image;


    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private Long createByUser ;

    @Column(nullable = false)
    private boolean isDelete;

    @Column(nullable = false)
    private boolean isPremium;

    @ManyToOne
    @JoinColumn(referencedColumnName = "categoryId", name = "categoryId")
    private Category category;

    @OneToMany(mappedBy = "song")
    private Set<Report> reports;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "Singer_Song",
            joinColumns = @JoinColumn(name = "songId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<User> users = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "songPlaylist", fetch = FetchType.LAZY)
    private Set<Playlist> playlists = new HashSet<>();
}
