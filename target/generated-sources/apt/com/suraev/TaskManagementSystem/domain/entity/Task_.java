package com.suraev.TaskManagementSystem.domain.entity;

import com.suraev.TaskManagementSystem.domain.entity.enums.Priority;
import com.suraev.TaskManagementSystem.domain.entity.enums.Status;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Task.class)
public abstract class Task_ {

	public static volatile ListAttribute<Task, Comment> commentList;
	public static volatile SingularAttribute<Task, Long> author;
	public static volatile SingularAttribute<Task, Long> executor;
	public static volatile SingularAttribute<Task, String> description;
	public static volatile SingularAttribute<Task, Long> id;
	public static volatile SingularAttribute<Task, String> title;
	public static volatile SingularAttribute<Task, Priority> priority;
	public static volatile SingularAttribute<Task, Status> status;

	public static final String COMMENT_LIST = "commentList";
	public static final String AUTHOR = "author";
	public static final String EXECUTOR = "executor";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String PRIORITY = "priority";
	public static final String STATUS = "status";

}

