package at.ac.uibk.fiba.wang.spring4uli.jpa.repository;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {

}
