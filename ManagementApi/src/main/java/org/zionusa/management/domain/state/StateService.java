package org.zionusa.management.domain.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.zionusa.base.service.BaseService;

import java.util.List;
import java.util.Map;

@Service
public class StateService extends BaseService<State, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(StateService.class);
    private final StateViewService stateViewService;

    @Autowired
    public StateService(StateDao stateDao, StateViewService stateViewService) {
        super(stateDao, logger, State.class);
        this.stateViewService = stateViewService;
    }

    @Override
    public List<Map<String, Object>> getAllDisplay(List<String> columns, Boolean archived) {
        return stateViewService.getAllDisplay(columns);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public State patchById(Integer id, Map<String, Object> fields) throws org.zionusa.base.util.exceptions.NotFoundException {
        return super.patchById(id, fields);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public State save(State t) {
        return super.save(t);
    }

    @Override
    @PreAuthorize("hasAuthority('Admin')")
    public List<State> saveMultiple(List<State> tList) {
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
