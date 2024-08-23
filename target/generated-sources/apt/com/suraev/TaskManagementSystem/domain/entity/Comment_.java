package com.suraev.TaskManagementSystem.domain.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Comment.class)
public abstract class Comment_ {

	public static volatile SingularAttribute<Comment, Task> task;
	public static volatile SingularAttribute<Comment, String> description;
	public static volatile SingularAttribute<Comment, Long> id;
	public static volatile SingularAttribute<Comment, Long> authorId;

	public static final String TASK = "task";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String AUTHOR_ID = "authorId";

}

