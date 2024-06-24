package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Role findRoleByRoleId(Long id);
}