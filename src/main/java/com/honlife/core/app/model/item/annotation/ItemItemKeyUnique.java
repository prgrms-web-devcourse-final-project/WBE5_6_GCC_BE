package com.honlife.core.app.model.item.annotation;

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
import com.honlife.core.app.model.item.service.ItemService;


/**
 * Validate that the itemKey value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = ItemItemKeyUnique.ItemItemKeyUniqueValidator.class
)
public @interface ItemItemKeyUnique {

    String message() default "{Exists.item.itemKey}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ItemItemKeyUniqueValidator implements ConstraintValidator<ItemItemKeyUnique, String> {

        private final ItemService itemService;
        private final HttpServletRequest request;

        public ItemItemKeyUniqueValidator(final ItemService itemService,
                final HttpServletRequest request) {
            this.itemService = itemService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(itemService.get(Long.parseLong(currentId)).getItemKey())) {
                // value hasn't changed
                return true;
            }
            return !itemService.itemKeyExists(value);
        }

    }

}
