package org.zionusa.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.management.dao.UserPreferencesDao;
import org.zionusa.management.domain.UserPreference;

import java.util.List;

@Service
public class UserPreferencesService extends BaseService<UserPreference, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(UserPreferencesService.class);

    private final UserPreferencesDao userPreferencesDao;

    @Autowired
    public UserPreferencesService(UserPreferencesDao userPreferencesDao) {
        super(userPreferencesDao, logger, UserPreference.class);
        this.userPreferencesDao = userPreferencesDao;
    }

    public List<UserPreference> getByUserId(Integer id) {
        return userPreferencesDao.getAllByUserId(id);
    }
}
