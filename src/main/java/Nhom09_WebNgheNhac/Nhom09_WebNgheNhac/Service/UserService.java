package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;


import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Playlist;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Song;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.CategoryPlaylistRepository;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.PlaylistRepository;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.RoleRepository;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.UserRepository;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Role;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private CategoryPlaylistRepository categoryPlaylistRepository;


    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    // Lưu người dùng mới vào cơ sở dữ liệu sau khi mã hóa mật khẩu.
    public void save(@NotNull User user, MultipartFile imageFile) throws IOException {
        if(imageFile.isEmpty()){
            user.setImage("/images/AnhMacDinh.jpg");
        }
        else {
            user.setImage(saveImage(imageFile));
        }

        user.setPremium(false);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.getRoles().add(roleRepository.findRoleByRoleId(Role.USER.value));
        user.setDelete(false);
        user.setCountReport(0);

        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlist.setDelete(false);
        playlist.setQuantity(0);
        playlist.setCategoryPlaylist(categoryPlaylistRepository.findById(1).get());
        playlist.setPlaylistName("Yêu thích");


        playlistRepository.save(playlist);
        userRepository.save(user);
    }

    /*// Gán vai trò mặc định cho người dùng dựa trên tên người dùng.
    public void setDefaultRole(String username) {
        userRepository.findByUserName(username).ifPresentOrElse(
                user -> {

                    user.getRoles().add(roleRepository.findRoleByRoleId(Role.USER.value));
                    userRepository.save(user);
                },
                () -> {
                    throw new UsernameNotFoundException("User not found");
                }
        );
    }*/
    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
       /* return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();*/


        return user;

    }

    public void setRole(User user){

        Set<Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Role> roles = new HashSet<>();
        if(user.getRoles().stream().anyMatch(role -> role.getRoleId().equals(Role.SINGER.value))){
            roles.add(roleRepository.findRoleByRoleId(Role.USER.value));
        }
        else
            roles.add(roleRepository.findRoleByRoleId(Role.SINGER.value));
        user.setRoles(roles);


        userRepository.save(user);
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user, currentAuth.getCredentials(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }
    public Optional<User> getUserId(Long id) {
        return userRepository.findById(id);
    }

    // Tìm kiếm người dùng dựa trên tên đăng nhập.
    public Optional<User> findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username);
    }

    // Kiểm tra tính duy nhất của username.
    public boolean isUsernameUnique(String username) {
        return !userRepository.findByUserName(username).isPresent();
    }

    // Kiểm tra tính duy nhất của email.
    public boolean isEmailUnique(String email) {
        return !userRepository.findByEmail(email).isPresent();
    }


    public User editUser(User user) {
        User existingsUser = userRepository.findById((Long) user.getUserId())
                .orElseThrow(() -> new IllegalStateException("User with ID " +
                        user.getUserId() + " does not exist."));
        existingsUser.setUserName(user.getUserName());
        existingsUser.setFullName(user.getFullName());
        existingsUser.setPhoneNumber(user.getPhoneNumber());
        existingsUser.setBirthDate(user.getBirthDate());
        existingsUser.setEmail(user.getEmail());
        existingsUser.setImage(user.getImage());
        existingsUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(existingsUser, currentAuth.getCredentials(), existingsUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        return userRepository.save(existingsUser);
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
}
