package ru.teadev.springspecwrapper;

import java.util.Collection;
import javax.persistence.metamodel.SingularAttribute;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

public interface BasicSpecifications {

    <R> Specification<R> distinct(@NonNull Class<R> rootClass);

    <E> Specification<E> select(@NonNull ExpressionSupplier<E> supplier);

    <E, G> Specification<E> select(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                   @NonNull SingularAttribute<G, ?> attribute);

    <E, G> Specification<E> select(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                   @NonNull ExpressionSupplier<G> supplier);

    <E, G> Specification<E> join(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer);

    <E> Specification<E> attributeIsNull(@NonNull SingularAttribute<? super E, ?> attribute);

    <E, G> Specification<E> attributeIsNull(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                            @NonNull SingularAttribute<G, ?> attribute);

    <E> Specification<E> attributeIsNotNull(@NonNull SingularAttribute<? super E, ?> attribute);

    <E, G> Specification<E> attributeIsNotNull(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                               @NonNull SingularAttribute<G, ?> attribute);

    <E> Specification<E> attributeIn(@NonNull SingularAttribute<? super E, ?> attribute,
                                     @Nullable Collection<?> value);

    <E, G> Specification<E> attributeIn(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                        @NonNull SingularAttribute<G, ?> attribute,
                                        @Nullable Collection<?> value);

    <E> Specification<E> attributeNotIn(@NonNull SingularAttribute<? super E, ?> attribute,
                                        @NonNull Collection<?> value);

    <E, G> Specification<E> attributeNotIn(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                           @NonNull SingularAttribute<G, ?> attribute,
                                           @Nullable Collection<?> value);

    <E, T> Specification<E> attributeEquals(@NonNull SingularAttribute<? super E, T> attribute,
                                            @Nullable T value);

    <E, G, T> Specification<E> attributeEquals(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                               @NonNull SingularAttribute<G, T> attribute,
                                               @Nullable T value);

    <E> Specification<E> attributeLikeIgnoreCase(@NonNull SingularAttribute<? super E, String> attribute,
                                                 @Nullable String value);

    <E, G> Specification<E> attributeLikeIgnoreCase(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                    @NonNull SingularAttribute<G, String> attribute,
                                                    @Nullable String value);

    <E, T extends Comparable<? super T>> Specification<E> attributeGreaterOrEquals(@NonNull SingularAttribute<? super E, T> attribute,
                                                                                   @Nullable T value);

    <E, G, T extends Comparable<? super T>> Specification<E> attributeGreaterOrEquals(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                                                      @NonNull SingularAttribute<G, T> attribute,
                                                                                      @Nullable T value);

    <E, T extends Comparable<? super T>> Specification<E> attributeLessOrEquals(@NonNull SingularAttribute<? super E, T> attribute,
                                                                                @Nullable T value);

    <E, G, T extends Comparable<? super T>> Specification<E> attributeLessOrEquals(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                                                   @NonNull SingularAttribute<G, T> attribute,
                                                                                   @Nullable T value);

    <E, T extends Comparable<? super T>> Specification<E> attributeInOneOfRanges(@NonNull SingularAttribute<? super E, T> attribute,
                                                                                 @Nullable Collection<Range<T>> ranges);

    <E, G, T extends Comparable<? super T>> Specification<E> attributeInOneOfRanges(@NonNull JoinInfoContainer<? super E, G> joinInfoContainer,
                                                                                    @NonNull SingularAttribute<G, T> attribute,
                                                                                    @Nullable Collection<Range<T>> ranges);

    <E, T> Specification<E> orderBy(@NonNull SingularAttribute<? super E, T> attribute,
                                    @Nullable Direction direction,
                                    @NonNull Nulls nulls);

    <E, T, G> Specification<E> orderBy(@NonNull JoinInfoContainer<E, G> joinInfoContainer,
                                       @NonNull SingularAttribute<G, T> attribute,
                                       @Nullable Direction direction,
                                       @NonNull Nulls nulls);

    <E> Specification<E> orderBy(@NonNull ExpressionSupplier<E> supplier,
                                 @Nullable Direction direction,
                                 @NonNull Nulls nulls);

    <E, G> Specification<E> orderBy(@NonNull JoinInfoContainer<E, G> joinInfoContainer,
                                    @NonNull ExpressionSupplier<G> supplier,
                                    @Nullable Direction direction,
                                    @NonNull Nulls nulls);

    <E, I extends Comparable<? super I>, T> Specification<E> uniqueByAttribute(@NonNull Class<E> entityClass,
                                                                               @NonNull SingularAttribute<? super E, I> rootId,
                                                                               @NonNull IdAggregation rootIdAggregation,
                                                                               @NonNull SingularAttribute<? super E, T> attribute);

}
