package com.github.freetie.course.aspect;

import com.github.freetie.course.annotation.RoleControl;
import com.github.freetie.course.configuration.ApplicationConfiguration;
import com.github.freetie.course.entity.Account;
import com.github.freetie.course.entity.HttpException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
//@Configuration
public class PermissionAspect {
    @Around("@annotation(com.github.freetie.course.annotation.RoleControl)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RoleControl roleControl) throws Throwable {
        Account currentAccount = ApplicationConfiguration.AccountContext.getCurrentAccount();
        if (currentAccount.getRole().equals(roleControl.role())) {
            return joinPoint.proceed();
        }
        throw new HttpException(403, "Forbidden");
    }
}
