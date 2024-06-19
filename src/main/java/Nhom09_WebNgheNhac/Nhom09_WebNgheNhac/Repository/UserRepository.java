package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository;


import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUserName(String userName);
    public User findByEmail(String email);


}

