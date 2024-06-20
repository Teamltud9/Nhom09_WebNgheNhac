package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Validator.Unique;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "Singer")
public class Singer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int singerId;

    @NotEmpty
    @Column(nullable = false)
    private String singerName;

    @NotNull
    @Column(nullable = false)
    private boolean gender;

    @Column(nullable = false)
    private String image;

    @OneToMany(mappedBy = "singer")
    private Set<Song> songs;
}
