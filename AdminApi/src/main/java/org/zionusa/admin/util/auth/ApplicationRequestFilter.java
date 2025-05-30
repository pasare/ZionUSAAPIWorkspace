package org.zionusa.admin.util.auth;

import org.zionusa.admin.dao.ApplicationDao;
import org.zionusa.admin.domain.Application;
import org.zionusa.base.util.auth.BaseApplicationRequestFilter;

public class ApplicationRequestFilter extends BaseApplicationRequestFilter<Application> {

    public ApplicationRequestFilter(ApplicationDao applicationDao) {
        super(applicationDao);
    }

}
