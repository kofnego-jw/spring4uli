# Eine andere Methode, um Beans zu definieren

In t01di haben wir gesehen, dass SpringFramework automatisch Pakete durchsucht, um einzelne Objekte
zu generieren. Was ist aber, wenn wir Objekte brauchen, die wir nicht selbst definiert haben? Um das
zu klären, nehmen wir an, dass wir für eine Applikation zwei Datei-Verzeichnisse brauchen: Ein TempDir,
das als temporäres Verzeichnis für alles Mögliche aufkommt; und ein RepoDir, in dem alle Dateien
schlußendlich landen.

## Beans in der Configurationsklasse definieren.

Die Datei DI2SpringAppDefinition ist ähnlich wie DISpringAppDefinition aufgebaut. Die Klassen-Annotationen
sind dieselben (bis auf basePackages in @ComponentScan, hier wird der Paket-Name aktualisiert). Und
auch die Main-Methode ist im Wesentlichen gleich geblieben. Es gibt jedoch zwei public Methoden mehr:

```
    @Bean
    public File repoDir() {
        return new File(tempDir(), "repo");
    }

    @Bean
    public File tempDir() {
        return new File("./temp");
    }
```

Mit @Bean werden weitere Beans definiert. Die Methodennamen sind übrigens auch die Namen, unter denen diese
Beans dann anderen Objekten zur Verfügung stehen.

Bemerkenswert: Wenn ein Bean ein anderes Bean braucht, kann es einfach die Methode aufrufen, welche das Objekt
zurückgibt. So liegt RepoDir in einem Unterverzeichnis von TempDir. Daher wird in repoDir() die Methode
tempDir() aufgerufen. SpringFramework "weiß", dass es nur eine Instanz von "tempDir()" erstellen muss.

PS: Wenn es zyklische Abhängigkeiten gibt, d.h., wenn ein Objekt A bei der Erzeugung Objekt B braucht, und
Objekt B bei der Erzeugung Objekt A, dann wirft SpringFramework einen Fehler aus.

## Erstellen eines Consumers

Wir erstellen nun drei Consumers für die Files Objekte im Paket "service":

1. TempDirConsumer: TempDirConsumer verlangt nur TempDir.
2. RepoDirConsumer: RepoDirConsumer braucht nur RepoDir.
3. FilesConsumer: FilesConsumer braucht beide Verzeichnisse.

Und siehe da: Die jeweiligen Verzeichnisse werden über den Namen an die richtige Stelle gebracht!


## Ein Wort zur Annotation @Autowired

Mit @Autowired kann man den Konstruktor, einzelne Felder oder auch Setter-Methoden annotieren. Diese Methoden
können auch miteinander vermischt werden.

Es scheint mir, wenn man gut funktional programmieren und private final Felder verwenden will, soll der
Konstruktor annotiert werden.
