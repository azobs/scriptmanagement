package com.sprintgether.script.management.server.scriptmanagement.service.commonused;

import com.sprintgether.script.management.server.scriptmanagement.exception.program.*;
import com.sprintgether.script.management.server.scriptmanagement.model.program.Course;

import java.util.Optional;

public interface CommonService {
    /*************************
     * This method take a string in paramater corresponding to an ID of some
     * program part (Module, Chapter, Section, SubSection or Paragraph)
     * In fact, the method return the class name identify by the ID in parameter
     * @param objectId
     * @return
     */
    Optional<String> concernedPartName(String objectId);

    /*****************************************************************************
     * If concernedPartId corresponds to a Module the method return true if the
     * specified module is a module of the courseOutline associated to the course specified
     * in parameter. otherwise it returns  false
     * if is a Chapter, it return true if the corresponding chapter belonging to one
     * module of the courseOutline asssociated to that Course in parameter
     * @param concernedPartId
     * @param course
     * @return
     */
    boolean isConcernedPartBelongingToCourse(String concernedPartId, Course course)
            throws CourseNotFoundException, ModuleNotFoundException, ChapterNotFoundException,
            SectionNotFoundException, SubSectionNotFoundException, ParagraphNotFoundException;
}
