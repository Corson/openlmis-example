package org.openlmis.example.web;

import org.openlmis.example.domain.Foo;
import org.openlmis.example.domain.Notification;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/*
    This class, as opposed to the BarValidator class, is intended to help illustrate
    validation via a Validator rather than through annotations.
 */
public class NotificationValidator implements Validator {
  @Override
  public boolean supports(Class<?> clazz) {
    //Indicate that we only wish to validate Notification objects
    return Notification.class.equals(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    //Ensure that the recipient field isn't empty (null or "")
    ValidationUtils.rejectIfEmpty(errors, "recipient", "recipient.empty");

    Notification notification = (Notification) obj;
    if (!notification.getRecipient().contains("@")) {
      errors.rejectValue("recipient", "invalid.recipient.address");
    }
  }
}
