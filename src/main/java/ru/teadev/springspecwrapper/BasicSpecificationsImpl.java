package ru.teadev.springspecwrapper;

import java.util.Collection;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.SingularAttribute;

import static org.springframework.data.jpa.domain.Specification.not;
import static org.springframework.data.jpa.domain.Specification.where;
import static org.springframework.util.CollectionUtils.isEmpty;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

public class BasicSpecificationsImpl implements BasicSpecifications {

    @Override
    public <R> Specification<R> distinct(Class<R> rootClass) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return null;
        };
    }

    @Override
    public <E, J1> Specification<E> join(JoinInfo<? super E, J1> joinInfo1) {
        return joinedAttributeSpec(
                joinInfo1,
                join -> null);
    }

    @Override
    public <E, J1, J2> Specification<E> join(JoinInfo<? super E, J1> joinInfo1,
                                             JoinInfo<J1, J2> joinInfo2) {
        return joinedAttributeSpec(
                joinInfo1,
                joinInfo2,
                join -> null);
    }

    @Override
    public <E, J1, J2, J3> Specification<E> join(JoinInfo<? super E, J1> joinInfo1,
                                                 JoinInfo<J1, J2> joinInfo2,
                                                 JoinInfo<J2, J3> joinInfo3) {
        return joinedAttributeSpec(
                joinInfo1,
                joinInfo2,
                joinInfo3,
                join -> null);
    }

    @Override
    public <E> Specification<E> attributeIsNull(SingularAttribute<? super E, ?> attribute) {
        return (root, query, criteriaBuilder) ->
                root.get(attribute).isNull();
    }

    @Override
    public <E> Specification<E> attributeIsNotNull(SingularAttribute<? super E, ?> attribute) {
        return not(attributeIsNull(attribute));
    }

    @Override
    public <E, J1> Specification<E> joinedAttributeIsNull(JoinInfo<? super E, J1> joinInfo1,
                                                          SingularAttribute<J1, ?> attribute) {
        return joinedAttributeSpec(
                joinInfo1,
                join -> join.get(attribute).isNull());
    }

    @Override
    public <E, J1> Specification<E> joinedAttributeIsNotNull(JoinInfo<? super E, J1> joinInfo1,
                                                             SingularAttribute<J1, ?> attribute) {
        return not(joinedAttributeIsNull(joinInfo1, attribute));
    }

    @Override
    public <E, J1, J2> Specification<E> joinedAttributeIsNull(JoinInfo<? super E, J1> joinInfo1,
                                                              JoinInfo<J1, J2> joinInfo2,
                                                              SingularAttribute<J2, ?> attribute) {
        return joinedAttributeSpec(
                joinInfo1,
                joinInfo2,
                join -> join.get(attribute).isNull());
    }

    @Override
    public <E, J1, J2> Specification<E> joinedAttributeIsNotNull(JoinInfo<? super E, J1> joinInfo1,
                                                                 JoinInfo<J1, J2> joinInfo2,
                                                                 SingularAttribute<J2, ?> attribute) {
        return not(joinedAttributeIsNull(
                joinInfo1,
                joinInfo2,
                attribute));
    }

    @Override
    public <E, J1, J2, J3> Specification<E> joinedAttributeIsNull(JoinInfo<? super E, J1> joinInfo1,
                                                                  JoinInfo<J1, J2> joinInfo2,
                                                                  JoinInfo<J2, J3> joinInfo3,
                                                                  SingularAttribute<J3, ?> attribute) {
        return joinedAttributeSpec(
                joinInfo1,
                joinInfo2,
                joinInfo3,
                join -> join.get(attribute).isNull());
    }

    @Override
    public <E, J1, J2, J3> Specification<E> joinedAttributeIsNotNull(JoinInfo<? super E, J1> joinInfo1,
                                                                     JoinInfo<J1, J2> joinInfo2,
                                                                     JoinInfo<J2, J3> joinInfo3,
                                                                     SingularAttribute<J3, ?> attribute) {
        return not(joinedAttributeIsNull(
                joinInfo1,
                joinInfo2,
                joinInfo3,
                attribute));
    }

    @Override
    public <E> Specification<E> attributeIn(SingularAttribute<? super E, ?> attribute,
                                            @Nullable Collection<?> value) {
        return nullIfCollectionEmpty(value,

                (root, query, criteriaBuilder) ->
                        root.get(attribute).in(value));
    }

    @Override
    public <E> Specification<E> attributeNotIn(SingularAttribute<? super E, ?> attribute,
                                               Collection<?> value) {
        return not(attributeIn(attribute, value));
    }

    @Override
    public <E, J1> Specification<E> joinedAttributeIn(JoinInfo<? super E, J1> joinInfo1,
                                                      SingularAttribute<J1, ?> attribute,
                                                      @Nullable Collection<?> value) {
        return nullIfCollectionEmpty(value,

                joinedAttributeSpec(joinInfo1,
                        join -> join.get(attribute).in(value)));
    }

    private <E, J1> Specification<E> joinedAttributeSpec(@NonNull JoinInfo<? super E, J1> joinInfo1,
                                                         @NonNull Function<Join<E, J1>, Predicate> condition) {
        return new AbstractReusableJoinSpecification<>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                Join<E, J1> join = getOrCreateJoin(root, joinInfo1);
                return condition.apply(join);
            }
        };
    }

    @Override
    public <E, J1> Specification<E> joinedAttributeNotIn(JoinInfo<? super E, J1> joinInfo1,
                                                         SingularAttribute<J1, ?> attribute,
                                                         Collection<?> value) {
        return not(joinedAttributeIn(joinInfo1, attribute, value));
    }

    @Override
    public <E, J1, J2> Specification<E> joinedAttributeIn(JoinInfo<? super E, J1> joinInfo1,
                                                          JoinInfo<J1, J2> joinInfo2,
                                                          SingularAttribute<J2, ?> attribute,
                                                          @Nullable Collection<?> value) {
        return nullIfCollectionEmpty(value,

                joinedAttributeSpec(joinInfo1, joinInfo2,
                        join -> join.get(attribute).in(value)));
    }

    //todo перейти на joinedAttributeSpec в других методах с джоинами

    private <E, J1, J2> Specification<E> joinedAttributeSpec(@NonNull JoinInfo<? super E, J1> joinInfo1,
                                                             @NonNull JoinInfo<J1, J2> joinInfo2,
                                                             @NonNull Function<Join<J1, J2>, Predicate> condition) {
        return new AbstractReusableJoinSpecification<>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                Join<E, J1> join1 = getOrCreateJoin(root, joinInfo1);
                Join<J1, J2> join2 = getOrCreateJoin(join1, joinInfo2);
                return condition.apply(join2);
            }
        };
    }

    @Override
    public <E, J1, J2> Specification<E> joinedAttributeNotIn(JoinInfo<? super E, J1> joinInfo1,
                                                             JoinInfo<J1, J2> joinInfo2,
                                                             SingularAttribute<J2, ?> attribute,
                                                             Collection<?> value) {
        return not(joinedAttributeIn(joinInfo1, joinInfo2, attribute, value));
    }

    @Override
    public <E, J1, J2, J3> Specification<E> joinedAttributeIn(JoinInfo<? super E, J1> joinInfo1,
                                                              JoinInfo<J1, J2> joinInfo2,
                                                              JoinInfo<J2, J3> joinInfo3,
                                                              SingularAttribute<J3, ?> attribute,
                                                              @Nullable Collection<?> value) {
        return nullIfCollectionEmpty(value,

                joinedAttributeSpec(joinInfo1, joinInfo2, joinInfo3,
                        join -> join.get(attribute).in(value)));
    }

    private <E, J1, J2, J3> Specification<E> joinedAttributeSpec(@NonNull JoinInfo<? super E, J1> joinInfo1,
                                                                 @NonNull JoinInfo<J1, J2> joinInfo2,
                                                                 @NonNull JoinInfo<J2, J3> joinInfo3,
                                                                 @NonNull Function<Join<J2, J3>, Predicate> condition) {
        return new AbstractReusableJoinSpecification<>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                Join<E, J1> join1 = getOrCreateJoin(root, joinInfo1);
                Join<J1, J2> join2 = getOrCreateJoin(join1, joinInfo2);
                Join<J2, J3> join3 = getOrCreateJoin(join2, joinInfo3);
                return condition.apply(join3);
            }
        };
    }

    @Override
    public <E, J1, J2, J3> Specification<E> joinedAttributeNotIn(JoinInfo<? super E, J1> joinInfo1,
                                                                 JoinInfo<J1, J2> joinInfo2,
                                                                 JoinInfo<J2, J3> joinInfo3,
                                                                 SingularAttribute<J3, ?> attribute,
                                                                 Collection<?> value) {
        return not(joinedAttributeIn(joinInfo1, joinInfo2, joinInfo3, attribute, value));
    }

    @Override
    public <E, T> Specification<E> attributeEquals(SingularAttribute<? super E, T> attribute,
                                                   @Nullable T value) {
        return nullIfParamIsNull(value,

                (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(attribute), value));
    }

    @Override
    public <E, J1, T> Specification<E> joinedAttributeEquals(JoinInfo<? super E, J1> joinInfo1,
                                                             SingularAttribute<J1, T> attribute,
                                                             @Nullable T value) {
        return nullIfParamIsNull(value,

                new AbstractReusableJoinSpecification<>() {
                    @Override
                    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                        Join<E, J1> join = getOrCreateJoin(root, joinInfo1);
                        return criteriaBuilder.equal(join.get(attribute), value);
                    }
                });
    }

    @Override
    public <E> Specification<E> attributeLikeIgnoreCase(SingularAttribute<? super E, String> attribute,
                                                        @Nullable String value) {
        return nullIfParamIsNull(value,

                (root, query, criteriaBuilder) ->
                        likeIgnoreCase(criteriaBuilder, root.get(attribute), value));
    }

    @Override
    public <E, J1> Specification<E> joinedAttributeLikeIgnoreCase(JoinInfo<? super E, J1> joinInfo1,
                                                                  SingularAttribute<J1, String> attribute,
                                                                  @Nullable String value) {
        return nullIfParamIsNull(value,

                new AbstractReusableJoinSpecification<>() {
                    @Override
                    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                        Join<E, J1> join = getOrCreateJoin(root, joinInfo1);
                        return likeIgnoreCase(criteriaBuilder, join.get(attribute), value);
                    }
                });
    }

    @Override
    public <E, J1, J2> Specification<E> joinedAttributeLikeIgnoreCase(JoinInfo<? super E, J1> joinInfo1,
                                                                      JoinInfo<J1, J2> joinInfo2,
                                                                      SingularAttribute<J2, String> attribute,
                                                                      @Nullable String value) {
        return nullIfParamIsNull(value,

                new AbstractReusableJoinSpecification<>() {
                    @Override
                    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                        Join<E, J1> join1 = getOrCreateJoin(root, joinInfo1);
                        Join<J1, J2> join2 = getOrCreateJoin(join1, joinInfo2);
                        return likeIgnoreCase(criteriaBuilder, join2.get(attribute), value);
                    }
                });
    }

    @Override
    public <E, J1, J2, J3> Specification<E> joinedAttributeLikeIgnoreCase(JoinInfo<? super E, J1> joinInfo1,
                                                                          JoinInfo<J1, J2> joinInfo2,
                                                                          JoinInfo<J2, J3> joinInfo3,
                                                                          SingularAttribute<J3, String> attribute,
                                                                          @Nullable String value) {
        return nullIfParamIsNull(value,

                new AbstractReusableJoinSpecification<>() {
                    @Override
                    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                        Join<E, J1> join1 = getOrCreateJoin(root, joinInfo1);
                        Join<J1, J2> join2 = getOrCreateJoin(join1, joinInfo2);
                        Join<J2, J3> join3 = getOrCreateJoin(join2, joinInfo3);
                        return likeIgnoreCase(criteriaBuilder, join3.get(attribute), value);
                    }
                });
    }

    private Predicate likeIgnoreCase(CriteriaBuilder builder, Expression<String> expression, String value) {
        return builder.like(builder.lower(expression), simpleLikePattern(value).toLowerCase());
    }

    @Override
    public <E, T extends Comparable<? super T>> Specification<E> attributeGreaterOrEquals(SingularAttribute<? super E, T> attribute,
                                                                                          @Nullable T value) {

        return nullIfParamIsNull(value,

                (root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get(attribute), value));
    }

    @Override
    public <E, T extends Comparable<? super T>> Specification<E> attributeLessOrEquals(SingularAttribute<? super E, T> attribute,
                                                                                       @Nullable T value) {

        return nullIfParamIsNull(value,

                (root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get(attribute), value));
    }

    @Override
    public <E, T extends Comparable<? super T>> Specification<E> attributeInOneOfRanges(SingularAttribute<? super E, T> attribute,
                                                                                        @Nullable Collection<Range<T>> ranges) {
        if (CollectionUtils.isEmpty(ranges)) {
            return null;
        }

        Specification<E> specs = where(null);

        for (Range<T> range : ranges) {

            Specification<E> fromSpec = attributeGreaterOrEquals(attribute, range.getFrom());
            Specification<E> toSpec = attributeLessOrEquals(attribute, range.getTo());

            specs = specs.or(
                    where(fromSpec).and(toSpec)
            );
        }

        return specs;
    }

    @Override
    public <E, T> Specification<E> orderBy(SingularAttribute<? super E, T> attribute,
                                           @Nullable Direction direction) {
        if (direction == null) {
            return null;
        }

        switch (direction) {
            case ASC:
                return orderByAsc(attribute);
            case DESC:
                return orderByDesc(attribute);
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    @Override
    public <E, I extends Comparable<? super I>, T> Specification<E> uniqueByAttribute(Class<E> entityClass,
                                                                                      SingularAttribute<? super E, I> rootId,
                                                                                      IdAggregation rootIdAggregation,
                                                                                      SingularAttribute<? super E, T> attribute) {
        return (root, query, criteriaBuilder) -> {

            Subquery<I> subQuery = query.subquery(rootId.getJavaType());
            Root<E> subRoot = subQuery.from(entityClass);

            subQuery.groupBy(subRoot.get(attribute));

            switch (rootIdAggregation) {
                case MIN:
                    subQuery.select(criteriaBuilder.least(subRoot.get(rootId)));
                    break;
                case MAX:
                    subQuery.select(criteriaBuilder.greatest(subRoot.get(rootId)));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + rootIdAggregation);
            }

            return root.get(rootId).in(subQuery);
        };
    }

    private <E, T> Specification<E> orderByAsc(SingularAttribute<? super E, T> attribute) {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get(attribute)));
            return null;
        };
    }

    private <E, T> Specification<E> orderByDesc(SingularAttribute<? super E, T> attribute) {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get(attribute)));
            return null;
        };
    }

    private <T, C> Specification<T> nullIfCollectionEmpty(Collection<C> collection, Specification<T> specification) {
        if (isEmpty(collection)) {
            return null;
        } else {
            return specification;
        }
    }

    private <T> Specification<T> nullIfParamIsNull(Object param, Specification<T> specification) {
        if (param == null) {
            return null;
        } else {
            return specification;
        }
    }

    private String simpleLikePattern(String value) {
        return "%" + value + "%";
    }
}
