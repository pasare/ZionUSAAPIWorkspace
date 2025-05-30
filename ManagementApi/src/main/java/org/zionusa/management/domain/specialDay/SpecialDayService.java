package org.zionusa.management.domain.specialDay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;

import java.util.List;

@Service
public class SpecialDayService extends BaseService<SpecialDay, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SpecialDayService.class);
    private final SpecialDayDao dao;

    public SpecialDayService(SpecialDayDao dao) {
        super(dao, logger, SpecialDay.class);
        this.dao = dao;
    }

    public List<SpecialDay> getAllBetweenDates(String startDate, String endDate) {
       return  dao.findAllByDateBetween(startDate, endDate);
    }
}
