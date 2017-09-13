package at.ac.uibk.fiba.wang.spring4uli.jpa.repository;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {

    List<Project> findAllByLeader(Person leader);

    @Query("SELECT p FROM Project p WHERE ?1 MEMBER OF p.laborators")
    List<Project> findAllByLaboratorsHavingPerson(Person laborator);

    Project findByName(String name);

}
