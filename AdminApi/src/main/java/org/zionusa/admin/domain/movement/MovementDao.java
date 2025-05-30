package org.zionusa.admin.domain.movement;

import org.zionusa.base.dao.BaseDao;

public interface MovementDao extends BaseDao<Movement, Integer> {

    Movement getFirstByActiveIsTrue();
}
