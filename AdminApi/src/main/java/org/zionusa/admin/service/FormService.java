package org.zionusa.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zionusa.admin.dao.FormDao;
import org.zionusa.admin.dao.PermissionDao;
import org.zionusa.admin.domain.Form;
import org.zionusa.admin.domain.Permission;
import org.zionusa.base.service.BaseService;
import org.zionusa.base.util.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class FormService extends BaseService<Form, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(FormService.class);

    private final FormDao formDao;
    private final PermissionService permissionService;
    private final PermissionDao permissionDao;

    @Autowired
    public FormService(FormDao formDao, PermissionService permissionService, PermissionDao permissionDao) {
        super(formDao, logger, Form.class);
        this.formDao = formDao;
        this.permissionService = permissionService;
        this.permissionDao = permissionDao;
    }

    @Override
    public List<Form> getAll(Boolean archived) {
        List<Form> forms = super.getAll(archived);

        for (Form form : forms) {
            Permission permission = permissionDao.findPermissionByReferenceAndReferenceId(Permission.Reference.FORM, form.getId());
            form.setPermission(permission);
        }

        return permissionService.filterFormListByPermission(forms);
    }

    @Override
    public Form getById(Integer id) throws NotFoundException {
        Optional<Form> formOptional = formDao.findById(id);

        if (!formOptional.isPresent())
            throw new NotFoundException("The Form could not be found in the system");

        //get the form permission
        Form form = formOptional.get();
        Permission permission = permissionDao.findPermissionByReferenceAndReferenceId(Permission.Reference.FORM, id);

        form.setPermission(permission);
        if (permissionService.checkPermission(formOptional.get().getPermission()))
            return form;

        return null;
    }

    @Override
    public Form save(Form form) {
        Form returnedForm = formDao.save(form);

        if (form.getPermission() != null) {
            Permission permission = form.getPermission();
            permission.setReferenceId(returnedForm.getId());
            permission.setReference(Permission.Reference.FORM);
            permissionDao.save(permission);
        }

        return returnedForm;
    }

    @Override
    public void delete(Integer formId) throws NotFoundException {
        Optional<Form> formOptional = formDao.findById(formId);

        if (!formOptional.isPresent())
            throw new NotFoundException("Cannot delete a form that does not exist");

        formDao.delete(formOptional.get());
    }
}
