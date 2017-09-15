package at.ac.uibk.fiba.wang.spring4uli.jpa.repository;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Picture;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.PictureProjection;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepo extends JpaRepository<Picture, Long> {

    Picture findByPath(String path);

    @Query("SELECT p.id AS id, p.path AS path FROM Picture p")
    List<PictureProjection> listAllPaths();

    @Query("SELECT p.id AS id, p.path AS path FROM Picture p WHERE ?1 MEMBER OF p.persons")
    List<PictureProjection> findAllPicturesContainingPerson(Person person);

    @Query("SELECT p.id AS id, p.path AS path FROM Picture p WHERE ?1 MEMBER OF p.projects")
    List<PictureProjection> findAllPicturesContainingProject(Project project);

}
