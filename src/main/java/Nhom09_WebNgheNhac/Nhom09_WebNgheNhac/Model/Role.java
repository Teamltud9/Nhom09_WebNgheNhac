package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @Column(nullable = false)
    @NotNull
    private String roleName;

    @OneToMany(mappedBy = "role")
    private Set<User> users;
}
