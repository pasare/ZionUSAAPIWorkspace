package org.zionusa.admin.dao;

import org.zionusa.admin.domain.MothersTeaching;
import org.zionusa.base.dao.BaseDao;

import java.util.List;

public interface MothersTeachingDao extends BaseDao<MothersTeaching, Integer> {

    List<MothersTeaching> getMothersTeachingByLanguage(String language);

    MothersTeaching getMothersTeachingByNumber(Integer number);

}
