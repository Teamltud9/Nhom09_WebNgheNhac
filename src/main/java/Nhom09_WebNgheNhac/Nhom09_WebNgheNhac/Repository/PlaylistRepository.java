package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Playlist;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist,Integer> {
    List<Playlist> findByUser(User user);
    List<Playlist> findByUserAndIsDeleteFalse(User user);
    Optional<Playlist> findByPlaylistIdAndUser(int playlistId, User user);

}
