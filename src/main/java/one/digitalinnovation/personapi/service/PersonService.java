package one.digitalinnovation.personapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.model.Person;
import one.digitalinnovation.personapi.repository.PersonRepository;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    private final PersonRepository personRepository;

    public MessageResponseDTO createPerson(final PersonDTO personDTO) {
        final Person personToSave = personMapper.toModel(personDTO);
        final Person savedPerson = personRepository.save(personToSave);
        return createMessageResponse(savedPerson.getId(), "Person successfully created with ID ");
    }

    public MessageResponseDTO updateById(final Long id, final PersonDTO personDTO) throws PersonNotFoundException {
        verifyIfExistis(id);

        final Person personToUpdate = personMapper.toModel(personDTO);
        final Person updatedPerson = personRepository.save(personToUpdate);
        return createMessageResponse(updatedPerson.getId(), "Person successfully updated with ID ");
    }

    public void deleteById(final Long id) throws PersonNotFoundException {
        verifyIfExistis(id);
        personRepository.deleteById(id);
    }

    public List<PersonDTO> listAll() {
        final List<Person> all = personRepository.findAll();
        return all.stream().map(personMapper::toDTO).collect(Collectors.toList());
    }

    public PersonDTO findById(final Long id) throws PersonNotFoundException {
        final Person person = verifyIfExistis(id);
        return personMapper.toDTO(person);
    }

    private Person verifyIfExistis(final Long id) throws PersonNotFoundException {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    private MessageResponseDTO createMessageResponse(final Long id, final String message) {
        return MessageResponseDTO.builder().message(message + id).build();
    }

}
