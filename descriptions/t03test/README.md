# Spring-Applikation Testen

Hat man eine Spring-Applikation, so will man wissen, wie man diese testet. Hier schildere ich nur allgemein,
wie Unit-Tests durchgeführt werden kann. V.a. für speziellere Anwendungen, die bspw. Datenbank-Anbindung oder
Web-Anbindung haben, gibt es speziellere Methoden, diese Tests durchzuführen.


## Setting

Nehmen wir an, wir wollen die Componente in t01di erweitern. Wir wollen zunächst einen UnitTest für DataPool
schreiben, und dann wollen wir auch ein eigenes Service schreiben, das DataPool gebraucht, und diese auch testen.


## Unit Test für Beans, die keine DI brauchen

Wenn ein Objekt keine DI gebraucht, d.h. wenn es keine Objekte von Spring entgegennimmt, dann kann man ein UnitTest
schreiben, das gänzlich ohne Spring auskommt.

Ein Beispiel kann man in UnitTestOhneSpring sehen.


## Kreieren eines Beans, das DataPool braucht

Nun erstellen wir ein Service, das DataPool gebraucht: DataPoolFacade. Diese Klasse hat neben DataPool als Feld auch
eine Methode isRegistered(username). Diese Methode sollte "true" zurückgeben, wenn ein Username registriert ist, und
"false", wenn er nicht registriert ist.

Wie kann man diese Klasse nun testen? Hierzu schreiben wir einen Test, der SpringFramework selbst erstellt:
DataPoolFacadeTest!

Zunächst: Die Test-Klasse müssen wir mit einige Annotationen kennzeichnen:

```
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestSpringAppDefinition.class})
```

@RunWith(SpringJUnit4ClassRunner.class)
weist JUnit an, dass die Klasse SpringJUnit4ClassRunner verwendet werden soll, um diesen Test durchzuführen.
Diese Runner-Klasse befindet sich in der Abhängigkeit spring-boot-starter-test.

@ContextConfiguration(classes = {TestSpringAppDefinition.class})
weist Runner-Klasse an, dass die Konfigurationsdatei "TestSpringAppDefinition" verwendet soll, um die Objekte
zu erstellen. In größeren Projekten ist es sinnvoll, die Konfigurationsdatei aufzuspalten in einzelnen Modulen,
und diese Module einzeln zu testen!

Die Testklasse selbst bekommt mittel @Autowired die von Spring generierten Objekten:

```
    @Autowired
    DataPool dataPool;

    @Autowired
    DataPoolFacade facade;
```

Und nun können wir diese Objekte gebauchen und testen. Die Klassen-Annotation
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
gibt vor, dass die Test-Methoden alphabetisch durchlaufen soll. Daher wird in

@Test
public void t01_createUser()

zunächst einen User über DataPool kreiert. (Bedenke, DataPoolFacade hat keine Methode zur Generierung von UserId!)
Dann wird der nächste Test

@Test
public void t02_facadeSayUsernameExists()

ausgeführt. Bei der Übergabe des bereits existierenden Usernames muss Facade true zurück geben. Und wenn wir einen
Namen in

@Test
public void t03_facadeSayUsernameNotExists()

übergeben, der noch nicht in DataPool existiert, müsste DataPoolFacade false zurückgeben.


## Anmerkungen zur Konfigurationsdatei TestSpringAppDefinition

Im Wesentlichen besteht die Datei aus derselben Annotationen und Methoden wie DISpringAppDefinition in t01di. Allerdings
gebraucht TestSpringAppDefinition auch die Beans, die in DISpringAppDefinition definiert sind.

Es gibt nun mehrere Möglichkeiten, diese Definitionen zu holen:

1. Wir schreiben in @ComponentScan mehrere Pakete hinein, sodass TestSpringAppDefinition diese auch findet und erstellt.
oder

2. Wir schreiben ein @Import({DISpringAppDefinition.class}) als Klassen-Annotation für TestSpringAppDefinition. So
werden die Beans in DISpringAppDefinition -- sowohl jene über @ComponentScan als auch jene in @Bean in der Klasse selbst
definiert werden -- auch von SpringFramework "entdeckt" und erstellt.

