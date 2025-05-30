package org.zionusa.base.service;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.Util;
import org.zionusa.base.util.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseViewService<T extends BaseDomain<K>, K> {

    private final JpaRepository<T, Integer> dao;
    private final Logger logger;

    public BaseViewService(JpaRepository<T, Integer> dao, Logger logger) {
        this.dao = dao;
        this.logger = logger;
    }

    public List<T> getAll() {
        logger.info("Retrieving list of all items");
        return dao.findAll();
    }

    public Page<T> getAllByPage(Pageable pageable) {
        logger.info("Retrieving list of pageable items");
        return dao.findAll(pageable);
    }

    public List<Map<String, Object>> getAllDisplay(List<String> columns) {
        logger.info("Retrieving list of display items");
        return getAllDisplayFromList(dao.findAll(), columns);
    }

    public <D> List<Map<String, Object>> getAllDisplayFromList(List<D> items, List<String> columns) {
        List<Map<String, Object>> displayItems = new ArrayList<>();

        items.forEach(item -> {
            Map<String, Object> displayItem = getOneDisplayFromItem(item, columns);
            if (displayItem.size() > 0) displayItems.add(displayItem);
        });

        return displayItems;
    }

    public <D> Map<String, Object> getOneDisplayFromItem(D item, List<String> columns) {
        return Util.getFieldsAndValues(columns, item);
    }

    public T getById(Integer id) throws NotFoundException {
        logger.info("Retrieving one with id of: {}", id);
        Optional<T> optional = dao.findById(id);

        if (!optional.isPresent())
            throw new NotFoundException(getClass().getSimpleName() + " could not be find the entity in the system");

        return optional.get();
    }

// TODO: Enable later
//    public Map<String, Object> getDisplayById(Integer id, List<String> columns) throws NotFoundException {
//        logger.info("Retrieving one display with id of: {}", id);
//        Optional<T> optional = dao.findById(id);
//
//        if (!optional.isPresent()) {
//            throw new NotFoundException(getClass().getSimpleName() + " could not be find the entity in the system");
//        }
//
//        return getOneDisplayFromItem(optional.get(), columns);
//    }
}
