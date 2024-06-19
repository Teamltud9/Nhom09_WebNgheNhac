package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;


    public List<User> getAllUser(){
        return userRepository.findAll();
    }
    public User addUser(User user) {
        return userRepository.save(user);
    }
    public Optional<User> getUserId(int id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById((int) user.getUserId())
                .orElseThrow(() -> new IllegalStateException("Product with ID " +
                        user.getUserId() + " does not exist."));
        existingUser.setUserName(user.getUserName());
        existingUser.setFullName(user.getFullName());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());;


        return userRepository.save(existingUser);
    }


    public void deleteUserById(int id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        userRepository.deleteById(id);
    }
}
