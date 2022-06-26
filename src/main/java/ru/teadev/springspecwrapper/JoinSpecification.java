package ru.teadev.springspecwrapper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

public interface JoinSpecification<F, J> {
    Predicate toPredicate(Join<F, J> join, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);

}
