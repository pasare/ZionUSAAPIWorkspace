package org.zionusa.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.admin.domain.MaterialReservation;
import org.zionusa.admin.service.MaterialReservationService;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/material-reservations")
public class MaterialReservationController extends BaseController<MaterialReservation, Integer> {

    public MaterialReservationController(MaterialReservationService materialReservationService) {
        super(materialReservationService);
    }
}
