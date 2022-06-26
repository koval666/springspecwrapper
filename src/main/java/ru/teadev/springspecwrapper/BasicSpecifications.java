package ru.teadev.springspecwrapper;

import java.util.Collection;
import javax.persistence.metamodel.SingularAttribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

public interface BasicSpecifications {


    <E> Specification<E> distinct(Class<E> rootClass);

    <E> Specification<E> select(ExpressionSupplier<E> supplier);

    <E, J1> Specification<E> select(@NonNull JoinInfo<? super E, J1> joinInfo1,
                                    @NonNull SingularAttribute<J1, ?> attribute);

    <E, J1> Specification<E> select(@NonNull JoinInfo<? super E, J1> joinInfo1,
                                    @NonNull ExpressionSupplier<J1> supplier);

    <E, J1, J2> Specification<E> select(@NonNull JoinInfo<? super E, J1> joinInfo1,
                                        @NonNull JoinInfo<J1, J2> joinInfo2,
                                        @NonNull ExpressionSupplier<J2> supplier);

    <E, J1> Specification<E> join(JoinInfo<? super E, J1> joinInfo1);

    <E, J1, J2> Specification<E> join(JoinInfo<? super E, J1> joinInfo1,
                                      JoinInfo<J1, J2> joinInfo2);

    <E, J1, J2, J3> Specification<E> join(JoinInfo<? super E, J1> joinInfo1,
                                          JoinInfo<J1, J2> joinInfo2,
                                          JoinInfo<J2, J3> joinInfo3);

    <E> Specification<E> attributeIsNull(SingularAttribute<? super E, ?> attribute);

    <E> Specification<E> attributeIsNotNull(SingularAttribute<? super E, ?> attribute);


    <E, J1> Specification<E> joinedAttributeIsNull(JoinInfo<? super E, J1> joinInfo1,
                                                   SingularAttribute<J1, ?> attribute);


    <E, J1> Specification<E> joinedAttributeIsNotNull(JoinInfo<? super E, J1> joinInfo1,
                                                      SingularAttribute<J1, ?> attribute);

    <E, J1, J2> Specification<E> joinedAttributeIsNull(JoinInfo<? super E, J1> joinInfo1,
                                                       JoinInfo<J1, J2> joinInfo2,
                                                       SingularAttribute<J2, ?> attribute);


    <E, J1, J2> Specification<E> joinedAttributeIsNotNull(JoinInfo<? super E, J1> joinInfo1,
                                                          JoinInfo<J1, J2> joinInfo2,
                                                          SingularAttribute<J2, ?> attribute);

    <E, J1, J2, J3> Specification<E> joinedAttributeIsNull(JoinInfo<? super E, J1> joinInfo1,
                                                           JoinInfo<J1, J2> joinInfo2,
                                                           JoinInfo<J2, J3> joinInfo3,
                                                           SingularAttribute<J3, ?> attribute);

    <E, J1, J2, J3> Specification<E> joinedAttributeIsNotNull(JoinInfo<? super E, J1> joinInfo1,
                                                              JoinInfo<J1, J2> joinInfo2,
                                                              JoinInfo<J2, J3> joinInfo3,
                                                              SingularAttribute<J3, ?> attribute);


    <E> Specification<E> attributeIn(SingularAttribute<? super E, ?> attribute,
                                     @Nullable Collection<?> value);

    <E> Specification<E> attributeNotIn(SingularAttribute<? super E, ?> attribute,
                                        @Nullable Collection<?> value);

    <E, J1> Specification<E> joinedAttributeIn(JoinInfo<? super E, J1> joinInfo1,
                                               SingularAttribute<J1, ?> attribute,
                                               @Nullable Collection<?> value);


    <E, J1> Specification<E> joinedAttributeNotIn(JoinInfo<? super E, J1> joinInfo1,
                                                  SingularAttribute<J1, ?> attribute,
                                                  @Nullable Collection<?> value);

    <E, J1, J2> Specification<E> joinedAttributeIn(JoinInfo<? super E, J1> joinInfo1,
                                                   JoinInfo<J1, J2> joinInfo2,
                                                   SingularAttribute<J2, ?> attribute,
                                                   @Nullable Collection<?> value);


    <E, J1, J2> Specification<E> joinedAttributeNotIn(JoinInfo<? super E, J1> joinInfo1,
                                                      JoinInfo<J1, J2> joinInfo2,
                                                      SingularAttribute<J2, ?> attribute,
                                                      @Nullable Collection<?> value);

    <E, J1, J2, J3> Specification<E> joinedAttributeIn(JoinInfo<? super E, J1> joinInfo1,
                                                       JoinInfo<J1, J2> joinInfo2,
                                                       JoinInfo<J2, J3> joinInfo3,
                                                       SingularAttribute<J3, ?> attribute,
                                                       @Nullable Collection<?> value);

    <E, J1, J2, J3> Specification<E> joinedAttributeNotIn(JoinInfo<? super E, J1> joinInfo1,
                                                          JoinInfo<J1, J2> joinInfo2,
                                                          JoinInfo<J2, J3> joinInfo3,
                                                          SingularAttribute<J3, ?> attribute,
                                                          @Nullable Collection<?> value);

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
                                                                                 @Nullable Collection<Range<T>> ranges);

    <E, T> Specification<E> orderBy(@NonNull ExpressionSupplier<E> supplier,
                                    @Nullable Direction direction,
                                    @NonNull Nulls nulls);

    <E, T> Specification<E> orderBy(@NonNull SingularAttribute<? super E, T> attribute,
                                    @Nullable Direction direction,
                                    @NonNull Nulls nulls);

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

    @AllArgsConstructor
    @Getter
    enum Nulls {
        FIRST(true),
        LAST(false);
        private final boolean isFirst;
    }

    enum IdAggregation {
        MIN, MAX
    }
}
