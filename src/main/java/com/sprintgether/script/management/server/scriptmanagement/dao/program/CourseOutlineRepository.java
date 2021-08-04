package com.sprintgether.script.management.server.scriptmanagement.dao.program;

import com.sprintgether.script.management.server.scriptmanagement.model.program.CourseOutline;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseOutlineRepository  extends MongoRepository<CourseOutline, String> {
}
