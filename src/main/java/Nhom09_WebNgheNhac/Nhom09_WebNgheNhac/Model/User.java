package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Validator.Unique;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
@Data
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Setter
    @Column(name = "userName",nullable = false, unique = true)
    @NotBlank(message = "Username không được bỏ trống")
    @Unique(message = "Username đã được dùng")
    @Size(min = 1, max = 50, message = "Username không quá 50 ký tự")
    private String userName;

    @Column(nullable = false)
    @NotEmpty(message = "FullName không được để trống")
    @Size(min =5 , message = "FullName ít nhất 5 ký tự")
    private  String fullName;

    @Column(name = "password",nullable = false)
    @NotBlank(message = "Password không được bỏ trống")
    @Pattern(regexp = "^[A-Z].*(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$",message = "Password bắt đầu bằng chữ hoa, có 1 số và 1 kí tự đặc biệt")
    @Size(min = 8, message = "Password phải từ 8 ký tự")
    private String password;

//    @NotBlank(message = "Password không được bỏ trống và không chứa khoảng cách")
//    @Size(min=8,message = "Password ít nhất phải 8 ký tự")
//    @Pattern(regexp = "^[A-Z].*(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$",message = "Password bắt đầu bằng chữ hoa, có 1 số và 1 kí tự đặc biệt")
//    @Column(name = "Password",nullable = false)
//    private String password;

    @Column(unique = true,nullable = false)
    @NotBlank(message = "Email không được bỏ trống")
    @Size(min = 1, max = 50, message = "Email không quá 50 ký tự")
    @Unique(message = "Email đã tồn tại")
    @Email
    private String email;


    @Column(unique = true,nullable = false)
    @Length(min = 10, max = 10, message = "Phone phải có 10 số")
    @Pattern(regexp = "[0][0-9]*$", message = "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số")
    @Unique(message = "PhoneNumber đã tồn tại")
    private String phoneNumber;

    @Column(nullable = false)
    @NotNull(message = "Birth Date không được bỏ trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Birth Date phải là ngày trong quá khứ")
    private LocalDate birthDate;

    @Column(nullable = false)
    @NotNull(message = "Country không được bỏ trống")
    private String country;

    @Column(nullable = false)
    private String image;

    private LocalDateTime timePremium;

    private LocalDateTime timeSinger;

    @Column(nullable = false)
    private boolean isDelete;

    @Column(nullable = false)
    private boolean isPremium;

    @Column(nullable = false)
    private int countReport;

    @OneToMany(mappedBy = "user")
    private Set<Playlist> playlists;

    @OneToMany(mappedBy = "user")
    private Set<Invoice> invoices;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();



    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private Set<Song> songs = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> userRoles = this.getRoles();
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .toList();
    }



    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public String getUserName() {
        return userName;
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
        return getUserId() != null && Objects.equals(getUserId(), user.getUserId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}