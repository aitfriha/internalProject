package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.IvaDto;
import org.techniu.isbackend.entity.Iva;

import java.util.List;

public interface IvaService {
    void saveIva(IvaDto ivaDto);

    List<IvaDto> getAllIva();

    List<String> getIvaCountries();

    List<IvaDto> getIvaStates(String CountryName);

    Iva getById(String id);

    List<IvaDto> updateIva(IvaDto ivaDto, String id);

    List<IvaDto> remove(String id);
}
