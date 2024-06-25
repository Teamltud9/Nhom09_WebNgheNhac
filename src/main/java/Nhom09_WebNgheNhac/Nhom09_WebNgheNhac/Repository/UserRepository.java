package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository;


import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserName(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);

}

