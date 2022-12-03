package ru.teadev.springspecwrapper;

import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

import static org.hibernate.query.criteria.internal.path.AbstractFromImpl.DEFAULT_JOIN_TYPE;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class JoinInfo<F, J> {

    @NonNull
    private final String attributeName;
    @NonNull
    private final JoinType type;
    @NonNull
    private final Fetching fetching;

    public static <F, J> JoinInfo<F, J> join(@NonNull SingularAttribute<? super F, J> joinedAttr) {

        return create(joinedAttr.getName(), DEFAULT_JOIN_TYPE, Fetching.JOIN);
    }

    public static <F, J> JoinInfo<F, J> join(@NonNull SingularAttribute<? super F, J> joinedAttr,
                                             @NonNull JoinType type) {

        return create(joinedAttr.getName(), type, Fetching.JOIN);
    }

    public static <F, J> JoinInfo<F, J> join(@NonNull SetAttribute<? super F, J> joinedAttr) {

        return create(joinedAttr.getName(), DEFAULT_JOIN_TYPE, Fetching.JOIN);
    }

    public static <F, J> JoinInfo<F, J> join(@NonNull SetAttribute<? super F, J> joinedAttr,
                                             @NonNull JoinType type) {
        return create(joinedAttr.getName(), type, Fetching.JOIN);
    }

    public static <F, J> JoinInfo<F, J> join(@NonNull ListAttribute<? super F, J> joinedAttr) {

        return create(joinedAttr.getName(), DEFAULT_JOIN_TYPE, Fetching.JOIN);
    }

    public static <F, J> JoinInfo<F, J> join(@NonNull ListAttribute<? super F, J> joinedAttr,
                                             @NonNull JoinType type) {
        return create(joinedAttr.getName(), type, Fetching.JOIN);
    }


    public static <F, J> JoinInfo<F, J> fetch(@NonNull SingularAttribute<? super F, J> joinedAttr) {

        return create(joinedAttr.getName(), DEFAULT_JOIN_TYPE, Fetching.FETCH);
    }

    public static <F, J> JoinInfo<F, J> fetch(@NonNull SingularAttribute<? super F, J> joinedAttr,
                                              @NonNull JoinType type) {

        return create(joinedAttr.getName(), type, Fetching.FETCH);
    }

    public static <F, J> JoinInfo<F, J> fetch(@NonNull SetAttribute<? super F, J> joinedAttr) {

        return create(joinedAttr.getName(), DEFAULT_JOIN_TYPE, Fetching.FETCH);
    }

    public static <F, J> JoinInfo<F, J> fetch(@NonNull SetAttribute<? super F, J> joinedAttr,
                                              @NonNull JoinType type) {
        return create(joinedAttr.getName(), type, Fetching.FETCH);
    }

    public static <F, J> JoinInfo<F, J> fetch(@NonNull ListAttribute<? super F, J> joinedAttr) {

        return create(joinedAttr.getName(), DEFAULT_JOIN_TYPE, Fetching.FETCH);
    }

    public static <F, J> JoinInfo<F, J> fetch(@NonNull ListAttribute<? super F, J> joinedAttr,
                                              @NonNull JoinType type) {

        return create(joinedAttr.getName(), type, Fetching.FETCH);
    }


    private static <F, J> JoinInfo<F, J> create(@NonNull String joinedAttrName,
                                                @NonNull JoinType type,
                                                @NonNull Fetching fetching) {
        return new JoinInfo<>(joinedAttrName, type, fetching);
    }

    enum Fetching {
        JOIN, FETCH
    }
}
