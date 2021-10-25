package com.sprintgether.script.management.server.scriptmanagement.service.school;

import com.sprintgether.script.management.server.scriptmanagement.commonused.ResponseCode;
import com.sprintgether.script.management.server.scriptmanagement.commonused.ServerResponse;
import com.sprintgether.script.management.server.scriptmanagement.dao.school.LevelRepository;
import com.sprintgether.script.management.server.scriptmanagement.exception.school.*;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Department;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Level;
import com.sprintgether.script.management.server.scriptmanagement.model.school.Option;
import com.sprintgether.script.management.server.scriptmanagement.model.school.School;
import com.sprintgether.script.management.server.scriptmanagement.model.user.Staff;
import com.sprintgether.script.management.server.scriptmanagement.service.user.StaffService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LevelServiceImpl implements LevelService {

    LevelRepository levelRepository;
    OptionService optionService;
    StaffService staffService;

    public LevelServiceImpl(LevelRepository levelRepository, OptionService optionService,
                            StaffService staffService) {
        this.levelRepository = levelRepository;
        this.optionService = optionService;
        this.staffService = staffService;
    }

    @Override
    public ServerResponse<Level> findLevelOfOptionById(String levelId) {
        levelId = levelId.trim();
        ServerResponse<Level> srLevel = new ServerResponse<>();
        Optional<Level> optionalLevel = levelRepository.findById(levelId);
        if(!optionalLevel.isPresent()){
            srLevel.setErrorMessage("The level name specified does not match any level " +
                    "in the precised option");
            srLevel.setResponseCode(ResponseCode.LEVEL_NOT_FOUND);
        }
        else {
            srLevel.setResponseCode(ResponseCode.LEVEL_FOUND);
            srLevel.setErrorMessage("The level has been successfully found");
            srLevel.setAssociatedObject(optionalLevel.get());
        }
        return srLevel;
    }

    @Override
    public ServerResponse<Level> findLevelOfOptionByName(String schoolName, String departmentName,
                                                         String optionName, String levelName)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();

        ServerResponse<Level> srLevel = new ServerResponse<>();
        ServerResponse<Option> srOption = optionService.findOptionOfDepartmentByName(
                schoolName, departmentName, optionName);
        if(srOption.getResponseCode() != ResponseCode.OPTION_FOUND){
            throw new OptionNotFoundException("The option name specified does not match any " +
                    "option in the department of school specified");
        }
        Option concernedOption = srOption.getAssociatedObject();

        Optional<Level> optionalLevel = levelRepository.findByOwnerOptionAndName(concernedOption,
                levelName);
        if(!optionalLevel.isPresent()){
            srLevel.setErrorMessage("The level name specified does not match any level " +
                    "in the precised option");
            srLevel.setResponseCode(ResponseCode.LEVEL_NOT_FOUND);
        }
        else {
            srLevel.setResponseCode(ResponseCode.LEVEL_FOUND);
            srLevel.setErrorMessage("The level has been successfully found");
            srLevel.setAssociatedObject(optionalLevel.get());
        }

        return srLevel;
    }

    @Override
    public ServerResponse<List<Level>> findAllLevel() {
        ServerResponse<List<Level>> srLevelList = new ServerResponse<>();
        List<Level> listOfLevel = levelRepository.findAll(Sort.by(new Sort.Order(Sort.Direction.ASC, "name")));
        srLevelList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srLevelList.setErrorMessage("The level list has been successfully made");
        srLevelList.setAssociatedObject(listOfLevel);
        return srLevelList;
    }

    @Override
    public ServerResponse<Page<Level>> findAllLevel(Pageable pageable) {
        ServerResponse<Page<Level>> srLevelPage = new ServerResponse<>();
        Page<Level> pageOfLevel = levelRepository.findAll(pageable);
        srLevelPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srLevelPage.setErrorMessage("The level page has been successfully made");
        srLevelPage.setAssociatedObject(pageOfLevel);
        return srLevelPage;
    }

    @Override
    public ServerResponse<Page<Level>> findAllLevel(String keyword, Pageable pageable) {
        keyword = keyword.toLowerCase().trim();
        ServerResponse<Page<Level>> srLevelPage = new ServerResponse<>();
        Page<Level> pageOfLevel = levelRepository.findAllByNameContaining(keyword, pageable);
        srLevelPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srLevelPage.setErrorMessage("The level page has been successfully made");
        srLevelPage.setAssociatedObject(pageOfLevel);
        return srLevelPage;
    }

    public Optional<Option> findConcernedOption(String optionId,
                                                String schoolName,
                                                String departmentName,
                                                String optionName){
        Option concernedOption = null;
        ServerResponse<Option> srOptionFoundById = optionService.findOptionOfDepartmentById(
                optionId);
        if(srOptionFoundById.getResponseCode() != ResponseCode.OPTION_FOUND) {
            ServerResponse<Option> srOption = null;
            try {
                srOption = optionService.findOptionOfDepartmentByName(schoolName,
                        departmentName, optionName);
                //There, we are sure that the option is available for searching all the level inside
                concernedOption = srOption.getAssociatedObject();

            } catch (SchoolNotFoundException e) {
                e.printStackTrace();
            } catch (DepartmentNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            concernedOption = srOptionFoundById.getAssociatedObject();
        }
        return Optional.ofNullable(concernedOption);
    }

    @Override
    public ServerResponse<Page<Level>> findAllLevelOfOption(String optionId,
                                                            String schoolName,
                                                            String departmentName,
                                                            String optionName,
                                                            Pageable pageable)
            throws OptionNotFoundException {
        optionId = optionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();

        ServerResponse<Page<Level>> srLevelPage = new ServerResponse<>();

        Option concernedOption = null;
        Optional<Option> optionalOption = this.findConcernedOption(optionId, schoolName, departmentName, optionName);
        if(!optionalOption.isPresent()){
            throw new OptionNotFoundException("The option specified does not found in the system");
        }
        concernedOption = optionalOption.get();

        Page<Level> pageOfLevel = levelRepository.findAllByOwnerOption(concernedOption, pageable);
        srLevelPage.setErrorMessage("The level page of option has been successfully made");
        srLevelPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srLevelPage.setAssociatedObject(pageOfLevel);
        return srLevelPage;
    }


    @Override
    public ServerResponse<Page<Level>> findAllLevelOfOption(String optionId,
                                                            String schoolName,
                                                            String departmentName,
                                                            String optionName,
                                                            String keyword,
                                                            Pageable pageable)
            throws OptionNotFoundException {
        optionId = optionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        keyword = keyword.toLowerCase().trim();

        ServerResponse<Page<Level>> srLevelPage = new ServerResponse<>();

        Option concernedOption = null;
        Optional<Option> optionalOption = this.findConcernedOption(optionId, schoolName, departmentName, optionName);
        if(!optionalOption.isPresent()){
            throw new OptionNotFoundException("The option specified does not found in the system");
        }
        concernedOption = optionalOption.get();

        Page<Level> pageOfLevel = levelRepository.findAllByOwnerOptionAndNameContaining(
                concernedOption, keyword, pageable);
        srLevelPage.setErrorMessage("The level page of option has been successfully made");
        srLevelPage.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srLevelPage.setAssociatedObject(pageOfLevel);
        return srLevelPage;
    }



    @Override
    public ServerResponse<List<Level>> findAllLevelOfOption(String optionId,
                                                            String schoolName,
                                                            String departmentName,
                                                            String optionName)
            throws OptionNotFoundException {
        optionId = optionId.trim();
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();

        ServerResponse<List<Level>> srLevelList = new ServerResponse<>();

        Option concernedOption = null;
        Optional<Option> optionalOption = this.findConcernedOption(optionId, schoolName, departmentName, optionName);
        if(!optionalOption.isPresent()){
            throw new OptionNotFoundException("The option specified does not found in the system");
        }
        concernedOption = optionalOption.get();

        List<Level> listOfLevel = levelRepository.findAllByOwnerOptionOrderByName(concernedOption);
        srLevelList.setErrorMessage("The level list of option has been successfully made");
        srLevelList.setResponseCode(ResponseCode.NORMAL_RESPONSE);
        srLevelList.setAssociatedObject(listOfLevel);
        return srLevelList;
    }


    @Override
    public Level saveLevel(Level level) {
        return levelRepository.save(level);
    }

    @Override
    public ServerResponse<Level> saveLevel(String name,
                                           String acronym,
                                           String optionName,
                                           String emailClassPerfect,
                                           String optionId,
                                           String departmentName,
                                           String schoolName)
            throws DuplicateLevelInOptionException, OptionNotFoundException {

        name = name.toLowerCase().trim();
        acronym = acronym.trim();
        optionName = optionName.toLowerCase().trim();
        emailClassPerfect = emailClassPerfect.trim();
        optionId = optionId.trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();

        ServerResponse<Level> srLevel = new ServerResponse<>(ResponseCode.LEVEL_NOT_CREATED);

        try{
            Option concernedOption = null;

            Optional<Option> optionalOption = this.findConcernedOption(optionId, schoolName, departmentName,
                    optionName);
            if(!optionalOption.isPresent()){
                throw new OptionNotFoundException("The specified option does not found in the system");
            }
            concernedOption = optionalOption.get();

            ServerResponse<Level> srLevel1 = null;

            srLevel1 = this.findLevelOfOptionByName(schoolName, departmentName,
                    optionName, name);

            if (srLevel1.getResponseCode() == ResponseCode.LEVEL_FOUND) {
                throw new DuplicateLevelInOptionException("The level that we are trying to save is already belonging to the option specified");
            }
            //We search the classperfect staff if it's specified
            ServerResponse<Staff> srClassperfect = staffService.findStaffByEmail(emailClassPerfect);
            Staff classPerfect = srClassperfect.getAssociatedObject();
            //We can now save the level without any problem
            Level level = new Level();
            level.setAcronym(acronym);
            level.setName(name);
            level.setOwnerOption(concernedOption);
            level.setClassPrefect(classPerfect);

            Level levelSaved = levelRepository.save(level);

            srLevel.setErrorMessage("The level specified has been successfully created");
            srLevel.setResponseCode(ResponseCode.LEVEL_CREATED);
            srLevel.setAssociatedObject(levelSaved);

        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srLevel.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
            srLevel.setErrorMessage("The school name specified does not match any school in the system");
            srLevel.setMoreDetails(e.getMessage());
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srLevel.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
            srLevel.setErrorMessage("The department name specified does not match any department in the school specified");
            srLevel.setMoreDetails(e.getMessage());
        }

        return srLevel;

    }


    @Override
    public ServerResponse<Level> updateLevel(String levelId, String name, String acronym,
                                             String optionName, String emailClassPerfect,
                                             String departmentName, String schoolName)
            throws LevelNotFoundException, DuplicateLevelInOptionException {
        levelId = levelId.trim();
        name = name.toLowerCase().trim();
        acronym = acronym.trim();
        optionName = optionName.toLowerCase().trim();
        emailClassPerfect = emailClassPerfect.trim();
        departmentName = departmentName.toLowerCase().trim();
        schoolName = schoolName.toLowerCase().trim();

        ServerResponse<Level> srLevel = new ServerResponse<>(ResponseCode.LEVEL_NOT_UPDATED);
        srLevel.setResponseCode(ResponseCode.LEVEL_NOT_UPDATED);

        Optional<Level> optionalLevel  = levelRepository.findById(levelId);
        if(!optionalLevel.isPresent()){
            throw new  LevelNotFoundException("The level specified by id does not found in " +
                    "the system");
        }
        else{
            Level levelToUpdated1 = optionalLevel.get();
            //We search the classperfect staff if it's specified
            ServerResponse<Staff> srClassperfect = staffService.findStaffByEmail(emailClassPerfect);
            //The staff can be null
            Staff classPerfect = srClassperfect.getAssociatedObject();
            levelToUpdated1.setAcronym(acronym);
            levelToUpdated1.setName(name);
            //level.setOwnerOption(concernedOption);
            levelToUpdated1.setClassPrefect(classPerfect);

            try {
                ServerResponse<Level> srLevel2 = this.findLevelOfOptionByName(schoolName, departmentName,
                        optionName, name);
                if(srLevel2.getResponseCode() == ResponseCode.LEVEL_FOUND){
                    Level levelToUpdated2 = srLevel2.getAssociatedObject();
                    if(!levelToUpdated1.getId().equalsIgnoreCase(levelToUpdated2.getId())){
                        throw new DuplicateLevelInOptionException("The new name of level already " +
                                "identify another level in the system");
                    }
                }
                else{
                    levelToUpdated1.setName(name);
                }

                Level levelUpdated = levelRepository.save(levelToUpdated1);

                srLevel.setErrorMessage("The level specified has been successfully updated");
                srLevel.setResponseCode(ResponseCode.LEVEL_UPDATED);
                srLevel.setAssociatedObject(levelUpdated);
            } catch (SchoolNotFoundException e) {
                //e.printStackTrace();
                srLevel.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
                srLevel.setErrorMessage("The school name specified does not match any school " +
                        "in the system");
                srLevel.setMoreDetails(e.getMessage());
            } catch (DepartmentNotFoundException e) {
                //e.printStackTrace();
                srLevel.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
                srLevel.setErrorMessage("The department name specified does not match any " +
                        "department in the school specified");
                srLevel.setMoreDetails(e.getMessage());
            } catch (OptionNotFoundException e) {
                //e.printStackTrace();
                srLevel.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
                srLevel.setErrorMessage("The option name specified does not match any " +
                        "option in the department specified");
                srLevel.setMoreDetails(e.getMessage());
            }
        }

        return srLevel;
    }

    @Override
    public ServerResponse<Level> updateLevelName(String levelId, String levelName)
            throws LevelNotFoundException, DuplicateLevelInOptionException {
        levelId = levelId.trim();
        levelName = levelName.toLowerCase().trim();
        ServerResponse<Level> srLevel = new ServerResponse<>(ResponseCode.LEVEL_NOT_UPDATED);
        Optional<Level> optionalLevel = levelRepository.findById(levelId);
        if(!optionalLevel.isPresent()){
            throw new LevelNotFoundException("The levelId specified does not match any level in " +
                    "the system");
        }
        Level levelToUpdated = optionalLevel.get();
        String optionName = levelToUpdated.getOwnerOption().getName();
        String departmentName = levelToUpdated.getOwnerOption().getOwnerDepartment().getName();
        String schoolName = levelToUpdated.getOwnerOption().getOwnerDepartment().getOwnerSchool().getName();
        try {
            ServerResponse<Level> srLevel1 = this.findLevelOfOptionByName(schoolName, departmentName,
                    optionName, levelName);
            if(srLevel1.getResponseCode() == ResponseCode.LEVEL_FOUND){
                throw new DuplicateLevelInOptionException("The new level name will match with " +
                        "another level in the option");
            }
            levelToUpdated.setName(levelName);
            Level levelUpdated = this.saveLevel(levelToUpdated);

            srLevel.setErrorMessage("The level has been successfully updated");
            srLevel.setResponseCode(ResponseCode.LEVEL_UPDATED);
            srLevel.setAssociatedObject(levelUpdated);
        } catch (SchoolNotFoundException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("There is problem in the school associated");
            srLevel.setMoreDetails(e.getMessage());
            srLevel.setResponseCode(ResponseCode.EXCEPTION_SCHOOL_FOUND);
        } catch (DepartmentNotFoundException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("There is problem in the department associated");
            srLevel.setMoreDetails(e.getMessage());
            srLevel.setResponseCode(ResponseCode.EXCEPTION_DEPARTMENT_FOUND);
        } catch (OptionNotFoundException e) {
            //e.printStackTrace();
            srLevel.setErrorMessage("There is problem in the option associated");
            srLevel.setMoreDetails(e.getMessage());
            srLevel.setResponseCode(ResponseCode.EXCEPTION_OPTION_FOUND);
        }
        return srLevel;
    }

    @Override
    public ServerResponse<Level> deleteLevel(String levelId, String schoolName, String departmentName,
                                             String optionName, String levelName)
            throws SchoolNotFoundException, DepartmentNotFoundException, OptionNotFoundException, LevelNotFoundException {
        schoolName = schoolName.toLowerCase().trim();
        departmentName = departmentName.toLowerCase().trim();
        optionName = optionName.toLowerCase().trim();
        levelName = levelName.toLowerCase().trim();

        return null;
    }

}
