package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Playlist;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayListService {
    private final PlaylistRepository playlistRepository;
    public List<Playlist> getAllPlaylist(){return playlistRepository.findAll();}

}
