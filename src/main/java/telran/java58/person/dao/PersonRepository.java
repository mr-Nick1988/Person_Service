package telran.java58.person.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import telran.java58.person.dto.CityPopulationDto;
import telran.java58.person.model.Child;
import telran.java58.person.model.Employee;
import telran.java58.person.model.Person;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;


public interface PersonRepository extends JpaRepository<Person, Integer> {

    Person[] findByNameIgnoreCase(String name);

    Stream<Person> findByAddressCityIgnoreCase(String city);

    Person[] findByBirthDateBetweenIgnoreCase(LocalDate start, LocalDate end);

    @Query("SELECT new telran.java58.person.dto.CityPopulationDto(p.address.city, COUNT(p)) FROM Person p GROUP BY p.address.city ORDER BY COUNT(p)")
    List<CityPopulationDto> getCitiesPopulation();

    @Query("SELECT p FROM Child p")
    Child[] findAllChildren();

    // @Query("SELECT p FROM Employee p WHERE p.salary BETWEEN ?1 AND ?2")
    Employee[] findBySalaryBetween(int minSalary, int maxSalary);
}

