package ru.teadev.springspecwrapperexample.service;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.teadev.springspecwrapper.JoinInfo.join;
import static ru.teadev.springspecwrapper.JoinInfoContainer.chain;
import static ru.teadev.springspecwrapper.JoinInfoContainer.single;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.teadev.springspecwrapper.BasicSpecifications;
import ru.teadev.springspecwrapper.Direction;
import ru.teadev.springspecwrapper.Nulls;
import ru.teadev.springspecwrapperexample.model.Company_;
import ru.teadev.springspecwrapperexample.model.Employee_;
import ru.teadev.springspecwrapperexample.model.Task;
import ru.teadev.springspecwrapperexample.model.Task_;
import ru.teadev.springspecwrapperexample.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final BasicSpecifications specs;

    @NonNull
    public List<Task> findTasks(TaskFilterDto taskFilterDto) {


        Specification<Task> specification =
                where(specs.distinct(Task.class))

                        .and(specs.attributeEquals(
                                Task_.name,
                                taskFilterDto.getTaskName()))

                        .and(specs.attributeGreaterOrEquals(
                                single(join(Task_.employee)),
                                Employee_.salary,
                                taskFilterDto.getEmployeeSalaryGTE()))

                        .and(specs.attributeInOneOfRanges(
                                chain(join(Task_.employee),
                                        join(Employee_.company)),
                                Company_.rating,
                                taskFilterDto.getCompanyRatingRanges()
                        ))

                        .and(specs.orderBy(
                                single(join(Task_.employee)),
                                Employee_.birthDate,
                                Direction.ASC,
                                Nulls.LAST
                        ));


        return taskRepository.findAll(specification);
    }
}
