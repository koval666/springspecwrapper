package ru.teadev.springspecwrapper;

import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

import static org.hibernate.query.criteria.internal.path.AbstractFromImpl.DEFAULT_JOIN_TYPE;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class JoinInfo<F, J> {

    @NonNull
    private final String attributeName;
    @NonNull
    private final JoinType type;

    public static <F, J> JoinInfo<F, J> create(@NonNull SingularAttribute<? super F, J> joinedAttr) {
        return create(joinedAttr, DEFAULT_JOIN_TYPE);
    }

    public static <F, J> JoinInfo<F, J> create(@NonNull SetAttribute<? super F, J> joinedAttr) {
        return create(joinedAttr, DEFAULT_JOIN_TYPE);
    }

    public static <F, J> JoinInfo<F, J> create(@NonNull ListAttribute<? super F, J> joinedAttr) {
        return create(joinedAttr, DEFAULT_JOIN_TYPE);
    }

    public static <F, J> JoinInfo<F, J> create(@NonNull SingularAttribute<? super F, J> joinedAttr,
                                               @NonNull JoinType type) {
        return create(joinedAttr.getName(), type);
    }

    public static <F, J> JoinInfo<F, J> create(@NonNull SetAttribute<? super F, J> joinedAttr,
                                               @NonNull JoinType type) {
        return create(joinedAttr.getName(), type);
    }

    public static <F, J> JoinInfo<F, J> create(@NonNull ListAttribute<? super F, J> joinedAttr,
                                               @NonNull JoinType type) {
        return create(joinedAttr.getName(), type);
    }

    private static <F, J> JoinInfo<F, J> create(@NonNull String joinedAttrName,
                                                @NonNull JoinType type) {
        return new JoinInfo<>(joinedAttrName, type);
    }

}
