package ru.teadev.springspecwrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Nulls {
    FIRST(true),
    LAST(false);
    private final boolean isFirst;
}
