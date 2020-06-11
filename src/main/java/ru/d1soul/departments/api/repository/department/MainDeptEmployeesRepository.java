package ru.d1soul.departments.api.repository.department;

import ru.d1soul.departments.model.MainDeptEmployee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MainDeptEmployeesRepository extends JpaRepository<MainDeptEmployee, Long> {
    List<MainDeptEmployee> findAll(Sort sort);
    Optional<MainDeptEmployee> findByLastNameAndFirstNameAndAndMiddleName(
            String lastName, String firstName, String middleName);
    Optional<MainDeptEmployee> findByLastNameAndFirstNameAndAndMiddleNameAndIdNot(
            String lastName, String firstName, String middleName, Long id);
    void deleteByLastNameAndFirstNameAndMiddleName(
            String lastName, String firstName, String middleName);
}
