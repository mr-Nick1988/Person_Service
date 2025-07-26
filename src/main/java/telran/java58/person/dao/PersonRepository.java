package telran.java58.person.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import telran.java58.person.dto.CityPopulationDto;
import telran.java58.person.model.Person;

import java.time.LocalDate;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    List<Person> findByName(String name);

    List<Person> findByAddressCity(String city);

    List<Person> findByBirthDateBetween(LocalDate start, LocalDate end);


    @Query("SELECT new telran.java58.person.dto.CityPopulationDto(p.address.city, COUNT(p)) FROM Person p GROUP BY p.address.city")
    List<CityPopulationDto> getCitiesPopulation();
}
