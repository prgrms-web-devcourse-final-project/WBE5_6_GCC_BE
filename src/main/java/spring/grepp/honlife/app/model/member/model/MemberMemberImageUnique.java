package spring.grepp.honlife.app.model.member.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;
import spring.grepp.honlife.app.model.member.service.MemberService;


/**
 * Validate that the id value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = MemberMemberImageUnique.MemberMemberImageUniqueValidator.class
)
public @interface MemberMemberImageUnique {

    String message() default "{Exists.member.memberImage}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class MemberMemberImageUniqueValidator implements ConstraintValidator<MemberMemberImageUnique, Integer> {

        private final MemberService memberService;
        private final HttpServletRequest request;

        public MemberMemberImageUniqueValidator(final MemberService memberService,
                final HttpServletRequest request) {
            this.memberService = memberService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Integer value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(memberService.get(Integer.parseInt(currentId)).getMemberImage())) {
                // value hasn't changed
                return true;
            }
            return !memberService.memberImageExists(value);
        }

    }

}
