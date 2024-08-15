package tutorin.com.validation.job;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tutorin.com.annotation.job.FutureDate;

import java.util.Date;
import java.util.Calendar;

public class FutureDateValidator implements ConstraintValidator<FutureDate, Date> {
    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date tomorrowStart = calendar.getTime();

        return value.after(tomorrowStart);
    }
}

