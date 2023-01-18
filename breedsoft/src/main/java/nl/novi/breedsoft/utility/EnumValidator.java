package nl.novi.breedsoft.utility;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enumeration validator. Validates if a given String exists in a given Enumeration.
 * Can be used as an Annotation to validate Strings against Enumerations.
 * @see ConstraintValidator
 */
public class EnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    private List<String> acceptedValues;

    /**
     * Initialize ValueOfEnumValidator with given Enumeration.
     * @param annotation Enumeration to be validated against
     */
    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    /**
     * @param value String to be checked if exists in initialized Enumeration
     * @param context could be used to override default error message generation (not used)
     * @return true if given String exists in initialized Enumeration else false
     */
    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return acceptedValues.contains(value.toString());
    }
}
