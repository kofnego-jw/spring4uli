# Spring MVC

SpringFramework bietet mit SpringMVC auch ein leicht verständliches
Framework für Web an. Die Standard-Konfiguration bindet den 
Apache Tomcat Server als Servlet-Container ein.

In diesem Projekt wollen wir folgendes machen: ein REST-API für das
Projekt t04jpa implementieren. Das eigentliche Frontend kommt wird 
dann später hinzugefügt.


## Prinzip von Spring MVC

In dem Namen "Spring MVC" steckt bereits das Pattern 
"Model-View-Controller", das eigentlich sehr brauchbar ist: Die Daten 
(in unserem Fall die JPA-Entitäten) sind "Models", und das Web-Frontend
wäre dann "View". Der Controller ist zwischengeschaltet und liefert die
Daten sowie die Eingaben zwischen Model und View.

Bevor es SPA (Single-Page-Application) gibt, gibt es unterschiedliche
"View-Technologien", die in einer Spring-Web-Applikation verwendet
werden können: JSP (Java Server Pages) mit unterschiedlichen JSTL,
Velocity, FreeMarker u.v.m.. Auch wenn Spring viele dieser Technologien
noch unterstützt, sind sie für neuere Web-Applikationen nicht mehr 
so interessant. Einzig Thymeleaf scheint noch gewisse Bedeutung in
Spring MVC zu haben, die anderen Technologien -- so scheint es mir --
verlieren vielfach an Bedeutung.

Ein Grund dafür, dass diese Technologien langsam obsolet werden, ist, 
dass es Sicherheitslücken in View-Technologien gibt, die leicht 
ausgenutzt werden können. Eine Lücke, unter der ich auch gelitten habe,
ist Data-Binding in solchen Frameworks: Data-Binding nimmt die 
Web-Anfrage entgegen und erstellt Java-Objekte daraus, mit denen der
Controller dann arbeiten kann. Während einfache Umwandlung aus Eingabe
zu String unverdächtig ist, kann man auch komplexe Java-Objekte erstellen
lassen. Hier gibt es auch Möglichkeiten, nicht nur eigene, sondern auch
Standard-Java-Objekte zu erstellen, die bspw. ein Thread startet...

Ein anderer Grund für das Auslaufen der View-Technologien liegt sicherlich
im Aufkommen von SPAs. Dank HTML5, CSS und JavaScript kann man heute
Clients in Browser erstellen, die auf einfache Server-APIs zurückgreifen.
Mittels Angular und ähnlichen JS-Frameworks muss der Server die Darstellung 
der Inhalte nicht mehr übernehmen, sonder der Client (Browser) übernimmt
diese Aufgabe. Das spart nicht nur Server-Resourcen, sondern man kann
die Aufgaben viel einfacher teilen: Während ein Team sich um Backend
und Domain kümmert, kümmert sich ein anderes Team -- mit Hilfe von
Web-Designern -- um das Frontend. Beide können parallel entwickelt werden!
Das einzige, woran sich beide Teams halten müssen, ist die sog. 
REST-Schnittstelle, also dort, wo Frontend die Daten von Backend bekommt
bzw. wo Backend die Daten von Frontend entgegennimmt.

In diesem Projekt kümmern wir uns nur um diese REST-Schnittstelle.


## Voraussetzungen für REST

Damit wir eine REST-Schnittstelle erstellen können, müssen wir zunächst 
die abhängige Bibliothek anhängen. In Spring-Boot werden alle Bibliotheken
mittels 

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

eingebunden. Mit dabei sind neben die Abhängigkeiten von SpringMVC auch
Apache Tomcat-Server, der als embedded Servlet-Container für die 
Web-Anbindung sorgt, und Jackson-Bibliothek, die für die Data-Binding 
von Java-Objekten zu JSON zuständig ist.

Man kann statt Tomcat natürlich auch Jetty verwenden, hier muss man 
nur die Dependency einwenig verändern:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

Und für unseres Projekt binden wir noch das eigene Projekt t04jpa an,
damit wir die JPA-Bibliotheken zur Verfügung haben.

In der Spring Configuration-Datei importieren wir die Konfiguration von
JpaSpringAppDefinition und fügen noch ein @ComponentScan hinzu, sodass
Spring die REST-Controller findet.


## Erstellen eines REST-Controllers

Für Spring ist jeder REST-Controller auch nur ein "Bean", das es findet
und erstellt. Bei Bedarf fügt er andere Beans in den Controller hinzu.
V.a. hier zeigt es sich, dass Spring die Arbeit wesentlich erleichter:
Denn statt jeder Controller die Aufgabe zu geben, dass er alle 
Abhängigkeiten (EntityManager von JPA, Service-Objekte etc.) selbst
erstellt, bekommt er diese einfach von Spring geliefert. Ein Controller
muss sich nicht mehr um alles kümmern, sondern haben eigentlich nur noch 
folgende Aufgaben:

1. Aus Web-Abfragen-Parametern die passende Java-Objekte zu genrieren,

1. diese Objekte als Eingabe für die Abfrage an Service-Objekte zu 
verwenden, und dann

1. die passenden Antworten zu generieren.

Spring kümmert sich um die Umwandlung von Eingaben zu Objekten, und 
um die Generierung von HTTP-Antworten, die Programmierer sich im Regelfall 
nur um den Aufruf der Methoden kümmern.


### Ein erster einfacher REST-Controller

Bei "Hello World" wird meist nur ein String oder ein einfaches JSON-Objekt
zurückgegeben. Bei der Einführung gut brauchbar. Deswegen gibt es auch
einen HelloWorldController hier.

Ein einfacher Controller schaut so aus:

```
@Controller
@RequestMapping("/api/hello")
public class HelloWorldController {

    @RequestMapping("/with/{name}")
    public String helloAgain(@PathVariable("name") String name) {
        return "Hello " + name.trim() + "!";
    }

}

```

Mit der Annotation @Controller zeigt man an, dass hier ein Web-Endpoint
definiert wird. Mit @RequestMapping zeigt man an, unter welchem URL 
diese Schnittstelle zu erreichen ist.

URL-Bildung = context-path + Klassen-Annotation + Methoden-Annotation

d.h. wenn der Context-Path (in "application.properties" unter 
`server.context-path=/path` definierbar, sonst ist es "/") "/myApp" 
heißt und die Klassen @RequestMapping("/api/mine"), und die 
Methode @RequestMapping("/again") lautet, dann erreicht man die 
Schnittstelle unter

http://localhost:8080/myApp/api/mine/again

Und wenn man die Port-Adresse verändern will, kann man das auch in
application.properties unter `server.port=56789` tun.

Ich habe die Port auf 56789 gestellt und den Context-Path auf "/t05".
Jetzt kann ich diese Methode mit

http://localhost:56789/t05/api/hello/with/Uli

aufrufen!


### Starten des einfachen Servers

Da wir jetzt einen Server eingebunden haben, wird Spring nicht mehr 
automatisch beendet, wenn wir die Configurationsdatei mit der 
main() Methode startet. Jetzt hört Spring auf Port 8080 und erwartet
unsere Eingabe.

Jedoch funktioniert der Controller nicht. Wenn wir die Adresse eintippen,
dann erscheint eine Fehlermeldung. Und in der Konsole gibt es auch
eine Fehler-Meldung.

Der Grund hierfür: Gibt eine Methode einen String zurück, sucht 
Spring nach einem View mit diesen Namen! Wir müssen daher etwas 
anderes tun.


### REST-Schnittstelle definieren

Da wir keinen View haben wollen, sondern "nur" eine JSON-Antwort, 
sollen wir @Controller nicht verwenden, sondern @RestController. Mit
@RestController nimmt Spring an, dass der String (aber auch jedes 
andere JavaObjekt), den die Methode zurückgibt, ein JSON-Objekt
sein sollte. Und deswegen serialisiert Spring diese Objekte 
mittels Jackson-Bibliothek als JSON.

Mit der Änderung von @Controller zu @RestController erscheint nicht 
mehr eine Fehler-Meldung, sondern der Satz 

Hello Uli!

Es gibt allerdings eine bessere Methode, die ich von Uli gelernt habe:
Statt ein Java-Objekt sollte die Methode ein ResponseEntity<T> zurückgeben.

Die Klasse ResponseEntity hat nur einen Fehler: Der Name ist zu lang und
man muss zuviel tippen. Sonst ist sie perfekt für REST-Schnittstellen.
Schauen wir uns ein Beispiel an:

```
    @RequestMapping("/with/{name}")
    public ResponseEntity<String> helloAgain(@PathVariable("name") String name) {
        String answer = "Hello " + name.trim() + "!";
        return ResponseEntity.ok(answer);
    }

}

```

Im Wesentlichen macht die Methode genau das, was die Methode früher
auch gemacht hat. Wenn man jetzt den URL aufruft, kommt die gleiche
Antwort. Aber nun haben wir unterschiedliche Möglichkeiten, die
Antworten zu manipulieren:

1. Wir können einen anderen Status zurückgeben: Mit
`ResponseEntity.status(HttpStatus.NOT_FOUND)` können wir eine 
404-Antwort generieren. Ähnlich kann man den Internal-Server-Error oder
I-Am-A-Teapot-Antwort generieren.

2. Trotz Fehler können (müssen) wir einen Body zurückgeben. Das bedeutet,
dass wir auch Fehlermeldungen individuell gestalten können, ohne sich "nur"
auf die Http-Statuten zu verlassen.


### Mein Vorschlag: Message Definieren

Mit Spring lässt sich daher eine Art Message-System aufbauen, das ich in
vielen Projekten verwende:

1. Jede Abfrage erzeugt ein ResponseEntity, mit einer Message als 
Basis-Antwort.

Ich verwende hierzu die BaseMsg Klasse. Alle Antworten sind davon 
abgeleitet. Gibt es einen Fehler, wird "hasError" zu "true" gestellt
und das Feld "message" beinhaltet dann den Fehler. Gibt es keinen
Fehler, ist "hasError" false und message ist (fast immer) "ok".

2. Die weiteren JSON-Objekte, welche der Client braucht, befindet sich
dann in weiteren Feldern, die in der abgeleiteten Klasse definiert
werden.

So z.B. PersonListMsg, sie erweiterte BaseMsg und enthält noch ein 
Feld "personList", welche die List der Personen enthält. 


### Analyse des PersonControllers

Betrachten wir den PersonController genauer:

```
@RestController
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @RequestMapping("")
    public ResponseEntity<PersonListMsg> findAll() {
        List<PersonFW> list = personService.findAll()
                .stream()
                .map(p -> PersonFW.createPersonFW(p))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PersonListMsg(list));
    }

    @RequestMapping("/{id}")
    public ResponseEntity<PersonFullMsg> findOne(@PathVariable("id") Long id) {
        PersonFullInfo info = personService.getFullInfo(id);
        if (info==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PersonFullMsg("Cannot find Person with id " + id + "."));
        }
        return ResponseEntity.ok(
                new PersonFullMsg(PersonFull.createPersonFull(info.person, info.asLeader, info.asLaborator))
        );
    }

}
```

#### Dependency Injection

Dieser Controller braucht den PersonService. Mittels @Autowired wird
der Service verfügbar gemacht. 

#### @RequestMapping und @PathVariable

Die Klasse wird mit @RequestMapping("/api/person") annotiert. Das bedeutet
wohl, dass sie unter http://localhost:56789/to5/api/person verfügbar
ist.

Die beiden Methoden findOne() und findAll() werden mit @RequestMapping(""")
und @RequestMapping("/{id}") annotiert. Daher steht

findAll() unter dem URL .../api/person zur Verfügung und 

findOne(Long id) unter dem URL .../api/person/id zur Verfügung.

Die Parameter-Annotation @PathVariable("id") zeigt an, dass Spring hier 
den Pfad analysieren soll und dort den Pfad, wo "{id}" steht, als Long
in die Methode einbringen soll. Daher kann man

.../api/person/1 aufrufen, um die Person mit ID 1 aufzurufen.


#### ResponseEntity

Die Methoden geben jeweils ein ResponseEntity zurück. Bei findOne() kann
man außerdem sehen: Wenn in der Datenbank die Person nicht gefunden werden
kann, dann wird ein NOT_FOUND zurückgegeben.


## Controller Testen

Es gibt zwei Möglichkeiten, Controller zu testen. Entweder über simulierte
HTTP-Aufrufe, oder einfach den Controller testen. Hier wird die zweite 
Möglichkeit berichtet.

Im Grunde genommen funktioniert das Testen nach demselben Prinzip wie bei 
JPA. Das bedeutet: Eine Testklasse mit diesen Annotationen erstellen:

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestSpringAppDefinition.class})
@PropertySource("/h2test.properties")
```
Und dann per @Autowired den (Rest-)Controller bekommen. Und dann 
die Methoden ausführen und das Result kontrollieren.

Z.B. kann man in der PersonControllerTest-Klasse diese Methode 
finden:

```
    @Autowired
    private PersonController personController;
    
    @Test
    public void t02_readOne() {
        Person adam = personRepo.findByName("Adam");
        ResponseEntity<PersonFullMsg> re = personController.findOne(adam.getId());
        Assert.assertEquals(HttpStatus.OK, re.getStatusCode());
        PersonFullMsg msg = re.getBody();
        Assert.assertEquals(1, msg.person.asLeader.size());
        Assert.assertEquals(1, msg.person.asLaborator.size());
    }
```

Der Test wird dadurch ausgeführt, dass man den Controller per DI bekommt
und dann die Methode "findOne(id)" ausführt. Das Ergebnis sollte dann
das erwartete Ergebnis sein.

Solche Tests haben natürlich einen Nachteil: sie testen nicht den Teil
"nach" ResponseEntity. 


## Über testhelper

Eine Strategie, um das Testen zu vereinheitlichen, ist das Erstellen
eines TestHelper-Moduls: Dort schreibe ich standardisierte Klassen,
welche die Datenbank mit genormten Daten füllen und sonst auch einige 
Helper-Klassen, welche das Testen vereinfachen. Wichtig dabei ist 
natürlich, dass dieser Helper auf die Ontology basieren und deswegen
zum Test von Ontologie nicht geeignet ist.

