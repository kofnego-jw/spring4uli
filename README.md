# spring4uli

Kurze Einführung in Spring (und Angular) für ULI.

In "projects" ist das POM-Projekt und seine Unter-Projekte aufgelistet. 
In "descriptions" liegen die Beschreibungen
für die einzelnen Projekte bereit.


## Gebrauchsanweisung

Es wird davon ausgegangen, dass ihr euch bereits mit 

* Java

* Maven

* JavaScript

* HTML und CSS

* npm

auskennt. Die Voraussetzungen, damit alle Codes funktionieren, sind

* Java 8, JDK 8

* Maven (oder eine IDE, die mit Maven etwas anfangen kann)

* node.js mit npm

* angular/cli und

* MySQL.

In MySQL sollte es eine Datenbank namens testdatabase geben, und 
einen User namens "test" mit Passwort "testMe123" geben, natürlich muss
der User alle Operationen (bis auf Grant) auf der Datenbank ausführen
können,


### TestServer

Um das Entwickeln von Angular-App zu erleichtern, kann man den 
TestServer (in t06angular/src/test/java....) verwenden. Der TestServer
nimmt an, dass man ihn aus dem Verzeichnis "sping4uli" startet. Falls
diese Annahme falsch ist, startet der Server nicht, weil er nicht
in die Embedded-Datenbank ein Bild aufnehmen kann. 

Um das Problem zu lösen, muss man die Pfadangabe in Zeile 41 ändern,
sodass wirklich sample.jpg eingespeist wird. Oder man verzichtet
auf die Testdaten.


## Beschreibung einzelner Module

Hier sind die Kurzdarstellungen für die einzelnen Projekte:


### t01di

Dependency-Injection ist hier das Hauptthema.


### t02di1

Weitere Möglichkeit der Dependency-Injection.


### t03test

Testen von Spring-Beans


### t04jpa

JPA und Spring Data für den Zugriff auf die Datenbank. Das gedachte
Szenario: Eine Datenbank verwaltet Daten zu Personen, Projekten und
Bildern.


### t05rest

Die REST-APIs für t04jpa.


### t06angular

Ein Web-Frontend basiert auf Angular.




