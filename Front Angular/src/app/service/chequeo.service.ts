import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment.development';
import * as moment from 'moment';

@Injectable({
  providedIn: 'root'
})
export class ChequeoService {
  private url = environment.baseUrl;
  private headersjson = new HttpHeaders({ 'Content-Type': 'application/json' });
  constructor(private http: HttpClient) { }

  postGetChequeosListById(pagina: number, cantidad: number, idUsuario: number) {
    return this.http.post(this.url+"api/v1/chequeos/getById", JSON.stringify({
      "paginaActual": pagina,
      "registrosPagina": cantidad,
      "idUsuario": idUsuario
    }), {responseType: "json", headers: this.headersjson })
  }

  postChecar(idUsuario: number){
    console.log(moment(new Date().getTime()).format('YYYY-MM-DD') + ' ' + moment(new Date().getTime()).format('HH:mm:ss'));
    return this.http.post(this.url+"api/v1/chequeos/save", JSON.stringify({
      "idUsuario": idUsuario,
      "horaChequeo": Date.now()
    }), {responseType: "json", headers: this.headersjson })
  }

  putJustificar(idChequeo: number, justificacion: String){
    return this.http.put(this.url+"api/v1/chequeos/justificar", JSON.stringify({
      "idChequeo": idChequeo,
      "justificacion": justificacion
    }), {responseType: "json", headers: this.headersjson })
  }
}
