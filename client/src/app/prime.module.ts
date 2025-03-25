import { NgModule } from "@angular/core";
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { MessageModule } from 'primeng/message';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { PaginatorModule } from 'primeng/paginator';
import { AccordionModule } from 'primeng/accordion';
import { PanelModule } from 'primeng/panel';

@NgModule({
    exports: [
        FloatLabelModule,
        InputTextModule,
        ButtonModule,
        CardModule,
        DividerModule,
        MessageModule,
        AutoCompleteModule,
        PaginatorModule,
        AccordionModule,
        PanelModule
    ]
})

export class PrimeModule { }