package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Category;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Playlist;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Song;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.PlaylistRepository;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.SongRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private SongRepository songRepository;

    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }
    public List<Playlist> getPlaylistsByUser(User user) {
        return playlistRepository.findByUserAndIsDeleteFalse(user);
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


    public Playlist likePlaylist(Long userId, int CategoryPlaylistId){
        Optional<Playlist> playlist = getAll().stream()
                .filter(p->p.getCategoryPlaylist().getCategoryPlaylistId()==CategoryPlaylistId && p.getUser().getUserId().equals(userId)).findFirst();
        return playlist.get();
    }

    public Optional<Playlist> getPlaylistById(int id){return playlistRepository.findById(id);}

    public String saveImage(MultipartFile image) throws IOException {
        File staticImagesFolder = new File("target/classes/static/images");
        if (!staticImagesFolder.exists()) {
            staticImagesFolder.mkdirs();
        }
        String fileName =image.getOriginalFilename();
        Path path = Paths.get(staticImagesFolder.getAbsolutePath() + File.separator + fileName);
        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "/images/" + fileName;
    }
    public void updatePlaylist(@NotNull Playlist playlist) {
        Playlist ex_playlist = playlistRepository.findById((int)
                        playlist.getPlaylistId())
                .orElseThrow(() -> new IllegalStateException("Playlist with ID " +
                        playlist.getPlaylistId() + " does not exist."));
        ex_playlist.setPlaylistName(playlist.getPlaylistName());
        ex_playlist.setImage(playlist.getImage());
        playlistRepository.save(ex_playlist);
    }

    public List<Playlist> getAlbum()
    {
        return playlistRepository.findAll().stream()
                .filter(p -> p.getCategoryPlaylist().getCategoryPlaylistId() == 3).toList();
    }

    public void deletedById(int id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Playlist does not exist."));
        playlist.setDelete(!playlist.isDelete());
        playlistRepository.save(playlist);
    }

    public void addSongToPlaylist(int playlistId, int songId, User user) throws Exception {
        Playlist playlist = playlistRepository.findByPlaylistIdAndUser(playlistId, user)
                .orElseThrow(() -> new AccessDeniedException("You don't have permission to modify this playlist"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (!playlist.getSongPlaylist().contains(song)) {
            playlist.getSongPlaylist().add(song);
            playlist.setQuantity(playlist.getQuantity() + 1);
            playlistRepository.save(playlist);
        }
    }
    public void removeSongFromPlaylist(int playlistId, int songId, User currentUser) {
        Playlist playlist = getPlaylistById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid playlist Id:" + playlistId));

        if (!playlist.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException("You don't have permission to modify this playlist");
        }

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid song Id:" + songId));

        if (playlist.getSongPlaylist().remove(song)) {
            playlist.setQuantity(playlist.getQuantity() - 1);

            song.setLikeCount(song.getLikeCount()-1);
            songRepository.save(song);
            playlistRepository.save(playlist);
        } else {
            throw new IllegalStateException("Song is not in the playlist");
        }
    }
}
