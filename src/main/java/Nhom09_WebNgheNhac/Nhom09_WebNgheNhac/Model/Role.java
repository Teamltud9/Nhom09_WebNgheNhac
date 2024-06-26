package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    @NotBlank(message = "Name is required")
    @Column(length = 50, nullable = false)
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String roleName;


    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<User> users = new HashSet<>();


    @Override
    public String getAuthority() {
        return roleName;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return
                false;
        Role role = (Role) o;
        return getRoleId() != null && Objects.equals(getRoleId(), role.getRoleId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
