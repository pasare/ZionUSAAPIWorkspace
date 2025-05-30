package org.zionusa.management.util.auth;

import org.zionusa.base.util.auth.BaseApplicationRequestFilter;
import org.zionusa.management.domain.application.Application;
import org.zionusa.management.domain.application.ApplicationDao;


public class ApplicationRequestFilter extends BaseApplicationRequestFilter<Application> {

    public ApplicationRequestFilter(ApplicationDao applicationDao) {
        super(applicationDao);
    }

}
