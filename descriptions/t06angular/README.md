# Angular Frontend

In diesem Teil wird geschildert, wie ich ein Frontend für die 
REST-API in t05rest mittesl Angular zu verwirklichen versuche. Hier wird
Angular (also Angular 4 und nicht AngularJS) mit Bootstrap verwendet.


## Voraussetzung

Folgende Voraussetzungen sollte gegeben sein:

1. Node.js ist installiert. Siehe: https://nodejs.org/en/

2. Angular-CLI ist installiert. Siehe: https://github.com/angular/angular-cli

Und dann erstellen wir ein Maven-Modul "t06angular".

Es gibt nur eine Klasse mit der Main-Methode.

Und dann erstellt man im Verzeichnis `src/main` mit `ng new webapp` 
eine Angular-Applikation.

### Vorbereitung

#### Einstellung in .angular-cli.json

Als erstes ändert man die in `.angular-cli.json` die Einstellungen für

* "outputDir" zu "../resources/static": Damit wird bei "ng build" die
static folder in "resources" erstellt. Hier sucht SpringFramework jene
Dateien, die bei der Web-Anwendung als statische Ressourcen geliefert
werden.

* "assets": Ich lösche die favicon.ico, aber das ist Geschmackssache.

* "styles": Hier füge ich 
"../node_modules/bootstrap/dist/css/bootstrap.min.css" hinzu, damit
wird Bootstrap CSS eingebunden.


#### Einstellung des Development-Servers

Angular-CLI kommt mit einem Dev-Server, der das Entwickeln wesentlich
erleichtert. Damit man die Spring-Controller beim Entwickeln schon 
vefwenden kann, kann man ein Proxy einrichten, damit man beim Entwickeln
von Frontend direkt auf das Backend (oder wenn man die Testumgebund
verwendet: Testserver) zugreifen kann.

Um das machen zu können, erstellt man im Verzeichnis "src" eine 
Datei namens "proxy.conf.json", und schreibt

``` 
{
  "/t06/api": {
    "target": "http://localhost:56790",
    "secure": false
  }
}

```

hinein. Diese Datei besagt: mappe alle calls zu "/t06/api" zum Server
"localhost:56790".

Und zu letzt, will man, dass diese Einstellung automatisch übernommen
wird. Das macht man am einfachsten in der Datei `package.json`, als
ein Start script:

``` 
"start": "ng serve --proxy-config proxy.conf.json --deploy-url /t06/"
```

So kann man, wenn man npm start eingibt, den Development-Server 
samt der Proxy- und Pfad-Einstellung starten. Später, wenn man
`ng build` startet, wird die Angular-App direkt in Maven-Verzeichnis
"src/main/resources/static" gepackt. Wenn man dann ein maven package 
startet, wird die lauffähige JAR-Datei samt der Angular-App erstellt.


## Vorgehensweise

Als erstes überlege ich mir, welche "Teile" das Frontend haben soll:

* Header

* Menüleiste

* Hauptfenster

* Footer mit Statusleiste 

Wir haben ja drei Entitäten, die in der Datenbank verwaltet werden. Für 
jede Entität-Art muss es CRUD-Operationen geben. Es soll also neben
Willkommen- und Impressumsseite auch eine Seite für jede Entitätart 
geben.


### Komponente?

In Angular spricht man von "Komponenten", eine Komponente ist im
Wesentlichen ein (selbst-definitiertes) HTML-Element, das in einem
Bereich der SPA aufscheint. Eine Komponente kann also beliebige 
HTML und andere Komponenten beinhalten und soll eine Funktion haben,
z.B. "Darstellung der Willkommensseite" oder auch "eine 
Eingabeformular verwalten". Natürlich beinhaltet eine Aufgabe auch 
"Unteraufgaben", die von Kind-Komponenten übernommen werden können.


### Service ?

Neben Komponenten gibt es noch Services, die bei Bedarf in Komponente
oder auch Services "hineingebracht" werden können. Sie dienen v.a.
der Inter-Komponenten-Kommunikation, aber auch sonstige Tätigkeiten 
wie "XHR-Abfrage starten".


### Modul ?

Komponenten und Services werden in einem Modul zusammengefasst. Da die
Applikation recht klein ist, wird sie nur ein Modul beinhalten,
das `AppModule`. Die Definition des Moduls wird in der Datei 
`src/app/app.module.ts` festgehalten. Da alle Services und Komponenten
im Modul "deklariert" werden müssen, ist es vielfach notwendig,
dass wir diese Datei verändern.


## Erster Schritt

Mit `ng new ...` haben wir bereits das erste Modul mit der ersten 
Komponente im Verzeichnis `src/app` erstellt. Um das Ergebnis sehen zu 
können, starten wir im Verzeichnis `webapp` den Dev-Server mit dem
Befehl `npm start`. Im Terminalfenster seiht man, dass AngularCli
die Module kompiliert, steht `webpack: Compiled successfully.` im
Fenster, bedeutet das, dass alle Quellcodes kompiliert werden 
können und dass man zu "http://localhost:4200" surfen kann, um das 
Ergebnis zu sehen.

Und nun startet man im IDE den Spring-Server. Unter 
`http://localhost:56790/t06/api/person` sollte die Personenliste 
(im Moment wohl noch leer) zu sehen sein. Wenn man den Proxy richtig
eingestellt hat, sieht man das gleiche Ergebnis unter
`http://localhost:4200/t06/api/person`.

Und nun -- da wir einen Context-Path "/t06" gebrauchen -- müssen wir 
den Kontext-Path noch in die Datei `src/index.html` schreiben.

```
  <base href="/t06/"> 
```

Wenn man die Datei speichert, sieht man, wie webpack die Sachen neu 
kompiliert. Ändert man noch andere Elemente, die besser sichtbar sind, 
sieht man das Ergebnis auch direkt am Browser.

Es gibt in der `index.html` in <body> nur <app-root></app-root>. 
<app-root> ist die einzige Komponente, die im Moment verfügbar ist.


### App-Komponente verändern

Wir wollen <app-root> verändern. Alle Komponenten und Services stehen 
unter Verzeichnis `app`.

* In `app.component.css` kann man CSS-Anweisungen, die nur 
<app-root> betreffen, schreiben.

* In `app.component.html` wird die HTML-Markup von der Komponente 
festgehalten. Das ist das sog. "template" von der Komponente.

* In `app.component.ts` wird die "Logik" der Komponente festgehalten.
Sie ist im Prinzip eine Typescript-Klasse.

* In `app.component.spec.ts` werden die Tests für die Komponente 
festgeschrieben.

Schauen wir uns die ts-Datei genauer an:

``` 
import { Component } from '@angular/core';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
}
```

Die Import-Statement funktioniert ähnlich wie Java-Imports. Wenn wir 
eine TS-Klasse gebrauchen (wie Component), müssen wir sie am Anfang 
importieren.

Die Einstellung der Komponente geschieht in @Component(). @Component()
ist ein sog. Decorator. Ähnlich wie die Java-Annotationen funktionieren
auch die Decorators so, dass sie bestimmte Funktionen für TS-Kompiler
anzeigen. Wichtig: Decorators sind JS-Funktionen, d.h. sie
haben immer () am Schluss. 

Der @Component-Decorator verlangt ein JS-Objekt, das folgende Felder
haben sollen:

`selector`: Der HTML-Element-Name. In diesem Fall <app-root>.

`templateUrl` oder `template`: Die Templates, die gerendert werden müssen.
Bei `templateUrl` muss ein relatives Pfad zu der TS-Datei stehen. Das 
relative PFad muss mit "./" anfangen, sonst findet Webpack die Datei 
nicht.

`styleUrls`: Hier die URLs für die CSS-Dateien.

So, da ich hier keine extra CSS verwende, außer jene in `src/styles.css`,
werde ich diese nicht verändern. Als erstes füge ich die unterschiedlichen
Flächen als <div> in app.component.html ein.

``` 
<div class="container">
  <div class="row">
    <header>
      <div class="col-md-12 text-center">
        <h1>Spring 4 Underdogs Like I</h1>
      </div>
    </header>
  </div>
  <div class="row">
    <nav>
      <div class="col-md-12">
        <ul class="nav nav-pills">
          <li><a href="">Willkommen</a></li>
          <li><a href="">Personen</a></li>
          <li><a href="">Projekte</a></li>
          <li><a href="">Bilder</a></li>
          <li><a href="">Impressum</a></li>
        </ul>
      </div>
    </nav>
  </div>
  <div class="row">
    <main>
      <div id="mainView">
        Hauptfenster
      </div>
    </main>
  </div>
  <div class="row">
    <footer>
      <div class="col-xs-12 text-center">
        <p>Status</p>
      </div>
    </footer>
  </div>
</div>
```

Dieses Template zeigt unterschiedliche Komponenten alle in einem. Als 
Beispiel will ich zuerst den Router einbinden. Router dient dazu, 
Links richtig zu stellen. 

### Router hinzufügen

Die offizielle Anweisung steht hier: https://angular.io/guide/router

Also: wir fügen das Modul in `app.module.ts` hinzu:

``` 
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {RouterModule, Routes} from "@angular/router";

const appRoutes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
```

Es gibt eine Route "home", die zu HomeComponent führt. Das Problem:
Es gibt noch kein HomeComponent. Das erledigen wir mit CLI:

``` 
ng generate component Home
```

### HomeComponent bearbeiten

Wir sehen, dass unter `src/app` ein Verzeichnis `home` erstellt 
worden ist. Dort befinden sich 4 Dateien zu home component. Wenn wir
jetzt in `app.component.html` irgendwo das Element `<app-home>` 
hinzufügt, sehen wir, dass diese Komponente dargestellt wird.

Wir wollen app-home aber als eine Unterseite haben. Deswegen müssen
wir in app.component.html einige Sachen ändern:

In der <nav> geben wir @routerLinkActive in <li> hinzu, damit wird 
dieses <li> mit einer Klasse "active" ausgestattet, wenn man auf
einen Link in <li> klickt. Und bei <a> geben wir @routerLink hinzu,
welche den Link zur Komponente anzeigt.

``` 
    <nav>
      <div class="col-md-12">
        <ul class="nav nav-pills">
          <li routerLinkActive="active"><a routerLink="/home">Willkommen</a></li>
          <li><a href="">Personen</a></li>
          <li><a href="">Projekte</a></li>
          <li><a href="">Bilder</a></li>
          <li routerLinkActive="active"><a routerLink="/impressum">Impressum</a></li>
        </ul>
      </div>
    </nav>

```

NB: @routerLink verlangt ein URL, d.h. es startet mit "/", in der
Route-Definition stehen die Routes ohne "/" da.

Wie man sieht habe ich nch ein ImpressumComponent hinzzgefügt.

In `app.component.html` müssen wir noch ein <router-outlet> hinzufügen.
Dort, wo dann die Komponente dargestellt wird, wenn man auf einen 
routerLink klickt.

``` 
    <main>
      <div id="mainView">
        <router-outlet></router-outlet>
      </div>
    </main>
```

Im ähnlichen Manieren können wir die anderen Komponenten schreiben.


### FooterComponent, mit Beispiel eines Services

In der Footer-Zeile will ich die Status-Zeile schreiben. Aus den 
Endpoints gibt es ja immer eine Message, und diese Message sollte,
v.a. bei Fehlermeldungen, angezeigt werden.


#### Idee

Es sollte ein "MessageService" geben, der die Schalt-Zentrale für 
Messages ist: Hat man eine Nachricht zu verschicken, sendet man diese
über eine Methode im MessageService, und empfangen kann man dann
diese Nachrichten, in dem man diese Nachricht-Kanal "abonniert."

Hierzu eignet sich die Observable-Subscriber-Pattern von RxJS, die
auch von Angular verwendet wird, ausgezeichnet. Die Messages sind 
"Observables", die man "subscriben" kann, um immer
die neueste Nachricht zu bekommen.


#### Erstellen des MessageService

AngularCLI macht das Erstellen von Service auch recht einfach:

``` 
ng generate service Message -m app
```

Im Gegensatz zu Komponenten fügt AngularCLI nicht automatisch die 
Services in `app.module.ts` ein. Das macht AngularCLI erst mit dem 
Schalter `-m app` (zum Modul app).

AngularCLI erstellt zwei Dateien in `app` Verzeichnis: 

* message.service.ts: Die Definition des MessageServices

* message.service.spec.ts: Die Tests zu dem Service

Wir verwenden die Klasse "Subject" von RxJS: Subject ist eine Art
"offene Kanal". Subject ist ein Observable, d.h. man kann seine
Inhalte abonnieren, aber mit "next()" Methode kann man auch Inhalte
hinzufügen.

Der Service schaut nun so aus:

``` 
import { Injectable } from '@angular/core';
import {Subject} from "rxjs/Subject";

@Injectable()
export class MessageService {
  
  public message:Subject<string>;

  constructor() {
    this.message = new Subject();
  }
  
  public setMessage(msg:string): void {
    this.message.next(msg);
  }

}
```

Im Konstruktor wird die Subject initiiert. Außerdem wollen wir
das Einbringen neuer Nachrichten erleichtern, in dem wir 
eine setMessage() Methode schreibt.


#### FooterComponent

Die Footer-Komponente aboniert den MessageService:

``` 
import {Component, OnDestroy, OnInit} from '@angular/core';
import {MessageService} from "../message.service";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit, OnDestroy {
  
  public message:string;
  
  private messageSubscription:Subscription;

  constructor(private messageService:MessageService) { }

  ngOnInit() {
    this.message = "Nachrichtensystem initiiert.";
    this.messageSubscription = this.messageService.message
      .subscribe(msg => this.message = msg);
  }
  
  ngOnDestroy(): void {
    this.messageSubscription.unsubscribe();
  }
}
```

Beim Konstruktor steht mit `private messageService:MessageService`, dass
Angular MessageService hier einbringen soll, und zwar als Feld von 
FooterComponent.

In der Initialisierungsmethode ngOnInit() initiieren wir die 
Statuszeile: Zunächst wird eine Default-Nachricht erstellt. Und dann 
wird die subscribe() Methode von `this.messageService.message` (= Feld 
message vom MessageService) aufgerufen: Der Syntax liest sich dann
so:

msg => this.message = msg 

Gegeben msg (die Nachricht von MessageService) => Stell this.message 
(= das Feld in der Footer-Komponente) gleich zu msg.

Statt `msg` kann man auch x schreiben. Dies ist nur der Platzhalter 
für das Observable.

Dank TypeScript wissen wir auch, von welchem Typ msg ist. Im 
MessageService steht, dass Message ein Subject<string> ist. D.h. 
dieses Observable liefert string als Inhalt.

Und im Template muss auch message ausgegeben werden:

``` 
<div class="col-md-12 text-center">
  <p>{{message}}</p>
</div>
```


#### Testen zum Spaß

Wir fügen in unserem MessageService einen Timer hinzu, der alle
5 Sekunden eine Zahl in Nachrichten-System schreibt:

``` 
  constructor() {
    this.message = new Subject();
    Observable.timer(5000,5000).subscribe(x => this.setMessage(x.toString()));
  }
```

Man muss natürlich auch die Imports verändern:

``` 
import {Observable} from "rxjs/Observable";
import "rxjs/add/observable/timer";
```

Und schon sieht man im Browserfenster, dass die Statuszeile alle 5 Sekunden
ein Mal ändert.


### REST-Endpoints ansprechen


#### Message-Klassen schreiben

Um die REST-Endpoints anzusprechen, soll man zunächst alle 
Austausch-Formate auch noch ein Mal als TypeScript-Klasse / Interface
definieren. Dies erleichtert das Schreiben der Routinen sehr:

`ng generate class msg/PersonFW`

Ich will, dass alle JSON-Message-Klassen in einem Verzeichnis verfügbar
sind: msg. Und diese Klasse wird gleich benannt wie jene in Java. 
(Das ist möglicherweise keine gute Idee...)

Und im Verzeichnis `app/msg/` gibt es nun eine person-fw.ts Datei, in
der die Definition der Klasse festgeschrieben ist:

``` 
export class PersonFW {

  public id:number;

  public name:string;

  public email:string;

  public info:string;

}
```

Man sieht, die Klasse ist eine Spiegelung der Java-Klasse PersonFW.
Wichtig dabei: Wir brauchen die Klassen von den entsprechenden Messages,
nicht jene der Domain-Objekte. Denn diese Klassenm dienen dem Austausch
von Nachrichten, nicht der Datenmodellierung.

In ähnlicher Weise werden die anderen Klassen geschrieben.

Man kann auch Interfaces schreiben. Aber Klassen haben den Vorteil,
dass sie mit einem Konstruktor erstellt werden können.


#### Eine Komponente schreiben, um die Personen aufzulisten

Generieren wir nun eine Komponente "Person", die als Person-Seite
dienen soll, und noch eine Unterkomponente, die in PersonComponent
verwendet wird, um die Personen aufzulisten.

``` 
ng generate component Person
ng generate component PersonList
```

PersonComponent soll eigentlich nur die Wahlmöglichkeiten für 
Person-CRUD-Operationen darstellen. PersonListComponent stellt
eine Liste aller Personen dar, die bearbeitet werden können.


#### PersonService

Der PersonService sollte die Kommunikation zwischen Endpoints und
Components ermöglichen. Mit `ng generate service Person -m app` 
können wir die Klasse erzeugen. Betrachten wir diese genauer:

``` 
import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MessageService} from "./message.service";
import {PersonFW} from "./msg/person-fw";
import {Subject} from "rxjs/Subject";
import {PersonListMsg} from "./msg/person-list-msg";

@Injectable()
export class PersonService implements OnInit {

  public personList:Subject<PersonFW[]>;

  constructor(private http:HttpClient, private messageService:MessageService) {
    this.personList = new Subject();
  }


  ngOnInit(): void {
    this.loadPersonList();
  }

  loadPersonList():void {
    this.http.get<PersonListMsg>("api/person")
      .subscribe(msg => {
        this.messageService.handleMessageResponse(msg);
        this.personList.next(msg.personList);
      }, error => {
        this.messageService.handleErrorResponse(error);
      });
  }
}
```

Neben den Import-Statements ist der erste Decorator @Injectable().
@Injectable() signalisiert, dass Angular andere Services auch in 
diese Klasse einbringen darf. 

Die Klasse hat im Moment drei Felder:

personList:Subject<PersonFW[]>: Hier ist die "manipulierbare" Observable
gespeichert.

http:HttpClient: Der HttpClient von Angular

messageService:MessageService: Unserer MessageService

Da http und messageService im Konstruktor stehen, werden diese auch
von Angular "detektiert" und eingebracht. Das Feld personList 
initiieren wir im Konstruktor, damit dieses Objekt bereit steht, wenn wir
es brauchen.

Die Klasse implementiert OnInit, deswegen führt die Angular auch
die Methode ngOnInit() aus, sobald die Klasse erstellt worden ist.

Außerdem hat die Klasse eine Methode: loadPersonList(). Die Methode
greift die Endpoint "api/person" mit GET zu.

``` 
  loadPersonList():void {
    this.http.get<PersonListMsg>("api/person")
      .subscribe(msg => {
        this.messageService.handleMessageResponse(msg);
        this.personList.next(msg.personList);
      }, error => {
        this.messageService.handleErrorResponse(error);
      });
  }
```

Dies geschieht in folgender Weise: Mit this.http.get() bekommt Angular
zunächst ein Observable für das Ergebnis der Http-Request. Wir sehen,
get() kann man mit Typen-Informationen ausstatten, was Code-Completion
ermöglicht. Wir sagen also: Greif auf "api/person" zu und erwarte
ein JSON-Objekt (im Body des Responses), das der Klasse
PersonListMsg entspricht.

Mit subscribe() weisen wir an, was mit dem Ergebnis zu tun ist: Das
erste Parameter von subscribe() sagt, was im Fall einer erfolgreichen
Abfrage zu tun ist: 1. messageService soll sich um Message kümmern, 
und 2. personList wird mit der msg.personList gefüttert.

Mit dem zweiten Parameter sagt man, was im Fall einer fehlerhaften
Abfrage zu tun ist. Hier soll sich messageService um den Response 
kümmern.


#### PersonListComponent

Gut, wir haben jetzt die Abfrage gemacht und eine Liste von Personen
zurückbekommen, die wir als solche an personList weiterreichen. Doch
wie können wir diese in HTML darstellen?

Schauen wir uns die PersonListComponent genauer an:

``` 
export class PersonListComponent implements OnInit, OnDestroy {

  public personList:PersonFW[];

  private personListSubscription;

  constructor(private personService:PersonService) { }

  ngOnInit() {
    this.personList = [];
    this.personListSubscription = this.personService.personList
      .subscribe(personList => this.personList = personList);
    this.personService.loadPersonList();
  }

  ngOnDestroy(): void {
    this.personListSubscription.unsubscribe();
  }
}
```

Hier gibt es zunächst eine "eigene" personList, die als [] initiiert
wird. Außerdem gibt es eine "Subscription", die wir behalten müssen,
um im Fall des Verlassens von PersonListComponent das Abonnoment wieder
kündigen zu können. In ngOnInit() abonnieren wir zunächst die Liste
und sagen dem Service dann, dass er die personList laden soll.

In `person-list.component.html` kann nun diese Liste dargestellt werden:

``` 
      <tr *ngFor="let person of personList">
        <td>{{person.id}}</td>
        <td>{{person.name}}</td>
        <td>{{person.email}}</td>
        <td>{{person.info}}</td>
      </tr>
```

Mit *ngFor wird über die Liste iteriert. Der Syntax sagt 
`let person of personList`, das bedeutet, in der Variable `person` 
soll jedes einzelne Element in der `personList` gespeichert werden.
Und die Darstellung der Elemente geschieht wie gewohnt in {{}} mit
Objekt-Verweis.


#### Router-Einstellung

Damit PersonListComponent aufgerufen wird, ergänzen wir die Route
in app.module.ts

``` 
const appRoutes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'person', component: PersonComponent,
    children: [
      { path: 'list', component: PersonListComponent},
      { path: '**', redirectTo: 'list' }
    ]},
  { path: 'impressum', component: ImpressumComponent },
  { path: '**', redirectTo: '/home' }
];
```

Im Pfad "person" soll PersonComponent verwendet werden, er hat jedoch
"Kinder": "list" verwendet PersonListComponent, und sonst wird auf "list"
weitergeleitet.


#### PersonEditComponent

Damit wir eine Person erstellen bzw. bearbeiten können, ergänzen
wir zunächst die entsprechende EndPoints im PersonService:

createPerson(), updatePerson() und deletePerson().

Damit wir Zugriff auf das <form> Element haben, fügen wir in 
`app.module.ts` das FormsModule hinzu:

``` 
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(appRoutes)
  ],
```

In `person-edit.component.html` fügen wir ein HTML-Formular ein,
der mit #personForm="ngForm" einen Namen hat:

``` 
    <form #personForm="ngForm">
      <table class="table">
        <tr>
          <td width="20%">ID</td>
          <td width="40%">{{person.id}}</td>
          <td width="40%">&nbsp;</td>
        </tr>
        <tr>
          <td><label for="nameInput">Name</label></td>
          <td>{{person.name}}</td>
          <td>
            <input class="form-control" id="nameInput" required [(ngModel)]="person.name"
                   name="name" #personName="ngModel" />
            <div *ngIf="personName.invalid && (personName.dirty || personName.touched)"
                 class="alert alert-danger">
              <div *ngIf="inputName.errors.required">
                Name muss sein.
              </div>
            </div>
          </td>
        </tr>
        ...
      </table>
    </form>
```

Mit `#name` kann man ein HTML-Element einen Namen geben, und dieses
dann in der Komponente mit @ViewChild("name") darauf verweisen. Deswegen
steht in `person-edit.component.ts` auch 
`@ViewChild("personForm") personForm:HTMLFormElement;` und man kann 
programmatisch dieses Element verändern, z.B. nach dem Abspeichern dieses
Formular zurücksetzen.

``` 
  savePerson() {
    if (this.person.id) {
      this.personService.updatePerson(this.person);
    } else {
      this.personService.createPerson(this.person);
      this.personService.setEditingModus(PersonEditingModus.EDIT);
    }
    this.personForm.reset();
  }
```

Auch die Validierung der Eingabe kann man über solche #Name erledigen.
Das <input> Element für den Namen bekommt mit #personName="ngModel"
einen Namen. Und die Validierungs-Hinweise kommen dann, wenn
`personName.invalid && (personName.dirty || personName.touched)` zutrifft.

Die Validierungshinweise kommen dann, wenn personName invalid ist und 
wenn diese Eingabefeld schon mal "berührt" worden ist, d.h. wenn die 
User die Möglichkeit schon hatte, dieses Feld zu bearbeiten.

Abspeichern kann man diese Formular dann, wenn die Daten verändert 
worden sind: 
`<button [disabled]="!personForm.dirty" (click)="savePerson()">Abspeichern</button>`
Das Attribut (click) besagt, wenn darauf geklickt wird, sollte
die Methode savePerson() ausgeführt werden.

Wie wir oben gesehen haben, ruft die Methode den Service auf und fügt
die Person hinzu bzw. aktualisiert die Daten.


#### RouterParameter verwenden

Wir ändern unseren Router und fügen bei "person" noch drei Kind-Routen
hinzu: 

``` 
      { path: 'create', component: PersonEditComponent},
      { path: 'edit/:id', component: PersonEditComponent},
      { path: 'edit', component: PersonEditComponent},
```

Wir sehen: Bei "create" und "edit" passiert eigentlich das gleiche.
Aber wenn es ein "id" hinter edit gibt, soll diese verwendet werden,
um eine bestimmte Person zu edieren. Wie macht man das?

Um einen sog. Router-Parameter zu erhalten, braucht man das Objekt
ActivatedRoute. Das kann man im Konstruktor von Angular geliefert
bekommen:

``` 
  constructor(private activatedRoute:ActivatedRoute, private personService:PersonService, private router:Router) { }

  ngOnInit() {
    this.person = {id:null, name:null, email:null, info:null, asLaborator:[], asLeader:[]};
    this.personSubscription = this.personService.personInDetail.subscribe(
      person => {
        this.person = person;
      }
    );
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => this.handleParams(params));
  }

  ngOnDestroy(): void {
    this.personSubscription.unsubscribe();
    this.paramsSubscription.unsubscribe();
  }

  handleParams(params:Params):void {
    let id = params['id'];
    if (id) {
      this.personService.loadPersonDetail(id);
    }
  }
```

Im Konstruktor wird das Objekt activatedRoute geliefert. Aus dem
lesen wir "params" heraus. Da params sich ändern könnten, wenn man
eine andere Route einschlägt, wird es als Observable geliefert.
Diese wird in ngOnInit() abonniert, und die eigentliche Parameter
werden in handleParams() bearbeitet. Das Abo wird in ngOnDestroy()
aufgelöst.

In handleParams() wird geprüft, ob es ein "id"-Parameter in dem
übergebenen Parameter gibt. Wenn ja, wird personService angewiesen,
diese Person zu laden. Das Feld person in personService wird als 
Subject (ähnlich wie personList)  geführt. Und so abonniert 
PersonEditComponent das Person-Feld in ngOnInit().

Gibt es ein ID-Parameter, lädt personService die Person und beliefert
die Daten an die Komponente. Die Komponente wird so verwendet, um
das Update von Personen verwendet. Gibt es keinen ID-Parameter,
wird die Komponente verwendet, um eine neue Person zu kreieren.

In öhnlicher Weise bearbeiten wir die Komponente für Projekte und
Bilder.


### Beziehungen edieren und NGX-Bootstrap und Modal

Eine Besonderheit gibt es beim Edieren von Projekten (und von Bildern).
Hier muss man eine aus der Personenliste den Leiter und die Mitarbeiter
bestimmen. Wie macht man das?


#### ManyToOne: Leiter auswählen.

Um einen Leiter auszuwählen (bzw. um das Feld leer zu lassen), gebrauchen
wir ein <select> Element, das am Anfang eine leere Option hat und dann
die Personen der ID nach auflistet.

Um überhaupt eine Liste von Personen zu haben, ist es notwendig, dass
die Komponente `PersonService` bekommt und dort die `personList` 
abonniert. So steht es im Konstruktor und ngOnInit() von 
`ProjectEditComponent`:

``` 
  constructor(private projectService:ProjectService, private activatedRoute:ActivatedRoute,
              private personServive:PersonService, private modalService:BsModalService,
              private router:Router) { }

  ngOnInit() {
    this.project = {id:null, name:null, goal:null, leader:null, laborators:[]};
    this.projectSubscription = this.projectService.project
      .subscribe(proj => {
        this.project = proj;
        this.myLeaderId = (proj.leader) ? proj.leader.id : 0;
      });
    this.paramsSubscription = this.activatedRoute.params.subscribe(params => this.handleParams(params));
    this.personList=[];
    this.personListSubscription = this.personServive.personList
      .subscribe(list => this.personList = list);
    this.personServive.loadPersonList();
  }

```

Da JS die Identität der Personen nicht wie in Java per der 
Implementierung von equals() und hashCode() Methoden erreichen kann,
muss man die Identität selbst herausfinden. Das bedeutet, dass
im Selekt-Feld `[(ngModel)]` nicht auf project.leader gestellt
werden kann, sondern muss auf ein künstlich von uns eingeführtes
Feld. In dem Fall: myLeaderId.Das Feld myLeaderId soll 0 sein, 
wenn kein Leader gesetzt ist, und sonst jene Nummer sein, die 
der ID von project.leader entspricht.

Das macht man, indem man myLeaderId zunächt mit 0 initiiert, und 
dann, wenn ein Projekt geladen wird, wird sie auf die ID von dem 
Projektleiter gesetzt.

Das <select> Element schaut dann so aus:

``` 
<select id="leaderInput" name="leader" [(ngModel)]="myLeaderId" (change)="setProjectLeader()">
  <option value="0">--</option>
  <option *ngFor="let person of personList" [value]="person.id">{{person.id}}: {{person.name}}</option>
</select>
```

Die erste Option ist 0 bzw. "--" und zeigt an, dass kein Leiter 
gesetzt worden ist. Die folgende Optionen sind der Liste von 
Personen entnommen. Mit `[value]="person.id"` setzt man @value 
immer auf die ID der Personen.

Bei der Änderung von dem <select> Element -- durch (change) 
gekennzeichent -- wird die Methode setProjectLeader() aufgerufen.

``` 
  setProjectLeader() {
    if (!this.project.leader || this.myLeaderId!==this.project.leader.id) {
      this.project.leader = null;
      for (let person of this.personList) {
        if (person.id==this.myLeaderId) {
          this.project.leader = person;
          return;
        }
      }
    }
  }
```

Hier wird den Leiter zunächst auf null gesetzt (gelöscht). Und dann
geht man die Liste der Personen durch, um jene Person zu finden,
deren ID myLeaderId entspricht. Und setzt dann project.leader auf
diese Person. Findet er keine Person mit der entsprechenden ID,
bleibt das Feld auf null.


#### ManyToMany: Modal verwenden.

Wie kann man die Personen verändern, die als Mitarbeiter vorkommen?

Die Idee ist, wenn man ein Modal-Fenster aufmacht, bei der man
einfach jene Personen einfach per Checkbox auswählen kann, die zum 
Team gehören. 

Um Modal verwenden zu können, verwenden wir die Angular- und 
Bootstrap-Erweiterung `ngx-bootstrap`. Hierzu installieren wir
die Erweiterung mit `npm install --save ngx-bootstrap` und warten,
bis die Installation fertig ist.

So dann steht uns einige Module zur Verfügung, die wir in 
`app.module.ts` einbinden können. Das Modal-Modul wird in imports
über `ModalModule.forRoot()` eingebunden, und muss auch über
`import {ModalModule} from "ngx-bootstrap";` importiert werden.

Ein Modalfenster wird über eine Methode in der Komponente
geöffnet:

```
  @ViewChild("personSelector") personSelectorTemplate:TemplateRef<any>;

  public personSelectorModal:BsModalRef;
 
  openPersonSelectorModal():void {
    this.personSelectorModal = 
        this.modalService.show(this.personSelectorTemplate);
  }
```

modalService ist ein BsModalService und wird von Angular durch den
Konstruktor eingebracht. Durch die Zuweisung zu einem Feld 
(personSelectorModal) kann man später auch auf darauf verweisen und
dieses Modal dann schließen. Bleibt noch personSelectorTemplate!

Das Feld "personSelectorTemplate" wird über die Annotation 
@ViewChild("#name") eingebracht. Die Annotation besagt, dass man jenes
Element mit dem Namen "name" in die Komponente einbringt. 
Ähnlich wie bei HTMLFormElement kann man auch ein Template mittels
"#name" kennzeichnen. Sehen wir uns das Template in der Datei
`project-edit.component.html` an:

``` 
  <ng-template #personSelector>
    <div class="modal-header">
      <h4 class="modal-title pull-left">Personen-Auswahl</h4>
      <button type="button" class="close pull-right" aria-label="Close" (click)="personSelectorModal.hide()">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="modal-body">
      <table class="table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>&nbsp;</th>
        </tr>
        </thead>
        <tbody>
        <tr *ngFor="let person of personList">
          <td>{{person.id}}</td>
          <td>{{person.name}}</td>
          <td>
            <input type="checkbox" [checked]="findIndexOfLaborator(person)!=-1"
                   (click)="toggleLaborator(person)" />
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </ng-template>

```

Wir sehen, ich habe das Template direkt von der Website von
ngx-bootstrap kopiert. Beim Schließ-Button sieht man, warum ich
dieses Modal ein Feld zugewiesen habe. Nun kann ich dieses Feld
in Component verwenden, um es zu schließen:
`(click)="personSelectorModal.hide()"` 

Die Tablle stellt alle Personen dar und fügt überall eine Checkbox
hinzu:

``` 
<input type="checkbox" [checked]="findIndexOfLaborator(person)!=-1"
       (click)="toggleLaborator(person)" />
```

Die beiden Methoden in der Komponente kann man leicht nachlesen. Die 
Methode `findIndexOfLaborator(person)` sucht nach der Index von
der Person. Ist die Person in project.laborators vorhanden, wird
die Index (startend von 0) zurückgegeben. Ist die Person nicht
in der Liste vorhanden, wird -1 zurück gegeben. Und mit
`toggleLaborator(person)` wird die Person zu laborators hinzugefügt 
oder von ihr gelöscht.


### FileUpload-Light: Über JSON Dateien hochladen

Es bleibt noch ein letzter Detail im PictureComponent: Datei-Upload. 
Hier wird der Weg geschildert, wie man über JSON-Requests die 
Datei auch die Datei hochlädt.

Voraussetzung dafür ist, dass beim File-Selektor ein Name angebracht
ist, der in der Komponente dann zur Verfügung steht:

``` 
<input type="file" #fileInput name="fileInput" (change)="readFile()" 
       class="form-control" />
```

In PictureEditComponent wird dann per @ViewChild() auf dieses Input-Element
zugegriffen.

``` 
@ViewChild("fileInput") fileInput:ElementRef;
```

Und so kann man dann, sobald dieses Input verändert wird (change), 
die Datei einlesen und zu Base64-String umwandeln:

``` 
  readFile():void {
    if (!this.fileInput.nativeElement.files) {
      console.log("No files found.");
      console.log(this.fileInput);
      return;
    }
    const files:File[] = this.fileInput.nativeElement.files;
    if (!files.length) {
      console.log("No file found.");
      return;
    }
    const reader = new FileReader();
    reader.onload = () => {
      let result:string = reader.result;
      let pattern:RegExp = /^data:(.*?);base64,(.*)/;
      let matcher = pattern.exec(result);
      this.picture.content = matcher[2];
      let imgType:string = matcher[1];
      if (imgType.indexOf("jpeg") != -1) {
        this.picture.type="JPEG";
      } else if (imgType.indexOf("tiff") != -1) {
        this.picture.type = "TIFF";
      } else if (imgType.indexOf("png") != -1) {
        this.picture.type = "PNG"
      } else {
        this.picture.type = "UNKNOWN";
      }
      this.picture.path = files[0].name;
      this.pictureChanged = true;
    };
    reader.readAsDataURL(files[0]);
  }

```

Mittels eines neuen FileReaders kann man die Daten in JavaScript
auslesen. Der FileReader funktioniert scheinbar so: mit 
readAsDataUrl(file) wird die Datei eingelesen und in result
bereitgestellt. Dann wird die Methode onload() von FileReader
ausgeführt, bzw. wenn Fehler auftaucht: onerror().

Deswegen schreiben wir zuerst in onload, was mit result zu
geschehen ist: Mittels RegExp wird der String analysiert:
die ersten Zeichen lauten immer "data:", dann kommt der MimeType,
und dann ";base64,", und danach der eigentliche Datei-Inhalt.


## Angular Kompilieren und dann SpringApp kompilieren.

Wenn man zufrieden ist mit dem Frontend, kann man über den Befehl

`ng build`

das Verzeichnis "src/main/resources/static" erstellen lassen. 
Danach kann man über 

`maven clean package`

das Projekt als JAR-Datei erstellen. Das Ergebnis, die Datei
`at.ac.uibk.fiba.wang.spring4uli.angular-0.0.1-SNAPSHOT.jar` 
kann man per 

`java -jar at.ac.uibk.fiba.wang.spring4uli.angular-0.0.1-SNAPSHOT.jar`

starten. (Vorher MySQL-Server starten und die Datenbank 
einrichten.) Dann kann man in Browser auf 

`http://localhost:56790/t06/` surfen. Das Ergebnis ist genau
gleich wie beim Dev-Server.


### Hash-Routing?

Leider unterstützt Spring von selbst noch nicht HTML5-Routen,
daher müsste man Hash-Routing verwenden, wenn man die URLs 
an der gleichen Stelle platzieren will. 

Also: Statt "http://localhost:56790/t06/person" müsste man dann
"http://localhost:56790/t06/#/person" schreiben. Hash schaut 
vielleicht nicht schön aus, dafür läuft diese Art von Routen 
überall. Die Einstellung geschieht in `app.module.ts`. Dort,
wo man die Pfade importiert, kann man die Option "useHash" auf
"true" stellen:

``` 
    RouterModule.forRoot(appRoutes, {useHash: true})
```

Dann bleibt der Link auch unter Spring okay.
