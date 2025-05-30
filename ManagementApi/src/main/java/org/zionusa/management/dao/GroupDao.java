package org.zionusa.management.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.management.domain.Group;

import java.util.List;
import java.util.Optional;

public interface GroupDao extends BaseDao<Group, Integer> {

    Optional<Group> getGroupByChurchIdAndChurchGroupTrue(Integer churchId);

    Group getGroupByIdAndArchivedIsFalse(Integer id);

    List<Group> getGroupsByArchivedIsFalse();

    List<Group> getGroupsByChurchIdAndArchivedIsFalse(Integer churchId);

    List<Group> getGroupsByChurchId(Integer churchId);

    Optional<Group> getGroupByLeaderId(Integer leaderId);

    List<Group> getGroupsByLeaderId(Integer leaderId);
}
