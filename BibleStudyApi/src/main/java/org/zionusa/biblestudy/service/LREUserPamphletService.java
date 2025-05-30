package org.zionusa.biblestudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;
import org.zionusa.biblestudy.dao.LREUserPamphletDao;
import org.zionusa.biblestudy.domain.LREUserPamphlet;

import java.util.List;

@Service
public class LREUserPamphletService extends BaseService<LREUserPamphlet, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(LREUserPamphletService.class);
    private final LREUserPamphletDao lreUserPamphletDao;

    @Autowired
    public LREUserPamphletService(LREUserPamphletDao lreUserPamphletDao) {
        super(lreUserPamphletDao, logger, LREUserPamphlet.class);
        this.lreUserPamphletDao = lreUserPamphletDao;
    }

    public List<LREUserPamphlet> getByUser(Integer userId) {
        return this.lreUserPamphletDao.findAllByUserId(userId);
    }

    public List<LREUserPamphlet> getByChurch(Integer churchId) {
        return this.lreUserPamphletDao.findAllByChurchId(churchId);
    }

    public List<LREUserPamphlet> getByParentChurch(Integer parentChurchId) {
        return this.lreUserPamphletDao.findAllByParentChurchId(parentChurchId);
    }

    public LREUserPamphlet getByBarcode(String barcode) {
        return this.lreUserPamphletDao.findByBarcode(barcode);
    }

}
