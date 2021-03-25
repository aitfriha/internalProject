package org.techniu.isbackend.service;

import org.techniu.isbackend.dto.model.CivilityTitleDto;
import org.techniu.isbackend.entity.ClassType;
import org.techniu.isbackend.entity.Log;
import org.techniu.isbackend.entity.LogType;

import java.util.List;

public interface LogService {
    Log saveLog(Log log);
    /**
     * all LogDto
     *
     * @return List LogDto
     */
    List<Log> getAll();

    void  addLog(LogType logType, ClassType classType);
}
