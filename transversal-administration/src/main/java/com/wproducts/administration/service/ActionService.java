package com.wproducts.administration.service;


import com.wproducts.administration.dto.model.ActionDto;

import java.util.List;

public interface ActionService {
    /**
     * Register a new Action
     *
     * @param actionDto - actionDto
     */
    void save(ActionDto actionDto);

    /**
     * Update Action
     *
     * @param actionDto - actionDto
     */
    void updateAction(ActionDto actionDto);

    /**
     * delete Action
     *
     * @param id - id
     */
    void removeAction(String id);

    /**
     * all ActionsDto
     *
     * @return List ActionsDto
     */
    List<ActionDto> getAllActions();

    /**
     * one ActionDto
     *
     * @param id - id
     * @return ActionDto
     */
    ActionDto getOneAction(String id);
}
