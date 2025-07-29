package telran.java58.person.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import telran.java58.person.dao.PersonRepository;
import telran.java58.person.dto.AddressDto;
import telran.java58.person.dto.CityPopulationDto;
import telran.java58.person.dto.PersonDto;
import telran.java58.person.dto.exception.PersonExistException;
import telran.java58.person.model.Address;
import telran.java58.person.model.Person;
import telran.java58.person.dto.exception.PersonNotFoundException;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public void addPerson(PersonDto personDto) {
        if (personRepository.existsById(personDto.getId())) {
            throw new PersonExistException();
        }
        personRepository.save(modelMapper.map(personDto, Person.class));
    }

    @Override
    public PersonDto getPerson(int id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    @Transactional
    public PersonDto deletePerson(int id) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        personRepository.delete(person);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    @Transactional
    public PersonDto updatePersonName(Integer id, String newName) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        person.setName(newName);
       // personRepository.save(person); //Transactional resolve this problem by commit
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    @Transactional
    public PersonDto updatePersonAddress(Integer id, AddressDto newAddressDto) {
        Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        person.setAddress(modelMapper.map(newAddressDto, Address.class));
       // personRepository.save(person);
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto[] findPersonsByName(String name)  {
        return personRepository.findByName(name).stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toArray(PersonDto[]::new);

    }

    @Override
    public PersonDto[] findPersonsByCity(String city) {
        return personRepository.findByAddressCity(city).stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toArray(PersonDto[]::new);
    }

    @Override
    public PersonDto[] findPersonsBetweenAges(Integer minAge, Integer maxAge) {
        LocalDate currentDate = LocalDate.now();
        LocalDate minAgeDate = currentDate.minusYears(maxAge);
        LocalDate maxAgeDate = currentDate.minusYears(minAge);

        return personRepository.findByBirthDateBetween(minAgeDate, maxAgeDate).stream()
                .map(person -> modelMapper.map(person, PersonDto.class))
                .toArray(PersonDto[]::new);
    }

    @Override
    public Iterable<CityPopulationDto> getCityPopulation() {
        return personRepository.getCitiesPopulation();
    }
}
