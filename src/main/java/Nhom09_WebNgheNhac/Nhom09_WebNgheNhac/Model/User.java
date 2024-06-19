package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(nullable = false,unique = true)
    @NotBlank(message = "UserName không được bỏ trống và không chứa khoảng cách")
    @Size(min = 6,message = "UserName ít nhất phải 6 ký tự")
    private String userName;

    @Column(nullable = false)
    @NotNull(message = "FullName không được bỏ trống")
    @Size(min = 4,message = "FullName ít nhất phải 4 ký tự")
    private String fullName;


    @Column(nullable = false,unique = true)
    @Email(message = "Email không hợp lệ")
    @NotNull(message = "Email không được để trống")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password không được bỏ trống và không chứa khoảng cách")
    @Size(min=8,message = "Password ít nhất phải 8 ký tự")
    @Pattern(regexp = "^[A-Z].*(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$",message = "Password bắt đầu bằng chữ hoa, có 1 số và 1 kí tự đặc biệt")
    private String password;

    @Column(nullable = false)
    @Past(message = "Ngày sinh không hợp lệ")
    @NotNull(message = "Ngày sinh không được bỏ trống")
    private Date birthDate;

    @Column(nullable = false,unique = true)
    @Pattern(regexp ="^[0][0-9]{9}$",message = "PhoneNumber không hợp lệ")
    @NotNull
    private String phoneNumber;

    @Column(nullable = false)
    @NotNull(message = "Quốc gia không được bỏ trống")
    private String country;

    private String image;

    @ManyToOne
    @JoinColumn(referencedColumnName = "roleId")
    private Role role;
}
