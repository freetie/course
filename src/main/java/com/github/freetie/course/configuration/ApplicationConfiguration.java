package com.github.freetie.course.configuration;

import com.github.freetie.course.dao.AccountDao;
import com.github.freetie.course.dao.SessionDao;
import com.github.freetie.course.entity.Account;
import com.github.freetie.course.entity.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.stream.Stream;

@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {
    public static final String COOKIE_TOKEN_NAME = "USER_SESSION";
    SessionDao sessionDao;
    AccountDao accountDao;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccountInterceptor(sessionDao, accountDao));
    }

    public static class AccountContext {
        private static final ThreadLocal<Account> currentAccount = new ThreadLocal<>();

        public static Optional<String> getCookieToken(HttpServletRequest request) {
            return Stream.of(request.getCookies()).filter(cookie -> cookie.getName().equals(COOKIE_TOKEN_NAME)).map(Cookie::getValue).findFirst();
        }

        public static Account getCurrentAccount() {
            return currentAccount.get();
        }

        public static void setCurrentAccount(Account account) {
            currentAccount.set(account);
        }
    }

    public static class AccountInterceptor implements HandlerInterceptor {
        SessionDao sessionDao;
        AccountDao accountDao;

        public AccountInterceptor(SessionDao sessionDao, AccountDao accountDao) {
            this.sessionDao = sessionDao;
            this.accountDao = accountDao;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            Optional<String> optionalToken = AccountContext.getCookieToken(request);
            optionalToken.ifPresent(token -> {
                Session session = sessionDao.findByToken(token);
                if (session != null) {
                    AccountContext.setCurrentAccount(accountDao.findById(session.getAccountId()));
                }
            });
            return true;
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            AccountContext.setCurrentAccount(null);
        }
    }
}
