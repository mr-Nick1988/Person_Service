package telran.java58.person.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import telran.java58.person.dto.*;
import telran.java58.person.service.PersonService;


@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addPerson(@RequestBody PersonDto personDto) {
        personService.addPerson(personDto);
    }

    @GetMapping("/{id}")
    public PersonDto getPerson(@PathVariable int id) {
        return personService.getPerson(id);
    }

    @DeleteMapping("/{id}")
    public PersonDto deletePerson(@PathVariable int id) {
        return personService.deletePerson(id);
    }

    @PatchMapping("/{id}/name/{newName}")
    public PersonDto updatePersonName(@PathVariable Integer id, @PathVariable String newName) {
        return personService.updatePersonName(id, newName);
    }

    @PatchMapping("/{id}/address")
    public PersonDto updatePersonAddress(@PathVariable Integer id, @RequestBody AddressDto newAddressDto) {
        return personService.updatePersonAddress(id, newAddressDto);
    }

    @GetMapping("/name/{name}")
    public PersonDto[] findPersonsByName(@PathVariable String name) {
        return personService.findPersonsByName(name);
    }

    @GetMapping("/city/{city}")
    public PersonDto[] findPersonsByCity(@PathVariable String city) {
        return personService.findPersonsByCity(city);
    }

    @GetMapping("/ages/{minAge}/{maxAge}")
    public PersonDto[] findPersonsBetweenAges(@PathVariable Integer minAge, @PathVariable Integer maxAge) {
        return personService.findPersonsBetweenAges(minAge, maxAge);
    }

    @GetMapping("/population/city")
    public Iterable<CityPopulationDto> getCityPopulation() {
        return personService.getCityPopulation();
    }

    @GetMapping("/children")
    public ChildDto[] getAllChildren() {
        return personService.getAllChildren();
    }

    @GetMapping("/salary/{minSalary}/{maxSalary}")
    public EmployeeDto[] getEmployeesBySalary(@PathVariable Integer minSalary, @PathVariable Integer maxSalary) {
        return personService.getEmployeesBySalary(minSalary, maxSalary);
    }
}

