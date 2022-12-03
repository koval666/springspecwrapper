package ru.teadev.springspecwrapperexample;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import ru.teadev.springspecwrapper.Range;

@SpringBootTest
class ExampleApplicationTests {

    String taskName = "important task";
    BigDecimal employeeSalaryGTE = BigDecimal.valueOf(100000L);
    List<Range<Integer>> companyRatingRanges = List.of(Range.of(3, 5), Range.of(7, null));

    //    @Test
    void contextLoads() {
    }

}
