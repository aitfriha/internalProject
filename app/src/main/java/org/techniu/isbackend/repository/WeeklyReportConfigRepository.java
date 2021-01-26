package org.techniu.isbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.techniu.isbackend.entity.WeeklyReportConfig;

@Repository
public interface WeeklyReportConfigRepository extends MongoRepository<WeeklyReportConfig, String> {
   WeeklyReportConfig findConfigurationByRemovable(boolean removable);
}
