package by.itransition.webconstructor.validation;

import by.itransition.webconstructor.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches passwordMatches) {

    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        UserDto user = (UserDto) object;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
