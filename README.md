Spring spec wrapper
============================
Library for convenient reuse of joins when building queries using Spring Data Specifications

Inspired by this stackoverflow question: [link to question](https://stackoverflow.com/questions/21791793/query-from-combined-spring-data-specification-has-multiple-joins-on-same-table)

### Maven:

```xml
<dependency>
    <groupId>ru.teadev</groupId>
    <artifactId>springspecwrapper</artifactId>
    <version>1.4.0</version>
</dependency>
```

### Usage example:

```java
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
                            chain(join(Task_.employee), //the first created join will be reused
                                    join(Employee_.company)),
                            Company_.rating,
                            taskFilterDto.getCompanyRatingRanges()
                    ))

                    .and(specs.orderBy(
                            single(join(Task_.employee)), //the first created join will be reused
                            Employee_.birthDate,
                            Direction.ASC,
                            Nulls.LAST
                    ));

    return taskRepository.findAll(specification);
}
```
[link to code](example/src/main/java/ru/teadev/springspecwrapperexample/service/TaskService.java)
