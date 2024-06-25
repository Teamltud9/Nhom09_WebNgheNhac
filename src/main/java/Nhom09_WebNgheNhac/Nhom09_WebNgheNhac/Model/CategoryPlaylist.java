package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "CategoryPlaylist")
public class CategoryPlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryPlaylistId;

    @Column(nullable = false)
    @NotEmpty(message = "Category Playlist Name không được để trống")
    private String categoryPlaylistName;


    @OneToMany(mappedBy = "categoryPlaylist")
    private Set<Playlist> playlists;
}
