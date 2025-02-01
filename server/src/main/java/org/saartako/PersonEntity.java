package org.saartako;

import jakarta.persistence.*;

@Entity(name = "people")
class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private int age;

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", age=" + age +
               '}';
    }
}
