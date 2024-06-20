package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Validator.Unique;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
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
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 50, unique = true)
    @NotBlank(message = "Username không được bỏ trống")
    @Unique(message = "Username đã được dùng")
    @Size(min = 1, max = 50, message = "Username không quá 50 ký tự")
    private String username;

    @Column(name = "password", length = 250)
    @NotBlank(message = "Password không được bỏ trống")
    @Size(min = 8, message = "Password phải từ 8 ký tự")
    private String password;

//    @NotBlank(message = "Password không được bỏ trống và không chứa khoảng cách")
//    @Size(min=8,message = "Password ít nhất phải 8 ký tự")
//    @Pattern(regexp = "^[A-Z].*(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$",message = "Password bắt đầu bằng chữ hoa, có 1 số và 1 kí tự đặc biệt")
//    @Column(name = "Password",nullable = false)
//    private String password;

    @Column(name = "email", length = 50, unique = true)
    @NotBlank(message = "Email is required")
    @Size(min = 1, max = 50, message = "Email không quá 50 ký tự")
    @Email
    private String email;
    @Column(name = "phone", length = 10, unique = true)
    @Length(min = 10, max = 10, message = "Phone phải có 10 characters")
    @Pattern(regexp = "^[0-9]*$", message = "Số điện thoại không ở dạng chữ")
    private String phone;
    @Column(name = "provider", length = 50)
    private String provider;
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> userRoles = this.getRoles();
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}