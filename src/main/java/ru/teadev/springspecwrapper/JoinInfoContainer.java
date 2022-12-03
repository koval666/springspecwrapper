package ru.teadev.springspecwrapper;

import java.util.List;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class JoinInfoContainer<E, G> {


    private final List<JoinInfo<?, ?>> joinInfos;


    public static <E> JoinInfoContainer<E, E> noJoins() {
        return new JoinInfoContainer<>(
                List.of());
    }

    public static <E, G> JoinInfoContainer<E, G> single(@NonNull JoinInfo<E, G> joinInfo1) {
        return new JoinInfoContainer<>(
                List.of(
                        joinInfo1));
    }

    public static <E, G, J1> JoinInfoContainer<E, G> chain(@NonNull JoinInfo<E, J1> joinInfo1,
                                                           @NonNull JoinInfo<J1, G> joinInfo2) {
        return new JoinInfoContainer<>(
                List.of(
                        joinInfo1,
                        joinInfo2));
    }

    public static <E, G, J1, J2> JoinInfoContainer<E, G> chain(@NonNull JoinInfo<E, J1> joinInfo1,
                                                               @NonNull JoinInfo<J1, J2> joinInfo2,
                                                               @NonNull JoinInfo<J2, G> joinInfo3) {
        return new JoinInfoContainer<>(
                List.of(
                        joinInfo1,
                        joinInfo2,
                        joinInfo3));
    }

    public static <E, G, J1, J2, J3> JoinInfoContainer<E, G> chain(@NonNull JoinInfo<E, J1> joinInfo1,
                                                                   @NonNull JoinInfo<J1, J2> joinInfo2,
                                                                   @NonNull JoinInfo<J2, J3> joinInfo3,
                                                                   @NonNull JoinInfo<J3, G> joinInfo4) {
        return new JoinInfoContainer<>(
                List.of(
                        joinInfo1,
                        joinInfo2,
                        joinInfo3,
                        joinInfo4));
    }

}
