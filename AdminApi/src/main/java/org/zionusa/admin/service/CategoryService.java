package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.CategoryDao;
import org.zionusa.admin.dao.ChallengeDao;
import org.zionusa.admin.domain.Category;
import org.zionusa.admin.domain.activities.Activity;
import org.zionusa.admin.service.activities.ActivityService;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryService extends BaseService<Category, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final ActivityService activityService;
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(ActivityService activityService, CategoryDao categoryDao, ChallengeDao challengeDao) {
        super(categoryDao, logger, Category.class);
        this.activityService = activityService;
        this.categoryDao = categoryDao;
    }

    @Override
    @Transactional
    public void delete(Integer id) throws NotFoundException {

        Optional<Category> categoryOptional = categoryDao.findById(id);
        //delete all activities associated with this category as well

        if (!categoryOptional.isPresent())
            throw new NotFoundException("Cannot delete a category that does not exist");

        List<Activity> activities = this.activityService.getActivitiesByCategoryId(id);

        if (activities != null && activities.size() > 0) {

            for (Activity activity : activities) {
                activityService.delete(activity.getId());
            }
        }

        categoryDao.delete(categoryOptional.get());
    }
}
