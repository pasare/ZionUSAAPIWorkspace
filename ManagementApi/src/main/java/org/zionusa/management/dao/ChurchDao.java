package org.zionusa.management.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zionusa.base.dao.BaseDao;
import org.zionusa.management.domain.Church;

import java.util.List;
import java.util.Optional;

@Deprecated
public interface ChurchDao extends BaseDao<Church, Integer> {

    List<Church> getAllByArchivedFalse();

    @Transactional
    @Query("from Church c where c.leaderId = :leaderId")
    Optional<Church> getChurchByLeaderId(@Param("leaderId") Integer leaderId);

    @Transactional
    @Query("from Church c where c.leaderId = :leaderId")
    List<Church> getChurchesByLeaderId(@Param("leaderId") Integer leaderId);

    List<Church> getByParentChurchId(Integer parentChurchId);

    Church getFirstByNameLikeAndHiddenLocationIsTrue(String name);

}
