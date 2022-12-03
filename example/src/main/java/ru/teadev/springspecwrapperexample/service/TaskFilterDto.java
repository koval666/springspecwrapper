package ru.teadev.springspecwrapperexample.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;
import ru.teadev.springspecwrapper.Range;

@Value
public class TaskFilterDto {

    @Nullable
    String taskName;
    @Nullable
    BigDecimal employeeSalaryGTE;
    @NonNull
    List<Range<Integer>> companyRatingRanges = new ArrayList<>();
}
