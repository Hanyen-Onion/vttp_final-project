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
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputNumberModule } from 'primeng/inputnumber';

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
        PanelModule,
        CalendarModule,
        DropdownModule,
        RadioButtonModule,
        InputNumberModule

    ]
})

export class PrimeModule { }