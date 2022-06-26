package ru.teadev.springspecwrapper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;

public interface ExpressionSupplier<F> {
    Expression get(From<?, F> from, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);
}
