package ru.teadev.springspecwrapper;

import javax.persistence.criteria.JoinType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

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

    public static <F, J> JoinInfo<F, J> create(@NonNull SingularAttribute<? super F, J> joinedAttr,
                                               @NonNull JoinType type,
                                               @NonNull Fetching fetching) {
        return create(joinedAttr.getName(), type, fetching);
    }

    public static <F, J> JoinInfo<F, J> create(@NonNull SetAttribute<? super F, J> joinedAttr,
                                               @NonNull JoinType type,
                                               @NonNull Fetching fetching) {
        return create(joinedAttr.getName(), type, fetching);
    }

    public static <F, J> JoinInfo<F, J> create(@NonNull ListAttribute<? super F, J> joinedAttr,
                                               @NonNull JoinType type,
                                               @NonNull Fetching fetching) {
        return create(joinedAttr.getName(), type, fetching);
    }

    private static <F, J> JoinInfo<F, J> create(@NonNull String joinedAttrName,
                                                @NonNull JoinType type,
                                                @NonNull Fetching fetching) {
        return new JoinInfo<>(joinedAttrName, type, fetching);
    }

}
