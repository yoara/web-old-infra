package org.yoara.framework.component.web.common.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoara on 2016/6/13.
 */
public class ValidationResult implements Serializable {
    private boolean hasError;
    private List<ValidationErrorMessage> errorMessages = new ArrayList<>();

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public boolean hasError() {
        return hasError;
    }

    public List<ValidationErrorMessage> getErrorMessages() {
        return errorMessages;
    }

    public void addErrorMessage(ValidationErrorMessage errorMessage) {
        errorMessages.add(errorMessage);
    }
}
