package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Song;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.SongRepository;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.UserRepository;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SongService {

    private final SongRepository songRepository;

    private final UserRepository userRepository;




    public List<Song> getAllSong(){
        return songRepository.findAll();
    }
    public Song addSong(Song song,String singers) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = (User) authentication.getPrincipal();
        song.setCreateByUser(userLogin.getUserId());


        Optional<User> user1 = userRepository.findById(userLogin.getUserId());
        Set<User> singer = new HashSet<>();
        singer.add(user1.get());

        if(!singers.isEmpty()){
            String[] parts = singers.split(",");

            List<Long> selectedNgheSiIds = Arrays.stream(parts)
                    .map(Long::parseLong)
                    .toList();
            for (Long userId : selectedNgheSiIds){
                Optional<User> user = userRepository.findById(userId);
                singer.add(user.get());
            }
        }

        song.setPremium(!user1.get().getCountry().equals("Vietnam"));
        song.setUsers(singer);
        song.setDelete(false);


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
        existingsSong.setCategory(song.getCategory());
        existingsSong.setTime(song.getTime());
        existingsSong.setImage(song.getImage());
        existingsSong.setFilePath(song.getFilePath());
        
        return songRepository.save(existingsSong);
    }

    public void deleteSongById(int id) {
        Song song = songRepository.findById(id).orElseThrow(() -> new IllegalStateException("Song does not exist."));
        song.setDelete(true);
        songRepository.save(song);
    }


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


    public String saveMusic(MultipartFile image) throws IOException {
        File staticImagesFolder = new File("target/classes/static/music");
        if (!staticImagesFolder.exists()) {
            staticImagesFolder.mkdirs();
        }
        String fileName =image.getOriginalFilename();
        Path path = Paths.get(staticImagesFolder.getAbsolutePath() + File.separator + fileName);
        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "/music/" + fileName;
    }

    public LocalTime timeMusic(MultipartFile mp3File) throws IOException, InvalidDataException, UnsupportedTagException {
        Path tempFile = Files.createTempFile("temp-", ".mp3");

        try {
            Files.copy(mp3File.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            Mp3File mp3 = new Mp3File(tempFile.toString());
            long durationInSeconds = mp3.getLengthInSeconds();

            int minutes = (int) (durationInSeconds / 60);
            int seconds = (int) (durationInSeconds % 60);
            LocalTime duration = LocalTime.of(0, minutes, seconds);

            return duration;
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }


    public List<Song> findByCategoryId(int categoryId) {
        return songRepository.findByCategory_CategoryId(categoryId);
    }
    public List<Song> searchSong(String query) {

        List<Song> songs = getAllSong();

        return songs.stream()
                .filter(title -> title.getSongName().toLowerCase().contains(query.toLowerCase()) || title.getUsers().stream().anyMatch(p->p.getFullName().toLowerCase().contains(query.toLowerCase())))
                .filter(s->!s.isDelete())
                .collect(Collectors.toList());
    }

    public List<String> SearchSuggestions(String query) {
        List<Song> songs = getAllSong().stream().filter(p->!p.isDelete()).toList();
        List<String> suggestions = new ArrayList<>();

        songs.stream()
                .filter(song -> song.getSongName().toLowerCase().contains(query.toLowerCase()) ||
                        song.getUsers().stream().anyMatch(user -> user.getFullName().toLowerCase().contains(query.toLowerCase())))
                .forEach(song -> {
                    if (song.getSongName().toLowerCase().contains(query.toLowerCase())) {
                        suggestions.add(song.getSongName());
                    }
                    else{
                        song.getUsers().stream()
                                .filter(user -> user.getFullName().toLowerCase().contains(query.toLowerCase()))
                                .forEach(user -> suggestions.add(user.getFullName()));
                    }

                });

        return suggestions;
    }
}
