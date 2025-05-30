package org.zionusa.base.util.auth;

import org.springframework.web.filter.OncePerRequestFilter;
import org.zionusa.base.dao.BaseApplicationDao;
import org.zionusa.base.domain.BaseApplication;
import org.zionusa.base.util.exceptions.ForbiddenException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.zionusa.base.util.auth.SecurityConstants.X_APPLICATION_ID;

public class BaseApplicationRequestFilter<T extends BaseApplication> extends OncePerRequestFilter {

    BaseApplicationDao<T, Integer> baseApplicationDao;

    public BaseApplicationRequestFilter(BaseApplicationDao<T, Integer> baseApplicationDao) {
        this.baseApplicationDao = baseApplicationDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Allow X_APPLICATION_ID to be sent as a query parameter when downloading files
        String applicationUniqueId = request.getRequestURI().contains("download")
            ? request.getParameter("x")
            : request.getHeader(X_APPLICATION_ID);

        if (applicationUniqueId == null || applicationUniqueId.trim().equals("")) {
            // Application does not exist
            throw new UnknownException();
        }

        Optional<T> applicationOptional = baseApplicationDao.getAllByUniqueId(applicationUniqueId);

        if (!applicationOptional.isPresent()) {
            // Application does not exist
            throw new UnknownException();
        }

        if (!Boolean.TRUE.equals(applicationOptional.get().getEnabled())) {
            // Application is not enabled
            throw new UnknownException();
        }

        filterChain.doFilter(request, response);
    }

    // Log an exception without showing the stacktrace
    public static class UnknownException extends ForbiddenException {
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
