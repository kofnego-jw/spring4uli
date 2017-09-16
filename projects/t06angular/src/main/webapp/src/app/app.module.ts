import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import {RouterModule, Routes} from "@angular/router";
import { HomeComponent } from './home/home.component';
import { ImpressumComponent } from './impressum/impressum.component';
import { FooterComponent } from './footer/footer.component';
import { MessageService } from './message.service';

const appRoutes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'impressum', component: ImpressumComponent },
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ImpressumComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [MessageService],
  bootstrap: [AppComponent]
})
export class AppModule { }
