package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository;


import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Integer> {
    List<Song> findBySongNameContainingIgnoreCase(String name);
    List<Song> findByCategory_CategoryId(int categoryId);
}
