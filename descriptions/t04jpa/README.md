# Verwendung von Spring DATA und JPA

Spring Data vereinfacht die Arbeit mit JPA erheblich. Jedoch birgt es auch Tücken in sich, auf die man 
achten sollte. Ich setze voraus, dass Leute bereits mit JPA einwenig vertraut sind. D.h. ihr wisst
schon, was die Annotationen 

```
@Entity
@Table
@Id
@GeneratedValue
@Column
@JoinColumn
etc.
```

bedeuten.

## Voraussetzungen

Um Spring-Data-JPA in einem Spring-Boot Projekt verwenden zu können, muss man nur einige Abhängigkeiten
anbinden:

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
            <version>${spring-boot.version}</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.38</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.191</version>
            <scope>test</scope>
        </dependency>

```

spring-boot-starter-data-jpa bindet neben Spring Data auch Hibernate und JPA Bibliotheken ein. 
In diesem Projekt wird eine MySQL-Datenbank verwendet, daher die Anbindung der Bibliothek 
mysql-connector-java.

Als Test-Datenbank wird eine embedded H2 verwendet. 

## Spring-Einstelljungen

Mit der Anbindung von Datenbank braucht Spring nun einige Einstellungen, die in 
src/main/resources/application.properties
angegeben werden. Zunächst braucht Spring ein JDBC-Url:

spring.datasource.url=jdbc:mysql://localhost:3306/testdatabase

Dieses URL zeigt die Datenbank-Verbindung an. Dann braucht Spring Benutzername und Passwort

spring.datasource.username=test

spring.datasource.password=testMe123

Und zum Schluss gibt man noch an, wo der JDBC-Treiber zu finden ist:

spring.datasource.driver-class-name=com.mysql.jdbc.Driver


### Empfohlene Einstellung in der Produktion

Leider kann es passieren, dass in der Produktion, v.a. dann, wenn das Programm (der Server) für längere Zeit
löuft, die Datenbank-Verbindung abreißt. Hier empfiehlt es sich, die folgende Einstellung auch anzugeben, 
damit eine "verlorene" Datenbankverbindung neu erstellt wird:

spring.datasource.test-on-borrow=true

spring.datasource.validation-query=SELECT 1

So führt Spring -- wenn ich das richtig verstanden habe -- "SELECT 1" aus, um die Datenbank-Verbbindung zu 
überprüfen. Das Ergebnis: Sollte diese nicht vorhanden sein, wird eine neue Verbindung erstellt. Und das 
Programm hat wieder Anbindung an die Datenbank.


### Weitere Einstellungen

Will man das Datenbank-Schema von Hibernate erstellen / korrigieren lassen, kann man 

spring.jpa.hibernate.ddl-auto = update

einstellen. Mögliche Werte: update (aktualisieren), create (erstellen, löscht die Daten vorher), 
create-drop (erstellen, beim Verlassen des Programms wieder alle Daten löschen. Nützlich beim Testen),
validate (Validieren, ob das Datenbank-Schema zu den Enitäten-Definition passt.)


### Einstellung der Test-Datenbank

Man kann beim Testen eine andere Datenbank verwenden als bei der Produktion. Da eine embedded In-Memory-
Datenbank schnell verfügbar ist und die Daten (meist) nicht dauerhaft gespeichert werden, wird die
Verwendung einer solchen Datenbank für Testen empfohlen. Meist wird H2 verwendet, so auch in diesem
Projekt.

Um eine Test-Datenbank verwenden zu können, muss man diese natürlich auch definieren. Man kann entweder
die "application.properties" "überschreiben", indem man eine Datei "src/test/resources/application.properties"
anlegt und die entsprechende Informationen dort festschreibt, oder man legt -- das wird von mir 
als praktikabler angesehen -- eine Date "src/test/resources/h2test.properties" an und schreibt die 
notwendige Information dort hinein. In der Test-Klasse verweist man dann auf diese Datei.

So kann man mehere Test-Systeme verwenden, um unterschiedliche Tests durchführen zu lassen. Bspw. kann man 
die normalen CRUD-Operationen an H2-Datenbank simulierend durchführen, aber system-spezifische Tests 
(z.B. Übertragen der Grunddaten) kann man dann mit einer anderen Datenbank in MySQL durchführen.

Hier noch die Einstellungen in h2test.properties:
```
spring.datasource.url=jdbc:h2:mem:testdatabase
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto = create
```

## Projekt-Setting

Als erstes wollen wir kurz die Daten anschauen, die wir modellieren wollen. Die Datenbank soll drei Arten von
Objekten verwalten: Personen (Person), Projekte (Project) und Bilder (Picture).

Personen haben: id (Long), name (String), email (String), und info (String, @Lob)

Projekte haben: id (Long), name (String), leader (Person) und laborators (Set<Person>).

Bilder haben: id (Long), path (String), persons (Set<Person>), projects (Set<Project>), type (enum PictureType)
und content (byte[], @Lob)

Die Klassen liegen alle in einem Paket ontology und werden mit JPA-Annotation versehen.


## Schritt eins für Spring-Data: Erstellung von Repository

Auch wenn es möglich ist, über SpringFramework EntityManagerFactory oder EntityManager zu bekommen, ist es 
wesentlich einfacher, Repository zu verwenden.

Repository sind Java Interfaces, die Springs JpaRepository erweitern. Spring "sieht" diese Interfaces und 
implementiert diese selbstständig. Es ist recht einfach, diese Interfaces zu definieren und dann in den 
Services diese Repositorien zu verwenden, um auf die Daten zuzugreifen bzw. CRUD-Operationen durchzuführen.


### Repository definieren.

Es ist wohl einfacher, wenn man für jede Entity-Klasse ein eigenes Repository erstellt. Hierzu muss man wissen:

1. Wie heißt die Entity-Klasse? 

2. Welche ist die ID-Klasse dieser Entität?

Die Definition ist dann recht einfach. Ein Java-Interface schreiben, diese mit 
`@Repository` annotieren und diese die JpaRepository-Interface von Spring erweitern.

Bei Person-Repository schaut es dann so aus:

```
@Repository
public interface PersonRepo extends JpaRepository<Person, Long> {

}
```

Hierbei muss man darauf achten, dass @Repository-Annotation beim Interface steht. Sonst wird SpringFramework
dieses Interface nicht automatisch implementieren, und wir bekommen kein Repository zugewiesen.

Jedes Repository ist bereits mit einigen gängigen Methoden ausgestattet:

find(id): Eine Entität aus der Datenbank laden

findAll(): Alle Entitäten dieser Klasse aus der Datenbank laden.

save(entity): Eine Entität speichern / updaten

save(Collection<entity>): Alle Entitäten in der Kollektion speichern


### Repository erweitern

Oft reichen die Methoden nicht aus, um alle Operationen durchführen zu können. Spring bietet mehrere Möglichkeiten,
das Repository zu erweitern.

#### Methode 1: Methoden in Interface benennen. 

Spring erkennt anhand von Methoden-Namen, welche JPQL-Abfrage gestartet werden soll. Z.B.

```
    List<Person> findAllByNameLike(String name); 
```

Hier weiß Spring anhand des Namen der Methode, dass alle Personen, deren Namen "like :name" sind, 
zurückgegeben werden sollen.

#### Methode 2: JPQL-Query selbst schreiben.

Eine zweite Möglichkeit, die Methoden zu erweitern, ist selbst die JPQL-Abfrage zu schreiben. Die eigentliche
JPQL steht dann in der @Query-Annotation von der Methode.

```
   @Query("SELECT p FROM Person p WHERE p.name LIKE %?1%")
   List<Person> findByName(String name);
```

Somit kann Repository eigentlich alle Operationen durchführen, die mit JPQL möglich sind. Und selten 
muss man auf andere zurückgreifen.


## Schritt 2: SpringFramework sagen, dass es Repository kreieren soll

Damit Spring die Repository erstellt, muss man zwei Annotationen anbringen:

1. @EntityScan: Damit weiß Spring, dass es ORM initiieren soll und welche Klassen nun verwaltet werden müssen.

2. @EnableJpaRepositories: Damit weiß Spring, dass es auch Repository erstellen muss.

Und so schaut die Spring Configuration aus:

```
@SpringBootApplication
@EntityScan(basePackages = {
        "at.ac.uibk.fiba.wang.spring4uli.jpa.ontology"
})
@EnableJpaRepositories(basePackages = {
        "at.ac.uibk.fiba.wang.spring4uli.jpa.repository"
})
public class JpaSpringAppDefinition {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(JpaSpringAppDefinition.class);
        Stream.of(ctx.getBeanDefinitionNames()).sorted()
                .forEach(System.out::println);
    }

}

```
Die basePackages geben jeweils an, wo Spring starten soll, um nach Entitäen / Repository zu suchen.


### Repository Testen

Wie kann man nun die Embedded Datenbank verwenden, um Tests durchzuführen? Im Grunde genommen schreibt 
man den gleichen Test wie in t03test, allerdings muss man neben @ContextConfiguration noch die Annotation
@PropertySource angeben, um die "Ersatz-Properties-Datei" anzugeben.

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaSpringAppDefinition.class})
@PropertySource("/h2test.properties")
```

Da alle drei Annotationen bei jenen Tests wiederholt werden müssen, die auf "/h2test.properties" basieren,
empfiehlt es sich, eine Klasse H2TestBase zu schreiben, welche diese Annotationen bereits beinhalten. 
Alle anderen Tests müssen nur noch diese Klasse erweitern. 

Um das Repository PersonRepo testen zu können, kann man ein PersonRepoTest schreiben, welche dann 
im bewährten JUnit-Manieren die Klasse testet.

Beachte, bei diesem Test wird nicht in die MySQL-Datenbank geschrieben, d.h. man sieht nachher keine Änderung
in MySQL. Will man schauen, ob die Daten wirklich "richtig" geschrieben worden sind, muss man eine andere
Properties-Datei schreiben, welche genau diese veranlasst.


