package org.zionusa.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.event.dao.UserPreferencesDao;
import org.zionusa.event.domain.UserPreference;

import java.util.List;

@Service
@CacheConfig(cacheNames = "user-preferences-cache")
public class UserPreferencesService extends BaseService<UserPreference, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(UserPreferencesService.class);

    private final UserPreferencesDao userPreferencesDao;

    @Autowired
    public UserPreferencesService(UserPreferencesDao userPreferencesDao) {
        super(userPreferencesDao, logger, UserPreference.class);
        this.userPreferencesDao = userPreferencesDao;
    }

    @Cacheable
    public List<UserPreference> getByUserId(Integer id) {
        return userPreferencesDao.getAllByUserId(id);
    }
}
