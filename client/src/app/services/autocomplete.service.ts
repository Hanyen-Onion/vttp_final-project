import { EventEmitter, inject, Injectable } from "@angular/core";
import { AutocompleteResult } from "../models";
import { HttpClient } from "@angular/common/http";
import { firstValueFrom } from "rxjs";

Injectable()

export class AutoCompleteService {

    private http = inject(HttpClient)

    airportSelected = new EventEmitter<AutocompleteResult>()
    //@ts-ignore
    private autocompletes: Map<string, google.maps.places.Autocomplete> = new Map();

    async initializeAutocomplete(inputElements: HTMLInputElement[]): Promise<void> {
        try {
          // Step 1: Get API key
          const apiKey = await this.getGoogleApiKey();
          console.log('API key retrieved successfully');
          
          // Step 2: Load Google Maps script
          await this.loadGoogleMapScript(apiKey);
          console.log('Google Maps script loaded');
          
          // Step 3: Initialize places library and setup autocompletes
          await this.init(inputElements);
          console.log('Autocomplete initialized for all inputs');
          
          return;
        } catch (error) {
          console.error('Failed to initialize autocomplete:', error);
          throw error;
        }
      }

    async init(inputElements: HTMLInputElement[]) {

        //@ts-ignore
        await google.maps.importLibrary("places")

        const promises = inputElements.map(input => this.setup(input));
        await Promise.all(promises);
    
        return this.autocompletes;
    }

    async setup(inputElement: HTMLInputElement) {
        
        if (!inputElement || !inputElement.id) {
            console.error('Input element does not have id');
            return
        }

        //@ts-ignore
        const autocomplete = new window.google.maps.places.Autocomplete(inputElement, {
            fields: ['name','place_id','id'],
            types:['airport'],
            includedPrimaryTypes: ['airport','international_airport']
        })

        this.autocompletes.set(inputElement.id, autocomplete)

        // Add place_changed event listener
        autocomplete.addListener('place_changed', () => {
            const place = autocomplete.getPlace()
            
            this.airportSelected.emit({
              inputName: inputElement.id,
              airport: place.name
            })
        })

        return autocomplete;
    }

    async getGoogleApiKey(): Promise<string> {
        const resp = await firstValueFrom(this.http.get<{apiKey:string}>('/api/google-maps-key'))
        return resp.apiKey
    }

    async loadGoogleMapScript(apiKey:string) {

      const script = document.createElement('script');
      script.innerHTML = `
        (g=>{var h,a,k,p="The Google Maps JavaScript API",c="google",l="importLibrary",q="__ib__",m=document,b=window;b=b[c]||(b[c]={});var d=b.maps||(b.maps={}),r=new Set,e=new URLSearchParams,u=()=>h||(h=new Promise(async(f,n)=>{await (a=m.createElement("script"));e.set("libraries",[...r]+"");for(k in g)e.set(k.replace(/[A-Z]/g,t=>"_"+t[0].toLowerCase()),g[k]);e.set("callback",c+".maps."+q);a.src=\`https://maps.\${c}apis.com/maps/api/js?\`+e;d[q]=f;a.onerror=()=>h=n(Error(p+" could not load."));a.nonce=m.querySelector("script[nonce]")?.nonce||"";m.head.append(a)}));d[l]?console.warn(p+" only loads once. Ignoring:",g):d[l]=(f,...n)=>r.add(f)&&u().then(()=>d[l](f,...n))})({
          key: "${apiKey}",
          v: "beta",
        });
      `;
      document.head.appendChild(script);
    }

    // getAutocomplete(inputName: string) {
    //     return this.autocompletes.get(inputName);
    // }
    
}