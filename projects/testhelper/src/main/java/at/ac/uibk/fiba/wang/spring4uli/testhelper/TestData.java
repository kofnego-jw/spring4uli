package at.ac.uibk.fiba.wang.spring4uli.testhelper;

import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Person;
import at.ac.uibk.fiba.wang.spring4uli.jpa.ontology.Project;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.PersonRepo;
import at.ac.uibk.fiba.wang.spring4uli.jpa.repository.ProjectRepo;

public class TestData {

    public static void writeData(PersonRepo personRepo, ProjectRepo projectRepo) throws Exception {

        // 4 Persons 2 Projects

        Person adam = new Person("Adam", "adam@example.com", "Adam info");
        adam = personRepo.save(adam);

        Person berta = new Person("Berta", "berta@example.com", "Berta info");
        berta = personRepo.save(berta);

        Person chris = new Person("Chris", "chris@example.com", "Chris info");
        chris = personRepo.save(chris);

        Person dana = new Person("Dana", "dana@example.com", "Dana info");
        dana = personRepo.save(dana);


        Project p1 = new Project();
        p1.setName("Project one");
        p1.setGoal("Explain Spring");
        p1.setLeader(adam);
        p1.getLaborators().add(berta);
        p1.getLaborators().add(chris);

        p1 = projectRepo.save(p1);

        Project p2 = new Project();
        p2.setName("Project two");
        p2.setGoal("Save the world");
        p2.setLeader(dana);
        p2.getLaborators().add(adam);
        p2.getLaborators().add(berta);

        p2 = projectRepo.save(p2);
    }

    public static void clearData(PersonRepo personRepo, ProjectRepo projectRepo) throws Exception {
        projectRepo.deleteAll();
        personRepo.deleteAll();
    }

}
