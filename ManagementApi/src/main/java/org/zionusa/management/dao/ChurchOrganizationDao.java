package org.zionusa.management.dao;


import org.zionusa.base.dao.BaseDao;
import org.zionusa.management.domain.ChurchOrganization;

import java.util.Optional;

@Deprecated
public interface ChurchOrganizationDao extends BaseDao<ChurchOrganization, Integer> {

    Optional<ChurchOrganization> getChurchOrganizationByChurchId(Integer churchId);
}
