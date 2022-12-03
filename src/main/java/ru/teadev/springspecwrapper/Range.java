package ru.teadev.springspecwrapper;

import lombok.Value;
import org.springframework.lang.Nullable;

@Value(staticConstructor = "of")
public class Range<T> {
    @Nullable
    T from;
    @Nullable
    T to;
}
