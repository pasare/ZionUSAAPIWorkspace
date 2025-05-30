package org.zionusa.base.service;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.zionusa.base.dao.BaseDao;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.util.Util;
import org.zionusa.base.util.exceptions.NotFoundException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseService<T extends BaseDomain<K>, K> {

    private final BaseDao<T, K> dao;
    private final Logger logger;
    private final Class<T> serviceClass;

    public BaseService(BaseDao<T, K> dao, Logger logger, Class<T> serviceClass) {
        this.dao = dao;
        this.logger = logger;
        this.serviceClass = serviceClass;
    }

    public List<T> getAll(Boolean archived) {
        if (Boolean.TRUE.equals(archived)) {
            return getAllByArchivedIsTrue();
        } else if (Boolean.FALSE.equals(archived)) {
            return getAllByArchivedIsFalse();
        }
        logger.info("Retrieving list of items");
        return dao.findAll();
    }

    public List<T> getAllByArchivedIsFalse() {
        logger.info("Retrieving list of all not archived items");
        return dao.getAllByArchivedIsFalse();
    }

    public List<T> getAllByArchivedIsTrue() {
        logger.info("Retrieving list of all archived items");
        return dao.getAllByArchivedIsTrue();
    }

    public Page<T> getAllByPage(Pageable pageable) {
        logger.info("Retrieving list of pageable items");
        return dao.findAll(pageable);
    }

    /**
     * Display items default to archived = false
     */
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        List<T> items;
        if (Boolean.TRUE.equals(archived)) {
            items = getAllByArchivedIsTrue();
        } else {
            items = getAllByArchivedIsFalse();
        }
        return getAllDisplayFromList(items, columns);
    }

    public <D> List<Map<String, Object>> getAllDisplayFromList(List<D> items, List<String> columns) {
        List<Map<String, Object>> displayItems = new ArrayList<>();

        items.forEach(item -> {
            Map<String, Object> displayItem = getOneDisplayFromItem(item, columns);
            if (!displayItem.isEmpty()) displayItems.add(displayItem);
        });

        return displayItems;
    }

    public <D> Map<String, Object> getOneDisplayFromItem(D item, List<String> columns) {
        return Util.getFieldsAndValues(columns, item);
    }

    public T getById(K id) throws NotFoundException {
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

    @Transactional
    public T patchById(K id, Map<String, Object> fields) throws NotFoundException {
        logger.info("Patching one with id of: {}", id);

        T item = getById(id);

        fields.forEach((key, value) -> {
            if (key.equals("id")) {
                logger.warn("- Cannot patch the id column");
            } else {
                // Use reflection to get field k on manager and set it to value v
                Field field = ReflectionUtils.findField(serviceClass, key);
                if (field == null) {
                    logger.warn("- Field '{}' was not found", key);
                } else {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, item, value);
                }
            }
        });

        return dao.save(item);
    }

    public T save(T item) {
        logger.info("Saving: {}", item);
        return dao.save(item);
    }

    public List<T> saveMultiple(List<T> tList) {
        logger.info("Saving multiple");
        return dao.saveAll(tList);
    }

    public void delete(K id) throws NotFoundException {
        logger.info("Deleting one id: {}", id);
        Optional<T> itemOptional = dao.findById(id);
        itemOptional.ifPresent(dao::delete);
    }

    @Transactional
    public void deleteMultiple(String ids) throws NotFoundException {
        logger.info("Deleting multiple: {}", ids);
        if (ids != null && !ids.isEmpty()) {
            String[] parsedIds = ids.split(",");

            for (String stringId : parsedIds) {
                // Try an integer Id, then try a stringId
                try {
                    deleteInteger(Integer.parseInt(stringId));
                } catch (NumberFormatException e) {
                    deleteString(stringId);
                }
            }
        }
    }

    private void deleteInteger(Integer id) throws NotFoundException {
        logger.info("Deleting one id: {}", id);
        Optional<T> itemOptional = dao.findById((K) id);
        itemOptional.ifPresent(dao::delete);
    }

    private void deleteString(String id) throws NotFoundException {
        logger.info("Deleting one id: {}", id);
        Optional<T> itemOptional = dao.findById((K) id);
        itemOptional.ifPresent(dao::delete);
    }
}
