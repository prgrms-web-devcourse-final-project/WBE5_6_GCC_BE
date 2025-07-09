package com.honlife.core.app.model.member.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import com.honlife.core.app.model.member.service.MemberPointService;
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
    validatedBy = MemberPointMemberUnique.MemberPointMemberUniqueValidator.class
)
public @interface MemberPointMemberUnique {

    String message() default "{Exists.memberPoint.member}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class MemberPointMemberUniqueValidator implements
        ConstraintValidator<MemberPointMemberUnique, Long> {

        private final MemberPointService memberPointService;
        private final HttpServletRequest request;

        public MemberPointMemberUniqueValidator(final MemberPointService memberPointService,
            final HttpServletRequest request) {
            this.memberPointService = memberPointService;
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
            if (currentId != null && value.equals(memberPointService.get(Long.parseLong(currentId)).getMember())) {
                // value hasn't changed
                return true;
            }
            return !memberPointService.memberExists(value);
        }

    }

}
