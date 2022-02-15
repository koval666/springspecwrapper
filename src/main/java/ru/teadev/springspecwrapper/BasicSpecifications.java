package ru.teadev.springspecwrapper;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.List;
import javax.persistence.metamodel.SingularAttribute;

public interface BasicSpecifications {


    <E> Specification<E> distinct(Class<E> rootClass);

    <E, J1> Specification<E> joinedAttributeIn(JoinInfo<? super E, J1> joinInfo,
                                               SingularAttribute<J1, ?> attribute,
                                               @Nullable List<?> value);

    <E, J1, J2> Specification<E> joinedAttributeIn(JoinInfo<? super E, J1> joinInfo1,
                                                   JoinInfo<J1, J2> joinInfo2,
                                                   SingularAttribute<J2, ?> attribute,
                                                   @Nullable List<?> value);

    <E, J1, J2, J3> Specification<E> joinedAttributeIn(JoinInfo<? super E, J1> joinInfo1,
                                                       JoinInfo<J1, J2> joinInfo2,
                                                       JoinInfo<J2, J3> joinInfo3,
                                                       SingularAttribute<J3, ?> attribute,
                                                       @Nullable List<?> value);

    <E> Specification<E> attributeIn(SingularAttribute<? super E, ?> attribute,
                                     @Nullable List<?> value);

    <E, T> Specification<E> attributeEquals(SingularAttribute<? super E, T> attribute,
                                            @Nullable T value);

    <E, J1, T> Specification<E> joinedAttributeEquals(JoinInfo<? super E, J1> joinInfo,
                                                      SingularAttribute<J1, T> attribute,
                                                      @Nullable T value);

    <E> Specification<E> attributeLikeIgnoreCase(SingularAttribute<? super E, String> attribute,
                                                 @Nullable String value);

    <E, J1> Specification<E> joinedAttributeLikeIgnoreCase(JoinInfo<? super E, J1> joinInfo1,
                                                           SingularAttribute<J1, String> attribute,
                                                           @Nullable String value);

    <E, J1, J2> Specification<E> joinedAttributeLikeIgnoreCase(JoinInfo<? super E, J1> joinInfo1,
                                                               JoinInfo<J1, J2> joinInfo2,
                                                               SingularAttribute<J2, String> attribute,
                                                               @Nullable String value);

    <E, J1, J2, J3> Specification<E> joinedAttributeLikeIgnoreCase(JoinInfo<? super E, J1> joinInfo1,
                                                                   JoinInfo<J1, J2> joinInfo2,
                                                                   JoinInfo<J2, J3> joinInfo3,
                                                                   SingularAttribute<J3, String> attribute,
                                                                   @Nullable String value);

    <E, T extends Comparable<? super T>> Specification<E> attributeGreaterOrEquals(SingularAttribute<? super E, T> attribute,
                                                                                   @Nullable T value);

    <E, T extends Comparable<? super T>> Specification<E> attributeLessOrEquals(SingularAttribute<? super E, T> attribute,
                                                                                @Nullable T value);

    /**
     * @param ranges one of the range boundaries may be null
     * @return Specification for searching for an entity with an attribute in the range or equal to one of the boundaries
     */
    <E, T extends Comparable<? super T>> Specification<E> attributeInOneOfRanges(SingularAttribute<? super E, T> attribute,
                                                                                 @Nullable List<Range<T>> ranges);

    <E, T> Specification<E> orderBy(SingularAttribute<? super E, T> attribute,
                                    @Nullable Direction direction);

    <E, I extends Comparable<? super I>, T> Specification<E> uniqueByAttribute(Class<E> entityClass,
                                                                               SingularAttribute<? super E, I> rootId,
                                                                               IdAggregation rootIdAggregation,
                                                                               SingularAttribute<? super E, T> attribute);

    @Data
    @Builder
    class Range<T> {
        @Nullable
        T from;
        @Nullable
        T to;
    }

    enum Direction {
        ASC, DESC
    }

    enum IdAggregation {
        MIN, MAX
    }
}
