package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Playlist;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Premium;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.PlaylistRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }
    public List<Playlist> getPlaylistsByUser(User user) {
        return playlistRepository.findByUser(user);
    }
    public void add(Playlist playlist){
        playlistRepository.save(playlist);
    }
    public void update(@NotNull Playlist playlist)
    {
        Playlist existingPlaylist = playlistRepository.findById((int)playlist.getPlaylistId())
                .orElseThrow(() -> new IllegalStateException("Premium" +
                        playlist.getPlaylistName() + " does not exist."));
        existingPlaylist.setPlaylistName(playlist.getPlaylistName());
        existingPlaylist.setCategoryPlaylist(playlist.getCategoryPlaylist());
        existingPlaylist.setUser(playlist.getUser());
        existingPlaylist.setDelete(playlist.isDelete());
        existingPlaylist.setSongPlaylist(playlist.getSongPlaylist());
        playlistRepository.save(existingPlaylist);
    }


    public Playlist likePlaylist(Long userId, int CategoryId){
        Optional<Playlist> playlist = getAll().stream().filter(p->p.getCategoryPlaylist().getCategoryPlaylistId()==CategoryId && p.getUser().getUserId().equals(userId)).findFirst();
        return playlist.get();
    }
}
