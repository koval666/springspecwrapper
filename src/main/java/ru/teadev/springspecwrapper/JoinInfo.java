package ru.teadev.springspecwrapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class JoinInfo<F, J> {

    private final String attributeName;

    public static <F, J> JoinInfo<F, J> create(@NonNull SingularAttribute<? super F, J> joinedAttr) {
        return create(joinedAttr.getName());
    }

    public static <F, J> JoinInfo<F, J> create(@NonNull SetAttribute<? super F, J> joinedAttr) {
        return create(joinedAttr.getName());
    }

    public static <F, J> JoinInfo<F, J> create(@NonNull ListAttribute<? super F, J> joinedAttr) {
        return create(joinedAttr.getName());
    }

    private static <F, J> JoinInfo<F, J> create(@NonNull String joinedAttrName) {
        return new JoinInfo<>(joinedAttrName);
    }

}
