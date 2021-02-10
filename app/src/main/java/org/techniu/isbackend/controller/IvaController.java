package org.techniu.isbackend.controller;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.techniu.isbackend.Response;
import org.techniu.isbackend.controller.request.IvaAddrequest;
import org.techniu.isbackend.controller.request.IvaUpdaterequest;
import org.techniu.isbackend.dto.mapper.IvaMapper;
import org.techniu.isbackend.dto.model.IvaDto;
import org.techniu.isbackend.entity.Iva;
import org.techniu.isbackend.entity.StateCountry;
import org.techniu.isbackend.exception.validation.MapValidationErrorService;
import org.techniu.isbackend.repository.StateCountryRepository;
import org.techniu.isbackend.service.IvaService;

import javax.validation.Valid;
import java.util.List;

import static org.techniu.isbackend.exception.EntityType.Iva;
import static org.techniu.isbackend.exception.ExceptionType.ADDED;
import static org.techniu.isbackend.exception.MainException.getMessageTemplate;

@RestController
@RequestMapping("/api/iva")
@CrossOrigin("*")
public class IvaController {

    private IvaService ivaService;
    private final MapValidationErrorService mapValidationErrorService;
    private final IvaMapper ivaMapper = Mappers.getMapper(IvaMapper.class);
    private StateCountryRepository stateCountryRepository;


    public IvaController(IvaService ivaService,StateCountryRepository stateCountryRepository,
                         MapValidationErrorService mapValidationErrorService) {
        this.ivaService = ivaService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.stateCountryRepository = stateCountryRepository;
    }


    @PostMapping("/add")
    public ResponseEntity add(@RequestBody @Valid IvaAddrequest ivaAddrequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return mapValidationErrorService.mapValidationService(bindingResult);
        // Save  Iva
        System.out.println(ivaAddrequest);

        StateCountry stateCountry = stateCountryRepository.findStateCountryBy_id(ivaAddrequest.getStateCountry().get_id());

        ivaAddrequest.setStateCountry(stateCountry);
        System.out.println(ivaAddrequest);

        ivaService.saveIva(ivaMapper.addRequestToDto(ivaAddrequest));
        return new ResponseEntity<Response>(Response.ok().setPayload(getMessageTemplate(Iva, ADDED)), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<IvaDto> getAllContractSt() {
        return ivaService.getAllIva();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/countries")
    public List<String> getIvaCountries() {
        return ivaService.getIvaCountries();
    }

    @PostMapping("/states/{CountryName}")
    public List<IvaDto> getIvaStates(@PathVariable String CountryName) {
        System.out.println("Country : " + CountryName);
        return ivaService.getIvaStates(CountryName);
    }

    @PostMapping("/row/{Id}")
    public Iva getIvaById(@PathVariable String Id) {
        return ivaService.getById(Id);
    }

    @PostMapping("/delete/{Id}")
    public List<IvaDto> deleteIvaById(@PathVariable String Id) {
        System.out.println("test delete :" +Id);
        return ivaService.remove(Id);

    }

    @PostMapping("/update")
    public List<IvaDto> update(@RequestBody @Valid IvaUpdaterequest ivaUpdaterequest) {
        // Save Contract Status
        String Id = ivaUpdaterequest.getIvaId();
        ivaService.updateIva(ivaMapper.updateRequestToDto(ivaUpdaterequest), Id);
        return ivaService.getAllIva();
    }

}
