package com.github.freetie.course.aspect;

import com.github.freetie.course.annotation.RoleControl;
import com.github.freetie.course.configuration.ApplicationConfiguration;
import com.github.freetie.course.entity.Account;
import com.github.freetie.course.entity.HttpException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PermissionAspect {
    @Around("@annotation(roleControl)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RoleControl roleControl) throws Throwable {
        Account currentAccount = ApplicationConfiguration.AccountContext.getCurrentAccount();
        if (currentAccount == null || !currentAccount.getRole().equals(roleControl.value())) {
            throw new HttpException(403, "Forbidden");
        }
        return joinPoint.proceed();
    }
}
