package org.zionusa.base.dao;

import org.zionusa.base.domain.BaseDomain;

import java.util.Optional;

public interface BaseApplicationDao<T extends BaseDomain<K>, K> extends BaseDao<T, K> {

    Optional<T> getAllByUniqueId(String uniqueId);

}
