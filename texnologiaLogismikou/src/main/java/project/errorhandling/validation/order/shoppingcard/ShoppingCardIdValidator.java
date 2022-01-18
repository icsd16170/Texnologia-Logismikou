package project.errorhandling.validation.order.shoppingcard;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import project.service.shoppingCard.ShoppingCardService;

public class ShoppingCardIdValidator implements
        ConstraintValidator<ValidShoppingCardId, Long> {

    private final ShoppingCardService shoppingCardService;

    public ShoppingCardIdValidator(ShoppingCardService shoppingCardService) {
        this.shoppingCardService = shoppingCardService;
    }

    @Override
    public void initialize(ValidShoppingCardId shoppingCardId) {
    }

    @Override
    public boolean isValid(Long shoppingCardId,
            ConstraintValidatorContext cxt) {
        return shoppingCardService.existsById(shoppingCardId);
    }

}