import { Component, ViewChild } from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatButtonModule} from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTable, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatPaginatorIntl, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ChequeoService } from '../service/chequeo.service';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import { Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';  
import { BrowserModule } from '@angular/platform-browser';
import Swal from 'sweetalert2';
import { JustificarComponent } from '../dialogs/justificar/justificar.component';
import { RelojService, valorReloj } from '../classes/reloj';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatIconModule, MatButtonModule,    FormsModule,     MatPaginatorModule,
    ReactiveFormsModule,    MatTooltipModule,    MatCardModule,
    MatTableModule, HttpClientModule, CommonModule, MatDialogModule,],
})
export class HomeComponent {
  datos$?: Observable<valorReloj>;
  hora?: number;
  minutos?: string;
  dia?: string;
  fecha?: string;
  ampm?: string;
  segundos?: string;
  information: any[] = [];
  displayedColumns: string[] = ['Id', 'Tipo', 'Estatus', 'Hora Chequeo', 'Justificación'];
  data: any;

  totalRegistros: number = 0;
  nombreUsuario: String = '';
  indicePagina: number = 1;
  registrosPorPagina: number = 10;

  filterList: FormGroup;

  constructor(
    private datosService: ChequeoService,
    public _MatPaginatorIntl: MatPaginatorIntl,
    public dialog: MatDialog,
    private segundo: RelojService
    
  ) {
    this.filterList = new FormGroup({
      idUsuario: new FormControl('', Validators.required)
    })
  }
  
  @ViewChild(MatTable) table!: MatTable<any>;
  ngOnInit(): void {
    this.datos$=this.segundo.getInfoReloj();
    this.datos$.subscribe(x => {
      this.hora = x.hora;
      this.minutos = x.minutos;
      this.dia = x.diadesemana;
      this.fecha = x.diaymes;
      this.ampm = x.ampm;
      this.segundos = x.segundo
    });
    this._MatPaginatorIntl.itemsPerPageLabel = "Registros por página"
    this._MatPaginatorIntl.getRangeLabel = (page: number, pageSize: number, length: number) =>  {
      if (length === 0 || pageSize === 0) {
        return `0 / ${length}`;
      }
      length = Math.max(length, 0);
      const startIndex = page * pageSize;
      const endIndex = startIndex < length ? Math.min(startIndex + pageSize, length) : startIndex + pageSize;
      return `${startIndex + 1} - ${endIndex} de  ${length}`;
    }
  }

  listar(){
    if(this.filterList.value.idUsuario != '')
    this.cargarListado(this.indicePagina, this.registrosPorPagina, this.filterList.value.idUsuario);
  }

  cargarListado(pagina: number, cantidad: number, idUsuario: number) {
    this.nombreUsuario = '';
    this.datosService.postGetChequeosListById(pagina, cantidad, idUsuario).subscribe((response: any) => {
      this.data = response;
      this.information = response.checadas;
      for(let k = 0; k < this.information.length; k++){
        this.totalRegistros = this.information[k].registros;
        this.nombreUsuario = this.information[k].nombreUsuario;
        this.information[k].horachequeo = new Date(this.information[k].horachequeo).toLocaleString()
      }
      this.table.renderRows();
      if(response.success == false){
        Swal.fire({
          title: "Error",
          text: response.message,
          confirmButtonColor: "#00008b",
          icon: "error",
        });
      }
      //console.log('endpoint: ', this.data)
      //console.log("¿qué respondió el endpoint? ", this.information);
    })
  }

  cambiarPagina(pageEvent: PageEvent) {
    this.indicePagina = pageEvent.pageIndex + 1;
    this.registrosPorPagina = pageEvent.pageSize;
    this.listar();
  }

  checar(){
    if(this.filterList.value.idUsuario != '')
    this.datosService.postChecar(this.filterList.value.idUsuario).subscribe((response: any) => {
      if(response.success == false){
        Swal.fire({
          title: "Error al checar",
          text: response.message,
          confirmButtonColor: "#00008b",
          icon: "error",
        });
      }else{
        Swal.fire({
          title: "Checada exitosa",
          text: response.message,
          confirmButtonColor: "#008000",
          icon: "success",
        });
        this.listar();
      }
      //console.log('endpoint: ', this.data)
      //console.log("¿qué respondió el endpoint? ", this.information);
    })
  }

  justificar(){
    const dialogRef = this.dialog.open(JustificarComponent, {
      width: '650px',
    });
    dialogRef.afterClosed().subscribe(response => {
      this.listar();
    })
  }
}
