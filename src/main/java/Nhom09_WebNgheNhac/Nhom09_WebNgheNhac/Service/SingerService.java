package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Singer;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.SingerRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SingerService {

    private final SingerRepository singerRepository;

    public List<Singer> getAllSinger(){
        return singerRepository.findAll();
    }
    public void addCategory(Singer singer) {
        singerRepository.save(singer);
    }

    public Optional<Singer> getSingerById(int id) {
        return singerRepository.findById(id);
    }

    public void updateSinger(@NotNull Singer singer) {
        Singer existingSinger = singerRepository.findById((int)
                        singer.getSingerId())
                .orElseThrow(() -> new IllegalStateException("Category with ID " +
                        singer.getSingerId() + " does not exist."));
        existingSinger.setSingerName(singer.getSingerName());
        existingSinger.setGender(singer.isGender());
        existingSinger.setImage(singer.getImage());
        singerRepository.save(existingSinger);
    }

    @Async
    public String saveImage(MultipartFile image) throws IOException {
        File staticImagesFolder = new File("target/classes/static/images/singer");
        if (!staticImagesFolder.exists()) {
            staticImagesFolder.mkdirs();
        }
        String fileName = image.getOriginalFilename();
        Path path = Paths.get(staticImagesFolder.getAbsolutePath() + File.separator + fileName);
        Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "/images/singer/" + fileName;
    }

    public void deleteSingerById(int id) {
        if (!singerRepository.existsById(id)) {
            throw new IllegalStateException("Singer with ID " + id + " does not exist.");
        }
        singerRepository.deleteById(id);
    }
}
