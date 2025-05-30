package org.zionusa.management.domain.country;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;

import java.util.List;
import java.util.Map;

@Service
public class CountryService extends BaseService<Country, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    @Autowired
    public CountryService(CountryDao countryDao) {
        super(countryDao, logger, Country.class);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public Country patchById(Integer id, Map<String, Object> fields) throws org.zionusa.base.util.exceptions.NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public Country save(Country t) {
        return super.save(t);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<Country> saveMultiple(List<Country> tList) {
        return super.saveMultiple(tList);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void delete(Integer id) throws org.zionusa.base.util.exceptions.NotFoundException {
        super.delete(id);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public void deleteMultiple(String ids) throws org.zionusa.base.util.exceptions.NotFoundException {
        super.deleteMultiple(ids);
    }

}
