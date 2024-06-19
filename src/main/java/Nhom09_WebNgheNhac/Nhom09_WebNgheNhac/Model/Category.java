package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Column(nullable = false)
    @NotNull
    private String categoryName;

    @Column(nullable = false)
    private String image;

    @OneToMany(mappedBy = "category")
    private Set<Song> songs;
}
