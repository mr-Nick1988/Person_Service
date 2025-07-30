package telran.java58.person.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telran.java58.person.dao.PersonRepository;
import telran.java58.person.dto.*;
import telran.java58.person.dto.exception.PersonExistException;
import telran.java58.person.dto.exception.PersonNotFoundException;
import telran.java58.person.model.Address;
import telran.java58.person.model.Child;
import telran.java58.person.model.Employee;
import telran.java58.person.model.Person;

import java.time.LocalDate;
import java.util.Arrays;


@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public void addPerson(PersonDto personDto) {
        if (personRepository.existsById(personDto.getId())) {
            throw new PersonExistException();
        }
        if (personDto instanceof EmployeeDto) {
            personRepository.save(modelMapper.map(personDto, Employee.class));
            return;
        }
        if (personDto instanceof ChildDto) {
            personRepository.save(modelMapper.map(personDto, Child.class));
            return;
        }
        personRepository.save(modelMapper.map(personDto, Person.class));
    }

    @Override
    public PersonDto getPerson(int id) {
        Person person = findPersonById(id);
        return mapToDto(person);
    }

    @Override
    @Transactional
    public PersonDto deletePerson(int id) {
        Person person = findPersonById(id);
        personRepository.delete(person);
        return mapToDto(person);
    }

    @Override
    @Transactional
    public PersonDto updatePersonName(Integer id, String newName) {
        Person person = findPersonById(id);
        person.setName(newName);
        // personRepository.save(person);
        return mapToDto(person);
    }

    @Override
    @Transactional
    public PersonDto updatePersonAddress(Integer id, AddressDto newAddressDto) {
        Person person = findPersonById(id);
        person.setAddress(modelMapper.map(newAddressDto, Address.class));
        // personRepository.save(person);
        return mapToDto(person);
    }

    @Override
    //@Transactional(readOnly = true)
    public PersonDto[] findPersonsByName(String name) {
        return Arrays.stream(personRepository.findByNameIgnoreCase(name))
                .map(this::mapToDto)
                .toArray(PersonDto[]::new);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDto[] findPersonsByCity(String city) {
        return personRepository.findByAddressCityIgnoreCase(city)
                .map(this::mapToDto)
                .toArray(PersonDto[]::new);
    }

    @Override
    public PersonDto[] findPersonsBetweenAges(Integer minAge, Integer maxAge) {
        LocalDate from = LocalDate.now().minusYears(maxAge);
        LocalDate to = LocalDate.now().minusYears(minAge);
        return Arrays.stream(personRepository.findByBirthDateBetweenIgnoreCase(from, to))
                .map(this::mapToDto)
                .toArray(PersonDto[]::new);
    }

    @Override
    public Iterable<CityPopulationDto> getCityPopulation() {
        return personRepository.getCitiesPopulation();
    }



    @Override
    public ChildDto[] getAllChildren() {
        return Arrays.stream(personRepository.findAllChildren())
                .map(c -> modelMapper.map(c, ChildDto.class))
                .toArray(ChildDto[]::new);
    }

    @Override
    public EmployeeDto[] getEmployeesBySalary(Integer minSalary, Integer maxSalary) {
        return Arrays.stream(personRepository.findBySalaryBetween(minSalary, maxSalary))
                .map(e -> modelMapper.map(e, EmployeeDto.class))
                .toArray(EmployeeDto[]::new);
    }

    @Override
    public void run(String... args) throws Exception {
        if (personRepository.count() == 0) {
            Person person = new Person(1000, "Ivan", LocalDate.of(1985, 3, 11), new Address("Kiev", "Lenina", 81));
            Child child = new Child(2000, "Petr", LocalDate.of(2015, 3, 11), new Address("Odessa", "Arnautska", 21), "Gordon");
            Employee employee = new Employee(3000, "Mary", LocalDate.of(1995, 11, 23), new Address("Rehovot", "Herzl", 81), "Google", 20_000);
            personRepository.saveAll(Arrays.asList(person, child, employee));
        }

    }

    private PersonDto mapToDto(Person person) {
        if (person instanceof Child) {
            return modelMapper.map(person, ChildDto.class);
        }
        if (person instanceof Employee) {
            return modelMapper.map(person, EmployeeDto.class);
        }
        return modelMapper.map(person, PersonDto.class);
    }

    private Person findPersonById(int id) {
        return personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
    }
}
