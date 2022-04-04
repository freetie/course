package com.github.freetie.course.aspect;

import com.github.freetie.course.annotation.RoleControl;
import com.github.freetie.course.configuration.ApplicationConfiguration;
import com.github.freetie.course.dao.RoleDao;
import com.github.freetie.course.entity.Account;
import com.github.freetie.course.entity.HttpException;
import com.github.freetie.course.entity.Role;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Aspect
@Configuration
public class PermissionAspect {
    RoleDao roleDao;

    @Around("@annotation(com.github.freetie.course.RoleControl)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RoleControl roleControl) throws Throwable {
        Account currentAccount = ApplicationConfiguration.AccountContext.getCurrentAccount();
        List<Role> roles = roleDao.findAllByAccountId(currentAccount.getId());
        if (roles.stream().allMatch(role1 -> role1.getName().equals(roleControl.role()))) {
            return joinPoint.proceed();
        }
        throw new HttpException(403, "Forbidden");
    }
}
