package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LevelService {
    ServerResponse<Level> findLevelOfOptionByName(String schoolName, String departmentName,
                                                  String optionName, String levelName)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException;
    ServerResponse<List<Level>> findAllLevel();
    ServerResponse<Page<Level>> findAllLevel(Pageable pageable);
    ServerResponse<Page<Level>> findAllLevel(String keyword, Pageable pageable);
    ServerResponse<Page<Level>> findAllLevelOfOption(String schoolName, String departmentName,
                                                     String optionName, Pageable pageable)
            throws OptionNotFoundException;
    ServerResponse<Page<Level>> findAllLevelOfOption(String schoolName, String departmentName,
                                                     String optionName, String keyword,
                                                     Pageable pageable)
            throws OptionNotFoundException;
    ServerResponse<List<Level>> findAllLevelOfOption(String schoolName, String departmentName,
                                                     String optionName)
            throws OptionNotFoundException;
    Level saveLevel(Level level);
    ServerResponse<Level> saveLevel(String name, String acronym, String optionName,
                                    String emailClassPerfect, String departmentName,
                                    String schoolName)
            throws DuplicateLevelInOptionException, OptionNotFoundException;
    ServerResponse<Level> updateLevel(String name, String acronym, String optionName,
                                    String emailClassPerfect, String departmentName,
                                      String schoolName)
            throws LevelNotFoundException, OptionNotFoundException;
    ServerResponse<Level> updateLevelName(String levelId, String levelName) throws LevelNotFoundException;
    ServerResponse<Level> deleteLevel(String schoolName, String departmentName, String optionName,
                                      String levelName)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException, LevelNotFoundException;
}
