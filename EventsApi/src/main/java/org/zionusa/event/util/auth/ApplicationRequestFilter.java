package org.zionusa.event.util.auth;

import org.zionusa.base.util.auth.BaseApplicationRequestFilter;
import org.zionusa.event.domain.Application.ApplicationDao;
import org.zionusa.event.domain.Application.Application;

public class ApplicationRequestFilter extends BaseApplicationRequestFilter<Application> {

    public ApplicationRequestFilter(ApplicationDao applicationDao) {
        super(applicationDao);
    }

}
