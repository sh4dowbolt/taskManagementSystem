package com.suraev.TaskManagementSystem.util;

import com.suraev.TaskManagementSystem.domain.entity.Task;
import com.suraev.TaskManagementSystem.domain.entity.Task_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class SpecificationUtil {
    public static Specification<Task> likeAuthorId(Long id) {
        if (id == null){
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Task_.AUTHOR), id);

    }
    public static Specification<Task> likeExecutorId(Long id) {
        if (id == null){
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Task_.EXECUTOR), id);
    }

    public static Specification<Task> likePriority(String priorityParam) {
        if (!StringUtils.hasText(priorityParam)) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like
                (criteriaBuilder.upper(root.get(Task_.PRIORITY)), "%" + priorityParam.toUpperCase() + "%");
    }

    public static Specification<Task> likeTitle(String titleParam) {
        if (!StringUtils.hasText(titleParam)) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like
                (criteriaBuilder.upper(root.get(Task_.TITLE)), "%" + titleParam.toUpperCase() + "%");
    }
    public static Specification<Task> likeStatus(String statusParam) {
        if (!StringUtils.hasText(statusParam)) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like
                (criteriaBuilder.upper(root.get(Task_.STATUS)), "%" + statusParam.toUpperCase() + "%");
    }
    public static Specification<Task> likeDescription(String descriptionParam) {
        if (!StringUtils.hasText(descriptionParam)) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like
                (criteriaBuilder.upper(root.get(Task_.DESCRIPTION)), "%" + descriptionParam.toUpperCase() + "%");
    }




}

