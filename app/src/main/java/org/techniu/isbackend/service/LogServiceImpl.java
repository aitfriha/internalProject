package org.techniu.isbackend.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techniu.isbackend.dto.model.CivilityTitleDto;
import org.techniu.isbackend.entity.*;
import org.techniu.isbackend.repository.LogRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LogServiceImpl implements LogService {
    private LogRepository logRepository;
    LogServiceImpl(LogRepository logRepository){
        this.logRepository = logRepository;
    }

    @Override
    public Log saveLog(Log log) {
        return logRepository.save(log);
    }

    /**
     * all LogDto
     *
     * @return List LogDto
     */
    @Override
    public List<Log> getAll() {
            // Get all civilityTitles
            List<Log> logs = logRepository.findAll();
            return logs;
        }
    @Override
    public void  addLog(LogType logType, ClassType classType,String description) {
        Object user = SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = user.toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        String dateString = format.format(new Date());
        Log log = new Log();
        log.setUserName(username);
        log.setLogType(logType);
        log.setClassType(classType);
        log.setActionDate(dateString);
        log.setDescription(description);
        logRepository.save(log);
    }
}
