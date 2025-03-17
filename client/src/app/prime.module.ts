import { NgModule } from "@angular/core";
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { MessageModule } from 'primeng/message';
import { AutoCompleteModule } from 'primeng/autocomplete';

@NgModule({
    exports: [
        FloatLabelModule,
        InputTextModule,
        ButtonModule,
        CardModule,
        DividerModule,
        MessageModule,
        AutoCompleteModule,
    ]
})

export class PrimeModule { }