package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.auth.AuthenticatedUser;
import org.zionusa.biblestudy.dao.StudyDao;
import org.zionusa.biblestudy.domain.Study;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyService extends BaseService<Study, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(StudyService.class);

    private final StudyDao studyDao;

    @Autowired
    public StudyService(StudyDao studyDao) {
        super(studyDao, logger, Study.class);
        this.studyDao = studyDao;
    }

    @Override
    public List<Study> getAll(Boolean archived) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Study> studies = super.getAll(archived);

        if (authenticatedUser.isTheologicalStudent())
            return studies;

        //only theological students can see theological studies
        return studies.stream().filter(study -> !study.isTheological()).collect(Collectors.toList());
    }

    public List<Study> getByCategories(List<Integer> categoryIds) {
        List<Study> studies = new ArrayList<>();

        for (Integer categoryId : categoryIds) {
            List<Study> categoryStudies = studyDao.findByStudyCategoryId(categoryId);

            if (!categoryStudies.isEmpty()) {
                studies.addAll(categoryStudies);
            }
        }

        return studies;
    }

    public Study getByTitle(String title) {
        return studyDao.findByTitle(title);
    }

    public List<Study> getByChapter(Integer chapterNumber) {
        return studyDao.findByChapterNumber(chapterNumber);
    }

    public List<Study> getByBook(Integer bookNumber) {
        return studyDao.findByBookNumber(bookNumber);
    }

    public List<Study> getByStudyType(Integer studyTypeId) {
        return studyDao.findByStudyTypeId(studyTypeId);
    }

    public List<Study> getByStudyCategory(Integer studyCategoryId) {
        return studyDao.findByStudyCategoryId(studyCategoryId);
    }

}
