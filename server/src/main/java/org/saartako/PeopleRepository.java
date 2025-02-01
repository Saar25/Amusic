package org.saartako;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface PeopleRepository extends JpaRepository<PersonEntity, Long> {

    @Query("SELECT p FROM people p WHERE p.name = :name")
    List<PersonEntity> findByName(@Param("name") String name);
}