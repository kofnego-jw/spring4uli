package at.ac.uibk.fiba.wang.spring4uli.jpa.repository;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long> {

    List<Person> findAllByNameLike(String name);

    @Query("SELECT p FROM Person p WHERE p.name LIKE %?1%")
    List<Person> findByName(String name);

}
