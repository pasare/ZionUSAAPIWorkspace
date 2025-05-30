package org.zionusa.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.base.domain.BaseDomain;

import java.util.List;

public interface BaseDao<T extends BaseDomain<K>, K> extends JpaRepository<T, K> {

    List<T> getAllByArchivedIsFalse();

    List<T> getAllByArchivedIsTrue();

}
