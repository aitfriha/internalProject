package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.TypeOfCurrencyAddrequest;
import org.techniu.isbackend.controller.request.TypeOfCurrencyUpdaterequest;
import org.techniu.isbackend.dto.mapper.TypeOfCurrencyMapper;
import org.techniu.isbackend.dto.model.TypeOfCurrencyDto;
import org.techniu.isbackend.entity.TypeOfCurrency;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.service.LogService;
import org.techniu.isbackend.service.TypeOfCurrencyService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.TypeOfCurrency;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/typeOfCurrency")
@CrossOrigin("*")
public class TypeOfCurrencyController {
    private TypeOfCurrencyService typeOfCurrencyService;
    private LogService logService;
    private final MapValidationErrorService mapValidationErrorService;
    private final TypeOfCurrencyMapper typeOfCurrencyMapper = Mappers.getMapper(TypeOfCurrencyMapper.class);


    public TypeOfCurrencyController(TypeOfCurrencyService typeOfCurrencyService, LogService logService, MapValidationErrorService mapValidationErrorService) {
        this.typeOfCurrencyService = typeOfCurrencyService;
        this.logService = logService;
        this.mapValidationErrorService = mapValidationErrorService;
    }


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid TypeOfCurrencyAddrequest typeOfCurrencyAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save Type Of Currency
        //System.out.println(typeOfCurrencyAddrequest);
        List <TypeOfCurrencyDto> currencyList = typeOfCurrencyService.getAllTypeOfCurrency();
        for (TypeOfCurrencyDto currencyDto : currencyList) {
            if (currencyDto.getCurrencyCode().equals(typeOfCurrencyAddrequest.getCurrencyCode())
                    || currencyDto.getCurrencyName().equals(typeOfCurrencyAddrequest.getCurrencyName())) return null;
        }
        typeOfCurrencyService.saveTypeOfCurrency(typeOfCurrencyMapper.addRequestToDto(typeOfCurrencyAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(TypeOfCurrency, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<TypeOfCurrencyDto> getAllTypeOfCurrencies() {
        return typeOfCurrencyService.getAllTypeOfCurrency();

    }

    @PostMapping("/row/{Id}")
    public TypeOfCurrency getTypeOfCurrencyById(@PathVariable String Id) {
        return typeOfCurrencyService.getById(Id);

    }

    @PostMapping("/delete/{Id}")
    public List<TypeOfCurrencyDto> deleteTypeOfCurrencyById(@PathVariable String Id) {
        System.out.println("test delete :" +Id);
        return typeOfCurrencyService.remove(Id);

    }

    @PostMapping("/update")
    public List<TypeOfCurrencyDto> update(@RequestBody @Valid TypeOfCurrencyUpdaterequest typeOfCurrencyUpdaterequest) {
        // Save Contract Status
        String Id = typeOfCurrencyUpdaterequest.getTypeOfCurrencyId();
       // System.out.println(typeOfCurrencyUpdaterequest + "" + Id);
        typeOfCurrencyService.updateTypeOfCurrency(typeOfCurrencyMapper.updateRequestToDto(typeOfCurrencyUpdaterequest), Id);

        return typeOfCurrencyService.getAllTypeOfCurrency();
    }

}
