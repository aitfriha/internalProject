package org.techniu.isbackend.service;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.techniu.isbackend.dto.mapper.CivilityTitleMapper;
import org.techniu.isbackend.dto.model.CivilityTitleDto;
import org.techniu.isbackend.entity.CivilityTitle;
import org.techniu.isbackend.entity.Contact;
import org.techniu.isbackend.exception.EntityType;
import org.techniu.isbackend.exception.ExceptionType;
import org.techniu.isbackend.exception.MainException;
import org.techniu.isbackend.repository.CivilityTitleRepository;
import org.techniu.isbackend.repository.ContactRepository;

import static org.springframework.util.StringUtils.capitalize;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.techniu.isbackend.exception.ExceptionType.*;

@Service
public class CivilityTitleServiceImpl implements CivilityTitleService{
    private CivilityTitleRepository civilityTitleRepository;
    private ContactRepository contactRepository;
    private final CivilityTitleMapper civilityTitleMapper = Mappers.getMapper(CivilityTitleMapper.class);
    CivilityTitleServiceImpl(CivilityTitleRepository civilityTitleRepository, ContactRepository contactRepository) {
        this.civilityTitleRepository = civilityTitleRepository;
        this.contactRepository = contactRepository;
    }
    @Override
    public void save(CivilityTitleDto civilityTitleDto) {
        civilityTitleDto.setName(capitalize(civilityTitleDto.getName()));
        if (civilityTitleDto.getName().contains(" ")) {
             throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
        Optional<CivilityTitle>  civilityTitle= Optional.ofNullable(civilityTitleRepository.findByName(civilityTitleDto.getName()));
        if (civilityTitle.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        Optional<CivilityTitle>  civilityTitle1= Optional.ofNullable(civilityTitleRepository.findByCode(civilityTitleDto.getCode()));
        if (civilityTitle1.isPresent()) {
            throw exception(DUPLICATE_ENTITY);
        }
        CivilityTitle civilityTitle3 = civilityTitleMapper.dtoToModel(civilityTitleDto);
        civilityTitleRepository.save(civilityTitle3);
    }

    @Override
    public void update(CivilityTitleDto civilityTitleDto) {
        // save country if note existe
        civilityTitleDto.setName(civilityTitleDto.getName().toLowerCase());
        if (civilityTitleDto.getName().contains(" ")) {
            throw exception(CODE_SHOULD_NOT_CONTAIN_SPACES);
        }
       Optional<CivilityTitle> civilityTitle1 = Optional.ofNullable(civilityTitleRepository.findBy_id(civilityTitleDto.getCivilityTitleId()));
        if (!civilityTitle1.isPresent()) {
            throw exception(ExceptionType.ENTITY_NOT_FOUND);
        }
        Optional<CivilityTitle> civilityTitle2 = Optional.ofNullable(civilityTitleRepository.findByName(civilityTitleDto.getName()));

        if (civilityTitle2.isPresent() && !(civilityTitle1.get().getName().equals(civilityTitleDto.getName())) ) {
            throw exception(DUPLICATE_ENTITY);
        }
        Optional<CivilityTitle> civilityTitle3 = Optional.ofNullable(civilityTitleRepository.findByCode(civilityTitleDto.getCode()));
        if (civilityTitle3.isPresent() && !(civilityTitle1.get().getCode().equals(civilityTitleDto.getCode())) ) {
            throw exception(DUPLICATE_ENTITY);
        }
         civilityTitleRepository.save(civilityTitleMapper.dtoToModel(civilityTitleDto));
    }

    @Override
    public List<CivilityTitleDto> getAll() {
        // Get all civilityTitles
        List<CivilityTitle> civilityTitles = civilityTitleRepository.findAll();
        // Create a list of all civilityTitle dto
        ArrayList<CivilityTitleDto> civilityTitleDtos = new ArrayList<>();

        for (CivilityTitle civilityTitle : civilityTitles) {
            List<Contact> contactList=contactRepository.findByCivilityTitle(civilityTitle);

            CivilityTitleDto civilityTitleDto = civilityTitleMapper.modelToDto(civilityTitle);
            if(contactList.size()>0){
                civilityTitleDto.setRelated(true);
            }
            else {
                civilityTitleDto.setRelated(false);
            }
            civilityTitleDtos.add(civilityTitleDto);
        }
        return civilityTitleDtos;
    }

    /**
     * delete Action
     *
     * @param id - id
     */
    @Override
    public void remove(String id) {
        Optional<CivilityTitle> action = Optional.ofNullable(civilityTitleRepository.findBy_id(id));
        // If CivilityTitle doesn't exists
        if (!action.isPresent()) {
            throw exception(ENTITY_NOT_FOUND);
        }
        civilityTitleRepository.deleteById(id);
    }


    /**
     * Returns a new RuntimeException
     *
     * @param exceptionType exceptionType
     * @param args  args
     * @return RuntimeException
     */
    private RuntimeException exception(ExceptionType exceptionType, String... args) {
        return MainException.throwException(EntityType.CivilityTitle, exceptionType, args);
    }
}
