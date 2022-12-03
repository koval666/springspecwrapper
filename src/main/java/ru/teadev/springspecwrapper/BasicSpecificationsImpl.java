package ru.teadev.springspecwrapper;

import java.util.Collection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.SingularAttribute;

import static org.springframework.data.jpa.domain.Specification.not;
import static org.springframework.data.jpa.domain.Specification.where;
import static org.springframework.util.CollectionUtils.isEmpty;
import static ru.teadev.springspecwrapper.JoinInfoContainer.noJoins;

import lombok.NonNull;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

public class BasicSpecificationsImpl implements BasicSpecifications {


    @Override
    public <R> Specification<R> distinct(@NonNull Class<R> rootClass) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return null;
        };
    }

    @Override
    public <E> Specification<E> select(@NonNull ExpressionSupplier<E> supplier) {
        return select(
                noJoins(),
                supplier);
    }

    @Override
    public <E, G> Specification<E> select(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                          @NonNull SingularAttribute<G, ?> attribute) {
        return select(
                joinInfoContainer,
                (from, query, criteriaBuilder) -> from.get(attribute));
    }

    @Override
    public <E, G> Specification<E> select(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                          @NonNull ExpressionSupplier<G> supplier) {
        return joinedAttributeSpec(
                joinInfoContainer,
                (from, query, criteriaBuilder) -> {
                    //noinspection rawtypes,unchecked
                    query.select((Expression) supplier.get(from, query, criteriaBuilder));
                    return null;
                });
    }

    @Override
    public <E, G> Specification<E> join(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer) {
        return joinedAttributeSpec(
                joinInfoContainer,
                (from, query, criteriaBuilder) -> null);
    }

    @Override
    public <E> Specification<E> attributeIsNull(@NonNull SingularAttribute<? super E, ?> attribute) {
        return attributeIsNull(
                noJoins(),
                attribute);
    }

    @Override
    public <E, G> Specification<E> attributeIsNull(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                   @NonNull SingularAttribute<G, ?> attribute) {
        return joinedAttributeSpec(
                joinInfoContainer,
                (from, query, criteriaBuilder) -> from.get(attribute).isNull());
    }

    @Override
    public <E> Specification<E> attributeIsNotNull(@NonNull SingularAttribute<? super E, ?> attribute) {
        return not(attributeIsNull(attribute));
    }

    @Override
    public <E, G> Specification<E> attributeIsNotNull(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                      @NonNull SingularAttribute<G, ?> attribute) {
        return not(attributeIsNull(joinInfoContainer, attribute));
    }


    @Override
    public <E> Specification<E> attributeIn(@NonNull SingularAttribute<? super E, ?> attribute,
                                            @Nullable Collection<?> value) {
        return attributeIn(
                noJoins(),
                attribute,
                value);
    }

    @Override
    public <E, G> Specification<E> attributeIn(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                               @NonNull SingularAttribute<G, ?> attribute,
                                               @Nullable Collection<?> value) {
        return nullIfCollectionEmpty(value,

                joinedAttributeSpec(
                        joinInfoContainer,
                        (from, query, criteriaBuilder) -> from.get(attribute).in(value)));
    }

    @Override
    public <E> Specification<E> attributeNotIn(@NonNull SingularAttribute<? super E, ?> attribute,
                                               @NonNull Collection<?> value) {
        return not(attributeIn(attribute, value));
    }

    @Override
    public <E, G> Specification<E> attributeNotIn(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                  @NonNull SingularAttribute<G, ?> attribute,
                                                  @Nullable Collection<?> value) {

        return not(attributeIn(joinInfoContainer, attribute, value));
    }

    private <E, G> Specification<E> joinedAttributeSpec(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                        @NonNull JoinSpecification<?, G> joinSpecification) {
        return new AbstractReusableJoinSpecification<>() {
            @Override
            public Predicate toPredicate(@NonNull Root<E> root,
                                         @NonNull CriteriaQuery<?> query,
                                         @NonNull CriteriaBuilder criteriaBuilder) {
                //noinspection rawtypes
                From currentFrom = root;

                //noinspection rawtypes
                for (JoinInfo joinInfo : joinInfoContainer.getJoinInfos()) {
                    //noinspection rawtypes,UnnecessaryLocalVariable,unchecked
                    Join join = getOrCreateJoin(currentFrom, joinInfo);
                    currentFrom = join;
                }

                //noinspection unchecked
                return joinSpecification.toPredicate(currentFrom, query, criteriaBuilder);
            }
        };
    }

    @Override
    public <E, T> Specification<E> attributeEquals(@NonNull SingularAttribute<? super E, T> attribute,
                                                   @Nullable T value) {
        return attributeEquals(
                noJoins(),
                attribute,
                value);
    }

    @Override
    public <E, G, T> Specification<E> attributeEquals(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                      @NonNull SingularAttribute<G, T> attribute,
                                                      @Nullable T value) {
        return nullIfParamIsNull(value,

                joinedAttributeSpec(
                        joinInfoContainer,
                        (from, query, criteriaBuilder) ->
                                criteriaBuilder.equal(from.get(attribute), value)));
    }

    @Override
    public <E> Specification<E> attributeLikeIgnoreCase(@NonNull SingularAttribute<? super E, String> attribute,
                                                        @Nullable String value) {
        return attributeLikeIgnoreCase(
                noJoins(),
                attribute,
                value);
    }

    @Override
    public <E, G> Specification<E> attributeLikeIgnoreCase(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                           @NonNull SingularAttribute<G, String> attribute,
                                                           @Nullable String value) {
        return nullIfParamIsNull(value,

                joinedAttributeSpec(
                        joinInfoContainer,
                        (from, query, criteriaBuilder) ->
                                likeIgnoreCase(criteriaBuilder, from.get(attribute), value)));
    }

    private Predicate likeIgnoreCase(CriteriaBuilder builder, Expression<String> expression, String value) {
        return builder.like(builder.lower(expression), simpleLikePattern(value).toLowerCase());
    }

    @Override
    public <E, T extends Comparable<? super T>> Specification<E> attributeGreaterOrEquals(@NonNull SingularAttribute<? super E, T> attribute,
                                                                                          @Nullable T value) {

        return attributeGreaterOrEquals(
                noJoins(),
                attribute,
                value);
    }

    @Override
    public <E, G, T extends Comparable<? super T>> Specification<E> attributeGreaterOrEquals(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                                                             @NonNull SingularAttribute<G, T> attribute,
                                                                                             @Nullable T value) {
        return nullIfParamIsNull(value,

                joinedAttributeSpec(
                        joinInfoContainer,
                        (from, query, criteriaBuilder) ->
                                criteriaBuilder.greaterThanOrEqualTo(from.get(attribute), value)));
    }

    @Override
    public <E, T extends Comparable<? super T>> Specification<E> attributeLessOrEquals(@NonNull SingularAttribute<? super E, T> attribute,
                                                                                       @Nullable T value) {

        return attributeLessOrEquals(
                noJoins(),
                attribute,
                value);
    }

    @Override
    public <E, G, T extends Comparable<? super T>> Specification<E> attributeLessOrEquals(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                                                          @NonNull SingularAttribute<G, T> attribute,
                                                                                          @Nullable T value) {

        return nullIfParamIsNull(value,

                joinedAttributeSpec(
                        joinInfoContainer,
                        (from, query, criteriaBuilder) ->
                                criteriaBuilder.lessThanOrEqualTo(from.get(attribute), value)));
    }

    @Override
    public <E, T extends Comparable<? super T>> Specification<E> attributeInOneOfRanges(@NonNull SingularAttribute<? super E, T> attribute,
                                                                                        @Nullable Collection<Range<T>> ranges) {
        return attributeInOneOfRanges(
                noJoins(),
                attribute,
                ranges);
    }

    @Override
    public <E, G, T extends Comparable<? super T>> Specification<E> attributeInOneOfRanges(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                                                           @NonNull SingularAttribute<G, T> attribute,
                                                                                           @Nullable Collection<Range<T>> ranges) {
        if (CollectionUtils.isEmpty(ranges)) {
            return null;
        }

        Specification<E> specs = where(null);

        for (Range<T> range : ranges) {

            Specification<E> fromSpec = attributeGreaterOrEquals(joinInfoContainer, attribute, range.getFrom());
            Specification<E> toSpec = attributeLessOrEquals(joinInfoContainer, attribute, range.getTo());

            specs = specs.or(
                    where(fromSpec).and(toSpec)
            );
        }

        return specs;
    }

    @Override
    public <E, T> Specification<E> orderBy(@NonNull SingularAttribute<? super E, T> attribute,
                                           @Nullable Direction direction,
                                           @NonNull Nulls nulls) {

        return orderBy(
                noJoins(),
                (from, query, criteriaBuilder) -> from.get(attribute),
                direction,
                nulls);
    }

    @Override
    public <E, T, G> Specification<E> orderBy(@NonNull JoinInfoContainer<E, G> joinInfoContainer,
                                              @NonNull SingularAttribute<G, T> attribute,
                                              @Nullable Direction direction,
                                              @NonNull Nulls nulls) {
        return orderBy(
                joinInfoContainer,
                (from, query, criteriaBuilder) -> from.get(attribute),
                direction,
                nulls);
    }

    @Override
    public <E> Specification<E> orderBy(@NonNull ExpressionSupplier<E> supplier,
                                        @Nullable Direction direction,
                                        @NonNull Nulls nulls) {
        return orderBy(
                noJoins(),
                supplier,
                direction,
                nulls);
    }

    @Override
    public <E, G> Specification<E> orderBy(@NonNull JoinInfoContainer<E, G> joinInfoContainer,
                                           @NonNull ExpressionSupplier<G> supplier,
                                           @Nullable Direction direction,
                                           @NonNull Nulls nulls) {
        if (direction == null) {
            return null;
        }

        switch (direction) {
            case ASC:
                return orderByAsc(
                        joinInfoContainer,
                        supplier,
                        nulls);
            case DESC:
                return orderByDesc(
                        joinInfoContainer,
                        supplier,
                        nulls);
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    @Override
    public <E, I extends Comparable<? super I>, T> Specification<E> uniqueByAttribute(@NonNull Class<E> entityClass,
                                                                                      @NonNull SingularAttribute<? super E, I> rootId,
                                                                                      @NonNull IdAggregation rootIdAggregation,
                                                                                      @NonNull SingularAttribute<? super E, T> attribute) {
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

    private <E, G> Specification<E> orderByAsc(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                               @NonNull ExpressionSupplier<G> supplier,
                                               @NonNull Nulls nulls) {
        return joinedAttributeSpec(
                joinInfoContainer,

                (from, query, criteriaBuilder) -> {
                    HibernateCriteriaBuilder hCriteriaBuilder = (HibernateCriteriaBuilder) criteriaBuilder;
                    query.orderBy(
                            hCriteriaBuilder.asc(
                                    supplier.get(from, query, criteriaBuilder),
                                    nulls.isFirst()));
                    return null;
                }
        );
    }

    private <E, G> Specification<E> orderByDesc(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                @NonNull ExpressionSupplier<G> supplier,
                                                @NonNull Nulls nulls) {
        return joinedAttributeSpec(
                joinInfoContainer,

                (from, query, criteriaBuilder) -> {
                    HibernateCriteriaBuilder hCriteriaBuilder = (HibernateCriteriaBuilder) criteriaBuilder;
                    query.orderBy(
                            hCriteriaBuilder.desc(
                                    supplier.get(from, query, criteriaBuilder),
                                    nulls.isFirst()));
                    return null;
                }
        );
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
