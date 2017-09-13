# Spring 4 Uli

Damit diese Erläuterungen sinnvoll eingesetzt werden können, sollte man Kenntnisse über

* Java
* Maven

verfügen. V.a. sollte man wissen, wie man in Java programmiert und wie Maven-Projekte
aufgebaut sind.

## Erster Schritt:

Die Materialien sind in diesem GitHub-Repo verfügbar. Die Daten sind so organisiert:

Unter "projects" ist ein Maven-Projekt mit vielen Sub-Projekten vorhanden.

Unter "descriptions" sind viele README.md Dateien vorhanden, welche die Erläuterungen zu
jedem Projekt darstellen. Die Ordner-Namen reflektieren die jeweiligen Projekte.

## Präambel

Am einfachsten ist es, wenn man ein Spring-Projekt über https://start.spring.io generieren
lässt. Jedoch finde ich es manchmal störend, wenn mehrere Maven-Module bereits vorhanden sind,
die alle dasselbe Parent-Modul haben und ich nun ein weiteres hinzufügen will, das mit Spring
arbeitet. Es ist da wirklich einfacher, die Spring-Dependencies selbst hinzuzufügen, um die
Modul-Hierachie nicht zu verletzen.

## Vorgehensweise

Studiere die pom.xml in "projects". Da wird man entdecken dass
1. wir Java 8 verwenden und
2. die Spring-Dependencies (über Spring-Boot) eingebunden werden.

Weiter geht es mit "t01di".


## Einige Fehler, die ich begangen habe während des Erstellen 
dieser Projekte:

### Test-Dependencies nicht richtig eingestellt

Oft habe ich vergessen H2 als Test-Dependency zu verwenden. Jetzt habe 
ich H2 einfach als Test-Dependency in das Haupt-POM geschrieben.

### Falsche Konfiguration von Springboot Maven Plugin

Springboot Maven Plugin hilft bei der Erstellung eines lauffähigen
JARs. Allerdings verhindert es -- wenn man die Konfiguration auf 
"repackage" stellt --, dass eine einbindbare Library erstellt wird.
Man sollte dieses Plugin daher nur bei jenem Modul verwenden, das
von keinem anderen gebraucht wird.