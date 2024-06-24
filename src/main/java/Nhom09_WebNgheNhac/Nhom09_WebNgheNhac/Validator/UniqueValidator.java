package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Validator;


import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Model.User;
import Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;




public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private final UserRepository userRepository;

    public UniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(Unique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        boolean containsAtSymbol = s.contains("@");
        Optional<User> existingUser = containsAtSymbol ? userRepository.findByEmail(s) : userRepository.findByUserName(s);


         if(!existingUser.isEmpty()){
             return false;
         }
         return true;

    }

}
