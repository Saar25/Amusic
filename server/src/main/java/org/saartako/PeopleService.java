package org.saartako;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class PeopleService {

    @Autowired
    private PeopleRepository peopleRepository;

    public List<PersonEntity> findAll() {
        return this.peopleRepository.findAll();
    }

    public Optional<PersonEntity> findById(long id) {
        return this.peopleRepository.findById(id);
    }

    public List<PersonEntity> findByName(String name) {
        return this.peopleRepository.findByName(name);
    }
}