package com.honlife.core.app.model.member.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import com.honlife.core.app.model.member.service.MemberImageService;
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

/**
 * Validate that the id value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
    validatedBy = MemberImageMemberUnique.MemberImageMemberUniqueValidator.class
)
public @interface MemberImageMemberUnique {

    String message() default "{Exists.memberImage.member}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class MemberImageMemberUniqueValidator implements
        ConstraintValidator<MemberImageMemberUnique, Long> {

        private final MemberImageService memberImageService;
        private final HttpServletRequest request;

        public MemberImageMemberUniqueValidator(final MemberImageService memberImageService,
            final HttpServletRequest request) {
            this.memberImageService = memberImageService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Long value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(memberImageService.get(Long.parseLong(currentId)).getMember())) {
                // value hasn't changed
                return true;
            }
            return !memberImageService.memberExists(value);
        }

    }

}
