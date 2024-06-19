package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import jakarta.persistence.*;
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

    @NotNull
    @Column(nullable = false)
    private String singerName;

    @NotNull
    @Column(nullable = false)
    private boolean gender;



    @OneToMany(mappedBy = "singer")
    private Set<Song> songs;
}
