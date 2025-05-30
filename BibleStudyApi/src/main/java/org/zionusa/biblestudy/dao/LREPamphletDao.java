package org.zionusa.biblestudy.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.biblestudy.domain.LREPamphlet;

import java.util.List;

public interface LREPamphletDao extends BaseDao<LREPamphlet, Integer> {

    List<LREPamphlet> findByName(String name);

    List<LREPamphlet> findByVersionAndName(String version, String name);

}
