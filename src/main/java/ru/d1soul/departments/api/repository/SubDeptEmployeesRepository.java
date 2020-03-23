package ru.d1soul.departments.api.repository;

import ru.d1soul.departments.model.SubDeptEmployee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubDeptEmployeesRepository extends JpaRepository<SubDeptEmployee, Long> {
    List<SubDeptEmployee> findAll(Sort sort);
    Optional<SubDeptEmployee> findByLastNameAndFirstNameAndAndMiddleName(
            String lastName, String firstName, String middleName);
    void deleteByLastNameAndFirstNameAndMiddleName(
            String lastName, String firstName, String middleName);
}
