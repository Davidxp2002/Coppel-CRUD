import { Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
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
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import { Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';  
import { BrowserModule } from '@angular/platform-browser';
import Swal from 'sweetalert2';
import { ChequeoService } from 'src/app/service/chequeo.service';


@Component({
  selector: 'app-justificar',
  templateUrl: './justificar.component.html',
  styleUrls: ['./justificar.component.css'],
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatIconModule, MatButtonModule,    FormsModule,     MatPaginatorModule,
    ReactiveFormsModule,    MatTooltipModule,    MatCardModule,
    MatTableModule, HttpClientModule, CommonModule, MatDialogModule],
})
export class JustificarComponent {
  justificacion: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<JustificarComponent>,
    public dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public content: any,
    private datosService: ChequeoService,
  ){
    this.justificacion = new FormGroup({
      idChequeo: new FormControl('',Validators.required),
      justificacion: new FormControl('', Validators.required)
    })
  }

  justificar(){
    if(this.justificacion.value.idChequeo != '' && this.justificacion.value.justificacion != '')
    this.datosService.putJustificar(this.justificacion.value.idChequeo, this.justificacion.value.justificacion).subscribe((response: any) => {
      if(response.success == false){
        Swal.fire({
          title: "No se puedo realizar la justificación",
          text: response.message,
          confirmButtonColor: "#00008b",
          icon: "error",
        });
      }else{
        Swal.fire({
          title: "Justificación exitosa",
          text: "Se realizó la justificación exitosamente",
          confirmButtonColor: "#008000",
          icon: "success",
        });
      }
      this.dialogRef.close();
      //console.log('endpoint: ', this.data)
      //console.log("¿qué respondió el endpoint? ", this.information);
    })
  }
}
