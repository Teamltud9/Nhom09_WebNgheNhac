package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "Playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playlistId;

    @NotEmpty(message = "Playlist Name không được để trống")
    @Column(nullable = false)
    private String playlistName;

    @Column(nullable = false)
    private int quantity;

    private String image;

    @Column(nullable = false)
    private boolean isDelete;

    @ManyToOne
    @JoinColumn(referencedColumnName = "categoryPlaylistId", name = "categoryPlaylistId")
    private CategoryPlaylist categoryPlaylist;

    @ManyToOne
    @JoinColumn(referencedColumnName = "userId" , name = "userId")
    private User user;


    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name = "Playlist_Song",
            joinColumns = @JoinColumn(name = "playlistId"),
            inverseJoinColumns = @JoinColumn(name = "songId"))
    private Set<Song> songPlaylist = new HashSet<>();
}
