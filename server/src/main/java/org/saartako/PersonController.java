package org.saartako;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/people")
class PersonController {

    @Autowired
    private PeopleService peopleService;

    @GetMapping("")
    public List<PersonEntity> findAll(@RequestParam(value = "name", required = false) String name) {
        if (name != null) {
            return peopleService.findByName(name);
        }
        return peopleService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<PersonEntity> findById(@PathVariable("id") Long id) {
        return peopleService.findById(id);
    }
}
