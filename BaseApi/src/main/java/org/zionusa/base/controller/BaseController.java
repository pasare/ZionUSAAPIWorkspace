package org.zionusa.base.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.zionusa.base.domain.BaseDomain;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public class BaseController<T extends BaseDomain<K>, K> {

    private final BaseService<T, K> service;

    public BaseController(BaseService<T, K> service) {
        this.service = service;
    }

    @GetMapping()
    @ApiOperation(value = "Get a list of all items")
    public List<T> getAll(@RequestParam(value = "archived", required = false) Boolean archived) {
        return service.getAll(archived);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/pageable")
    @ApiOperation(value = "Get a list of all items by page")
    public Page<T> getAllByPage(Pageable pageable) {
        return service.getAllByPage(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/display")
    @ApiOperation(value = "Get a list of all items only with the specified columns")
    public List<Map<String, Object>> getAllDisplay(
        @RequestBody List<String> columns,
        @RequestParam(value = "archived", required = false) Boolean archived) {
        return service.getAllDisplay(columns, archived);
    }

    // Use ?archived=true or ?archived=false
    @Deprecated
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/enabled")
    public List<T> getAllByArchivedIsFalse() {
        return service.getAllByArchivedIsFalse();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get one item")
    T getById(@PathVariable K id) throws NotFoundException {
        return service.getById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/{id}")
    @ApiOperation(value = "Save partial data for one item")
    T patchById(@PathVariable K id, @RequestBody Map<String, Object> fields) throws NotFoundException {
        return service.patchById(id, fields);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    @ApiOperation(value = "Create one item")
    T save(@Valid @RequestBody T item) {
        return service.save(item);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/multiple")
    @ApiOperation(value = "Create multiple items")
    List<T> saveMultiple(@Valid @RequestBody List<T> tList) {
        return service.saveMultiple(tList);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}")
    @ApiOperation(value = "Save one item")
    T update(@PathVariable K id, @Valid @RequestBody T item) {
        return service.save(item);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete one item")
    void delete(@PathVariable K id) throws NotFoundException {
        service.delete(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/multiple")
    @ApiOperation(value = "Delete multiple items")
    void delete(@RequestParam String ids) throws NotFoundException {
        service.deleteMultiple(ids);
    }
}
