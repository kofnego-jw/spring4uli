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


## Service-Layer

Es ist inzwischen unbestritten, dass two-tiered Applikationen (Backend + Web) schwieriger zu verwalten 
sind. Die meisten modernen Applikationen gebrauchen mindestens 3-Layers: Backend, Domain und Web.

Durch das Verwenden von Repository kann man nun leicht zwischen Web und Repository noch eine Schicht einfügen,
die sog. Service-Layer. In der Tat ist Service-Layer nicht notwendig, man kann ja alles in Web-Controllern 
programmieren. Aber es gibt gute Gründe, um Service-Layer zu verwenden.

Eine Technik, die ich oft verwende, ist die "Anreicherung" der Datenobjekte in Service-Layer:


### Anzeige der Objekte mit mehr Information

Die Idee ist einfach: Das Objekt, das ich in der Datenbank speichere, ist nicht immer das Objekt, das ich 
bei diversen Operationen brauche.

So kann es sein, dass ich beim Speichern und Verändern einer Person-Entität tatsächlich nur die Person brauche:
Name, Email und Information sind alles, was ich brauche. Beim Speichern ist das Objekt also recht einfach.

Aber bei der Anzeige einer Person braucht man auch alle Projekte und Bilder, mit denen diese Person in 
Verbindung steht. Ich muss also die Datenbank nicht nur nach Person, sondern auch nach Projekten und Bildern
abfragen. Das Ergebnis dieser Abfragen kann man dann in einem "erweiterten" Person-Objekt an Web-Tier liefern.

Nun könnte man meinen, dass man diese Information auch per Reverse-Mapping einer zur Verfügung steht. Und
das stimmt. So schafft man aber zirkuläre Abhängigkeiten, die möglicherweise zu Problemen führen können. 

Aber der Vorteil dieser Vorgehensweise sehe ich v.a. darin, dass die Datenbank-Operationen explizit werden. 
Jede Entität enthält nur jene Felder, die auch in der Datenbank in der Tabelle (bzw. in einer JoinTable
zu dieser Entität) gespeichert werden. Und andere Felder sind nur in View-Objekten vorhanden, die man 
nicht verändern kann.

Hier ein Beispiel: Nehmen wir an, die Person-Klasse hat nicht nur Name, Email und Info als Felder, sondern 
auch ein Feld projects, in denen alle Projekten aufgelistet sind, in denen diese Person als Leiter 
arbeitet. Was passiert, wenn man ein neues Projekt erstellt und dieses in das Set einfügt? Bekommt das
neue Projekt automatisch diese Person als Leiter? Oder wird das nicht passieren? Muss man als Programmierer
darauf aufpassen?

Um dieses Problem zu entgehen, vermeide ich reverse mapping der Objekten...


### Service-Layer-Klassen

Während die Repository an Entitäten gekoppelt sind, sollten sich die Service-Layer-Objekte an Operationen 
orintieren, zumindest glaube ich das. 

Ein typisches Beispiel für ein Service wäre z.B. ein Service, mit dem man Personen Projekten zuweist. Nehmen
wir an, dass der Projekt-Leiter seine Mitarbeiter aussuchen kann. Bei der Zuweisung zu einem Projekt soll
die Daten nicht nur in der Datenbank gespeichert werden, sondern auch ein Email an die Person verschickt 
werden.

Um solche "Kopplung" von Operationen zu ermöglichen, wäre ein Service vom Vorteil. Ein Service kann bspw. 
das Person- und das Projekt-Repository und ein Email-Sender gebrauchen, um genau dies zu erreichen.

Außerdem kann Transaktions-Management auch in Service-Layer wirken, um mehrere Daten-Updates in einer Transaktion
durchzuführen.


### Erstellen eines Service

Spring macht das Erstellen eines Services recht einfach. Eine Klasse erstellen, diese mit @Component oder @Service
annotieren, mittels @Autowired die Repositories hineinbringen, und schon hat man ein Service bereit.

Im Gegensatz zu Repository kann man prorammatisch bei CRUD-Operationen zusätzliche Routinen ausführen. Ein Beispiel:
Wenn zwei Personen mit denselben Namen gespeichert werden, kann ein Service ein Exception mit zusätzlichen
Informationen werfen.

Damit Spring die Service-Klassen auch finden kann, muss in der Configurationsdatei JpaSpringAppDefinition noch mit
@ComponentScan mit den Basispaketen annotiert weren.


