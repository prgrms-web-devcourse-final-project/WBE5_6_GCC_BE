package com.honlife.core.app.model.notification.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import com.honlife.core.app.model.notification.service.NotificationService;
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
    validatedBy = NotificationMemberUnique.NotificationMemberUniqueValidator.class
)
public @interface NotificationMemberUnique {

    String message() default "{Exists.notification.member}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class NotificationMemberUniqueValidator implements
        ConstraintValidator<NotificationMemberUnique, Long> {

        private final NotificationService notificationService;
        private final HttpServletRequest request;

        public NotificationMemberUniqueValidator(final NotificationService notificationService,
            final HttpServletRequest request) {
            this.notificationService = notificationService;
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
            if (currentId != null && value.equals(notificationService.get(Long.parseLong(currentId)).getMember())) {
                // value hasn't changed
                return true;
            }
            return !notificationService.memberExists(value);
        }

    }

}
