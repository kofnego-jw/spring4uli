# Dependency Injection

## Vorbereitung, um Spring-Framework zum Laufen zu bringen

Um DI zu verstehen, stellen wir zunächst sicher, dass Spring funktioniert:

1. Wir erstellen ein Maven-Modul, das von unserem projekt-Modul abgeleitet ist. Damit
sind alle Abhängigkeiten auch eingebunden.

2. Wir sagen, dass wir eine "jar"-Datei zum Schluss haben wollen.

3. Wir erstellen in einem Paket unserer Wahl ("at.ac.uibk.fiba.wang.spring4uli.di" eine
Spring-Configurationsdatei (DISpringAppDefinition). In dieser Datei befindet sich drei
Klassen-Annotationen:

@SpringBootApplication
Das bedeutet: Ich bin eine Spring-Boot-Configurationsdatei, und ich enthalte Verweise
zu allen "Beans" (von SpringFramework verwalteten Objekten).

@EnableAutoConfiguration
Das bedeutet: Bitte konfigurier selbst, ohne dass ich alles explizit nennen werde

@ComponentScan(basePackages = {
        "at.ac.uibk.fiba.wang.spring4uli.di.service"
})
Das bedeutet: Schau in dem Paket "at.ac.uibk.fiba.wang.spring4uli.di.service" nach, ob es
dort Klassen mit der Annotation "@Service" oder "@Component" gibt. Wenn ja, bitte erstell
Objekt(e) aus dieser Klasse und halte sie bereit.

4. In der Klasse DISpringAppDefinition befindet sich eine main-Methode:

```
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DISpringAppDefinition.class);
        Stream.of(context.getBeanDefinitionNames()).sorted()
                .forEach(System.out::println);
    }
```
Diese Methode macht folgendes:
Mittels "SpringApplication.run()" wird ein ApplicationContext (im Wesentlichen: ein Objekt-Pool)
erstellt. Wir lesen daraus alle Namen von den Beans aus, sortieren diese Namen und geben diese
dann an der Konsole aus.

Wenn man jetzt diese Klasse ausführt, gibt Spring einige Logs und eine Liste von Beans in der Konsole
aus. Diese sind für das Funktionieren des Frameworks notwendig.


## Der erste Versuch: Ein Bean definieren und erstellen lassen.

Der erste Bean soll ein Datenpool (DataPool) sein. Es ist im Wesentlichen ein Map<Long,String>
das zu einer ID (Long) einen Username (String) speichert.

Diese Klasse wird mit @Service annotiert. Somit weiß SpringFramework, dass es ein Objekt dieser
Klasse erstellen soll.

Wenn man jetzt DISpringAppDefinition startet, sieht man, dass zusätzlich ein "dataPool" als Bean-Name
aufscheint! Siehe da, Spring hat diese Klasse entdeckt und ein Objekt dieser Klasse erstellt.


## Der zweite Versuch: Ein zweites Bean definieren und das erste Bean hineinbringen lassen.

Das zweite Bean soll nun ein Consumer für das erste Bean sein. Die Klasse "DataPoolConsumer" beinhaltet
ein Feld "private final DataPool dataPool". Mittels

@Service

weiß Spring, dass es DataPoolConsumer erstellen soll. Und anhand der Annotation

@Autowired

bei der Konstruktor-Methode weiß Spring, dass es die Anhängigkeiten, die in der Konstruktur-Methode
angegeben sind, beim Konstruieren verwenden soll.

Da beim Konstruktor auf die Methode run() aufgerufen wird, wird diese nun beim Erstellen ausgeführt. Wenn
wir jetzt DISpringAppDefinition ausführen, sehen wir, dass

```
TestUser has ID of 1! Sie muss 1 sein.
```

bei der Konsole steht und dass ein weiteres Objekt dataPoolConsumer erstellt worden ist.


