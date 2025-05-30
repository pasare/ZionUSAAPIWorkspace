package org.zionusa.management.domain.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zionusa.base.controller.BaseController;

@RestController
@RequestMapping("/countries")
public class CountryController extends BaseController<Country, Integer> {

    @Autowired
    public CountryController(CountryService countryService) {
        super(countryService);
    }

}
