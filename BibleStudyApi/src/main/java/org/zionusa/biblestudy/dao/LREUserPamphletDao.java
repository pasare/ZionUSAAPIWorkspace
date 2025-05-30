package org.zionusa.biblestudy.dao;

import org.zionusa.base.dao.BaseDao;
import org.zionusa.biblestudy.domain.LREUserPamphlet;

import java.util.List;

public interface LREUserPamphletDao extends BaseDao<LREUserPamphlet, Integer> {

    List<LREUserPamphlet> findAllByUserId(Integer userId);

    LREUserPamphlet findByBarcode(String barcode);

    List<LREUserPamphlet> findAllByChurchId(Integer churchId);

    List<LREUserPamphlet> findAllByParentChurchId(Integer parentChurchId);

}
