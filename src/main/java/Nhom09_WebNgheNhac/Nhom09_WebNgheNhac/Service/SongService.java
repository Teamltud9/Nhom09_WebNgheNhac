package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Song;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.SongRepository;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SongService {

    private final SongRepository songRepository;

    public List<Song> getAllSong(){
        return songRepository.findAll();
    }
    public Song addSong(Song song) {
        return songRepository.save(song);
    }
    public Optional<Song> getSongId(int id) {
        return songRepository.findById(id);
    }

    public Song updateSong(Song song) {
        Song existingsSong = songRepository.findById((int) song.getSongId())
                .orElseThrow(() -> new IllegalStateException("Song with ID " +
                        song.getSongId() + " does not exist."));
        existingsSong.setSongName(song.getSongName());
        existingsSong.setReleaseDate(song.getReleaseDate());
        existingsSong.setTime(song.getTime());
        existingsSong.setImage(song.getImage());
        existingsSong.setFilePath(song.getFilePath());


        return songRepository.save(existingsSong);
    }

    public void deleteSongById(int id) {
        if (!songRepository.existsById(id)) {
            throw new IllegalStateException("Song with ID " + id + " does not exist.");
        }
        songRepository.deleteById(id);
    }
}
