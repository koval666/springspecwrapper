package ru.teadev.springspecwrapper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

public interface JoinSpecification<F, J> {
    Predicate toPredicate(From<F, J> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);

}
