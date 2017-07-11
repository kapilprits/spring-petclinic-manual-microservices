package com.springframework.samples.petclinic.owner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Juergen Hoeller
 * Can be Cat, Dog, Hamster...
 */
@Entity
@Table(name = "types")
public class PetType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
}

/*import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "types")
public class PetType extends NamedEntity {

}*/
