import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {RouterModule, Routes} from "@angular/router";
import { HomeComponent } from './home/home.component';
import { ImpressumComponent } from './impressum/impressum.component';
import { FooterComponent } from './footer/footer.component';
import { MessageService } from './message.service';
import { PersonComponent } from './person/person.component';
import { PersonListComponent } from './person-list/person-list.component';
import {PersonService} from "./person.service";
import {HttpClientModule} from "@angular/common/http";
import { PersonEditComponent } from './person-edit/person-edit.component';
import {FormsModule} from "@angular/forms";
import { ProjectComponent } from './project/project.component';
import { ProjectListComponent } from './project-list/project-list.component';
import { ProjectEditComponent } from './project-edit/project-edit.component';
import { PictureComponent } from './picture/picture.component';
import { PictureListComponent } from './picture-list/picture-list.component';
import { PictureEditComponent } from './picture-edit/picture-edit.component';
import { ProjectService } from './project.service';
import {PictureService} from "./picture.service";
import {ModalModule} from "ngx-bootstrap";

const appRoutes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'person', component: PersonComponent,
    children: [
      { path: 'list', component: PersonListComponent},
      { path: 'create', component: PersonEditComponent},
      { path: 'edit/:id', component: PersonEditComponent},
      { path: 'edit', component: PersonEditComponent},
      { path: '**', redirectTo: 'list' }
    ]},
  { path: 'project', component: ProjectComponent,
    children: [
      { path: 'list', component: ProjectListComponent},
      { path: 'create', component: ProjectEditComponent},
      { path: 'edit/:id', component: ProjectEditComponent},
      { path: 'edit', component: ProjectEditComponent},
      { path: '**', redirectTo: 'list' }
    ]},
  { path: 'picture', component: PictureComponent,
    children: [
      { path: 'list', component: PictureListComponent},
      { path: 'create', component: PictureEditComponent},
      { path: 'edit/:id', component: PictureEditComponent},
      { path: 'edit', component: PictureEditComponent},
      { path: '**', redirectTo: 'list' }
    ]},
  { path: 'impressum', component: ImpressumComponent },
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ImpressumComponent,
    FooterComponent,
    PersonComponent,
    PersonListComponent,
    PersonEditComponent,
    ProjectComponent,
    ProjectListComponent,
    ProjectEditComponent,
    PictureComponent,
    PictureListComponent,
    PictureEditComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ModalModule.forRoot(),
    RouterModule.forRoot(appRoutes, {useHash: true})
  ],
  providers: [MessageService, PersonService, ProjectService, PictureService],
  bootstrap: [AppComponent]
})
export class AppModule { }
