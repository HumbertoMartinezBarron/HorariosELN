/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Horarios;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
/**
 *
 * @author Humberto
 */
public class Prueba {
    
    public static void writeCSV(String[] data, String day)
    {
        try(PrintWriter writer = new PrintWriter(new File(day+"Results.csv")))
        {
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<data.length;i++)
            {
                sb.append("Bloque ");
                sb.append(i+1);
                sb.append(":\n");
                sb.append(data[i]);
                sb.append("\n");
            }
            writer.write(sb.toString());
            System.out.println("Wrote "+day+" CSV file.");
        }
        catch(FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[][][] bloques = HelperFunctions.bloques;
        int[][] costs = new int[bloques.length][bloques[0].length];
        ArrayList[][][] matrix = HelperFunctions.intersectBlocks(bloques, costs);
        String[]grupo=new String[1];String path="/Users/Humberto/Downloads/";
        
        // Lunes
        System.out.println("Lunes");
        HelperFunctions.generaCursos();
        String []clasesLunes = {"Quimica1", "Matematicas1", "Fisica1", "Matematicas3",
                                "CalculoDiferencial", "Informatica1", "DesarrolloHumano1",
                                "Biologia1", "TemasSelectosDeFisica1", //"Geografia",
                                "MarcoNormativoDeMexico", "HistoriaDeMexico1", 
                                "Literatura1", "DIN3", "LenguajeFinanciero3",
                                "LeguajeFinanciero1yTLR1", "EstructuraSocioeconomicaDeMexico"};
        ArrayList<Curso> cursosLunes = HelperFunctions.selectCourses(clasesLunes, 0);
        String[] clase={"DesarrolloHumano5.2"};String[]grupos={"5C","5B"};int[]params={0,2};
        cursosLunes.addAll(HelperFunctions.selectCourses(clase, grupos, params));
        clase[0]="DesarrolloDeInversores1";grupo[0]="1A";
        cursosLunes.addAll(HelperFunctions.selectCourses(clase, grupo, params));
        clase[0]="TemasSelectosDeQuimica1";grupos[0]="5C";grupos[1]="5A";
        cursosLunes.addAll(HelperFunctions.selectCourses(clase, grupos, params));
        clase[0]="MetodologiaDeLaInvestigacion";grupos[0]="1A";grupos[1]="1B";
        cursosLunes.addAll(HelperFunctions.selectCourses(clase, grupos, params));
        clase[0]="Etica1";grupo[0]="1C";
        cursosLunes.addAll(HelperFunctions.selectCourses(clase, grupo, params));
        //clase[0]="DesarrolloDeInversores5";grupo[0]="5B";
        //cursosLunes.addAll(HelperFunctions.selectCourses(clase, grupo, params));
        clase[0]="DesarrolloHumano5.1";grupos[0]="5A";grupos[1]="5B";
        cursosLunes.addAll(HelperFunctions.selectCourses(clase, grupos, params));
        HelperFunctions.agregaProhibido(path+"MondayAvailability.csv", cursosLunes);
        System.out.println("Cursos a asignar: "+cursosLunes.size());
        HazHorarios lunes = new HazHorarios(cursosLunes, 3, bloques.length, true, matrix, costs, bloques);
        lunes.hazRelaciones();
        lunes.resuelve();
        String[] horariosLunes = lunes.imprime();
        System.out.println("Bloque 1:\n"+horariosLunes[0]+"\nBloque 2:\n"+horariosLunes[1]+"\nBloque 3:\n"+horariosLunes[2]);
        //writeCSV(horariosLunes,"Monday");
        
        // Martes
        System.out.println("Martes");
        HelperFunctions.resetNodes();
        String []clasesMartes = {"Quimica1", "Matematicas1", "Fisica1", "Matematicas3",
                                "CalculoDiferencial", "DesarrolloDeInversores1",
                                "Biologia1", "DesarrolloHumano1", "Geografia",
                                "EstructuraSocioeconomicaDeMexico", "MarcoNormativoDeMexico", 
                                "DesarrolloHumano3", "HistoriaDeMexico1", "Literatura1", 
                                "Etica1", "DesarrolloDeInversores3", "LeguajeFinanciero1yTLR1",
                                "CienciasDeLaSalud1", "LenguajeFinanciero5"};
        ArrayList<Curso> cursosMartes = HelperFunctions.selectCourses(clasesMartes, 0);
        clase[0]="DesarrolloHumano5.2";grupo[0]="5C";
        cursosMartes.addAll(HelperFunctions.selectCourses(clase, grupo, params));
        clase[0]="MetodologiaDeLaInvestigacion";grupo[0]="1A";
        cursosMartes.addAll(HelperFunctions.selectCourses(clase, grupo, params));
        clase[0]="TemasSelectosDeQuimica1";grupos[0]="5C";grupos[1]="5B";
        cursosMartes.addAll(HelperFunctions.selectCourses(clase, grupos, params));
        cursosMartes.add(new Curso("DesarrolloDeInversores1-2","Manuel Villagomez Levi","1C"));
        HelperFunctions.agregaProhibido(path+"TuesdayAvailability.csv", cursosMartes);
        System.out.println("Cursos a asignar: "+cursosMartes.size());
        HazHorarios martes = new HazHorarios(cursosMartes, 3, bloques.length, true, matrix, costs, bloques);
        martes.hazRelaciones();
        martes.resuelve();
        String[] horariosMartes = martes.imprime();
        System.out.println("Bloque 1:\n"+horariosMartes[0]+"\nBloque 2:\n"+horariosMartes[1]+"\nBloque 3:\n"+horariosMartes[2]);
        //writeCSV(horariosMartes,"Tuesday");
        
        // Miercoles
        System.out.println("Miercoles");
        HelperFunctions.resetNodes();
        String []clasesMiercoles = {"Quimica1", "Matematicas1", "Fisica1", "Matematicas3",
                                "CalculoDiferencial", "Informatica1",
                                "Biologia1", "TemasSelectosDeFisica1", 
                                "MarcoNormativoDeMexico", 
                                "DesarrolloHumano3", "HistoriaDeMexico1", "Literatura1", 
                                "DesarrolloDeInversores3",
                                "LeguajeFinanciero1yTLR1", "DesarrolloHumano5.1",
                                "LenguajeFinanciero5"};
        ArrayList<Curso> cursosMiercoles = HelperFunctions.selectCourses(clasesMiercoles, 0);
        clase[0]="DesarrolloDeInversores1";grupos[0]="1A";grupos[1]="1B";
        cursosMiercoles.addAll(HelperFunctions.selectCourses(clase, grupos, params));
        clase[0]="TemasSelectosDeQuimica1";grupo[0]="5B";
        cursosMiercoles.addAll(HelperFunctions.selectCourses(clase, grupo, params));
        clase[0]="MetodologiaDeLaInvestigacion";grupos[0]="1C";grupos[1]="1B";
        cursosMiercoles.addAll(HelperFunctions.selectCourses(clase, grupos, params));
        clase[0]="Etica1";grupo[0]="1A";
        cursosMiercoles.addAll(HelperFunctions.selectCourses(clase, grupo, params));
        clase[0]="MarcoNormativoDeMexico";grupos[0]="5B";grupos[1]="5A";
        HelperFunctions.agregaProhibido(path+"WednesdayAvailability.csv", cursosMiercoles);
        System.out.println("Cursos a asignar: "+cursosMiercoles.size());
        HazHorarios miercoles = new HazHorarios(cursosMiercoles, 3, bloques.length, true, matrix, costs, bloques);
        miercoles.hazRelaciones();
        miercoles.resuelve();
        String[] horariosMiercoles = miercoles.imprime();
        System.out.println("Bloque 1:\n"+horariosMiercoles[0]+"\nBloque 2:\n"+horariosMiercoles[1]+"\nBloque 3:\n"+horariosMiercoles[2]);
        //writeCSV(horariosMiercoles,"Wednesday");
        
        // Jueves
        System.out.println("Jueves");
        HelperFunctions.resetNodes();
        String []clasesJueves = {"Quimica1", "Matematicas1", "Fisica1", "Matematicas3",
                                "Informatica1", "Geografia", "Biologia1", "DesarrolloHumano3",
                                "DesarrolloHumano1", "CienciasDeLaSalud1", "LenguajeFinanciero3",
                                "DesarrolloDeInversores3", "EstructuraSocioeconomicaDeMexico",
                                "LenguajeFinanciero5", "TemasSelectosDeFisica1"};
        ArrayList<Curso> cursosJueves = HelperFunctions.selectCourses(clasesJueves, 0);
        clase[0] = "CoachingDeACO";String[]prof={"Manuel Levi Villagomez"};params[1]=1;
        cursosJueves.addAll(HelperFunctions.selectCourses(clase, prof, params));
        clase[0]="MetodologiaDeLaInvestigacion";grupos[0]="1A";grupos[1]="1C";params[1]=2;
        cursosJueves.addAll(HelperFunctions.selectCourses(clase, grupos, params));
        clase[0]="DesarrolloDeInversores1";grupos[0]="1B";grupos[1]="1C";
        cursosJueves.addAll(HelperFunctions.selectCourses(clase, grupos, params));
        clase[0]="TemasSelectosDeQuimica1";grupos[0]="5B";grupos[1]="5C";
        cursosJueves.addAll(HelperFunctions.selectCourses(clase, grupos, params));
        clase[0]="DesarrolloDeInversores5";grupos[0]="5C";grupos[1]="5A";
        cursosJueves.addAll(HelperFunctions.selectCourses(clase, grupos, params));
        HelperFunctions.agregaProhibido(path+"ThursdayAvailability.csv", cursosJueves);
        System.out.println("Cursos a asignar: "+cursosJueves.size());
        HazHorarios jueves = new HazHorarios(cursosJueves, 3, bloques.length, true, matrix, costs, bloques);
        jueves.hazRelaciones();
        jueves.resuelve();
        String[] horariosJueves = jueves.imprime();
        System.out.println("Bloque 1:\n"+horariosJueves[0]+"\nBloque 2:\n"+horariosJueves[1]+"\nBloque 3:\n"+horariosJueves[2]);
        //writeCSV(horariosJueves,"Thursday");
        
        // Viernes
        System.out.println("Viernes");
        HelperFunctions.resetNodes();
        String []clasesViernes = {"Matematicas1", "Matematicas3", "DIN5", "DesarrolloDeInversores5",
                                  "CienciasDeLaSalud1", "TemasSelectosDeQuimica1"};
        ArrayList<Curso> cursosViernes = HelperFunctions.selectCourses(clasesViernes, 0);
        clase[0] = "CoachingDeACO";prof[0]="Norberto Ponce Venga";params[1]=1;
        cursosViernes.addAll(HelperFunctions.selectCourses(clase, prof, params));
        clase[0]="MetodologiaDeLaInvestigacion";grupos[0]="1B";grupos[1]="1C";params[1]=2;
        cursosViernes.addAll(HelperFunctions.selectCourses(clase, grupo, params));
        HelperFunctions.agregaProhibido(path+"FridayAvailability.csv", cursosViernes);
        System.out.println("Cursos a asignar: "+cursosViernes.size());
        HazHorarios viernes = new HazHorarios(cursosViernes, 3, bloques.length, true, matrix, costs, bloques);
        viernes.hazRelaciones();
        viernes.resuelve();
        String[] horariosViernes = viernes.imprime();
        System.out.println("Bloque 1:\n"+horariosViernes[0]+"\nBloque 2:\n"+horariosViernes[1]+"\nBloque 3:\n"+horariosViernes[2]);
        //writeCSV(horariosViernes,"Friday");
    }
    
}
