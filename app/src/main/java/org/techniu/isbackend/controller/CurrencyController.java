package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.CurrencyAddrequest;
import org.techniu.isbackend.controller.request.CurrencyUpdaterequest;
import org.techniu.isbackend.dto.mapper.CurrencyMapper;
import org.techniu.isbackend.dto.model.CurrencyDto;
import org.techniu.isbackend.entity.Currency;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.CurrencyService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.Currency;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin("*")
public class CurrencyController {

    private CurrencyService currencyService;
    private final MapValidationErrorService mapValidationErrorService;
    private final CurrencyMapper currencyMapper = Mappers.getMapper(CurrencyMapper.class);


    public CurrencyController(CurrencyService currencyService, MapValidationErrorService mapValidationErrorService) {
        this.currencyService = currencyService;
        this.mapValidationErrorService = mapValidationErrorService;
    }


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid CurrencyAddrequest currencyAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save Contract Status
        //System.out.println(currencyAddrequest);
        List<CurrencyDto> currencyList = currencyService.getAllCurrency();
        for (CurrencyDto currencyDto : currencyList) {
            if (currencyDto.getTypeOfCurrency().get_id().equals(currencyAddrequest.getTypeOfCurrency().get_id())
                    && currencyDto.getYear() == currencyAddrequest.getYear()
                    && currencyDto.getMonth() == currencyAddrequest.getMonth()) return null;
        }

        currencyService.saveCurrency(currencyMapper.addRequestToDto(currencyAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Currency, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<CurrencyDto> getAllContract() {
        return currencyService.getAllCurrency();

    }

    @RequestMapping(method = RequestMethod.GET, value = "/Filtered")
    public List<CurrencyDto> getFilteredCurrency() {
        return currencyService.getFilteredCurrency();

    }

    @PostMapping("/row/{Id}")
    public Currency getCurrencyById(@PathVariable String Id) {
        return currencyService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<CurrencyDto> deleteCurrencyById(@PathVariable String Id) {
        return currencyService.remove(Id);

    }

    @PostMapping("/update")
    public List<CurrencyDto> update(@RequestBody @Valid CurrencyUpdaterequest currencyUpdaterequest) {
        // Save Contract Status
        String Id = currencyUpdaterequest.getCurrencyId();
        currencyService.updateCurrency(currencyMapper.updateRequestToDto(currencyUpdaterequest), Id);
        return currencyService.getAllCurrency();
    }

    @PostMapping("/lastDataByCurrencyType/{currencyTypeId}")
    public CurrencyDto getLastDataByCurrencyType(@PathVariable String currencyTypeId) {
        return currencyService.getLastDataByCurrencyType(currencyTypeId);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/lastDataAssociatedWithCurrencyTypes")
    public List<CurrencyDto> getLastDataByCurrencyType() {
        return currencyService.getLastDataAssociatedWithCurrencyTypes();
    }

}
