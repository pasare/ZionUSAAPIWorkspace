package org.zionusa.biblestudy.util.auth;

import org.zionusa.base.util.auth.BaseApplicationRequestFilter;
import org.zionusa.biblestudy.dao.ApplicationDao;
import org.zionusa.biblestudy.domain.Application;

public class ApplicationRequestFilter extends BaseApplicationRequestFilter<Application> {

    public ApplicationRequestFilter(ApplicationDao applicationDao) {
        super(applicationDao);
    }

}
