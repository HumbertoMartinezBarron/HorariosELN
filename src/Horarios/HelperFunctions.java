/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Horarios;

import java.util.*;
import conjuntos.*;
import java.io.*;

/**
 *
 * @author Humberto
 */
public class HelperFunctions {

    static int n = 99;
    static ArrayList<Curso> cursos = new ArrayList();
    static int[] trap = {0,0,0,1,1,2,2};
    static String[][] availabilities = {{"7:0","7:30"},
                                        {"7:30","8:0"},
                                        {"8:0","8:30"},
                                        {"8:30","9:0"},
                                        {"9:0","9:30"},
                                        {"9:30","10:0"},
                                        {"10:0","10:30"},
                                        {"10:30","11:0"},
                                        {"11:0","11:30"},
                                        {"11:30","12:0"},
                                        {"12:0","12:30"},
                                        {"12:30","13:0"},
                                        {"13:0","13:30"},
                                        {"13:30","14:0"},
                                        {"14:0","14:30"},
                                        {"14:30","15:0"}};
    static String[][][] bloques = {{{"7:00","8:00"},{"7:00","8:00"},{"7:00","8:00"}},
                                    {{"8:00","9:00"},{"8:00","9:00"},{"8:00","9:00"}}, 
                                    {{"9:00","10:00"},{"9:00","10:00"},{"9:00","10:00"}}, 
                                    {{"10:00","11:00"},{"10:00","11:00"},{"10:00","11:00"}}, 
                                    {{"11:00","12:00"},{"11:00","12:00"},{"11:00","12:00"}}, 
                                    {{"12:00","13:00"},{"12:00","13:00"},{"12:00","13:00"}}, 
                                    {{"13:00","14:00"},{"13:00","14:00"},{"13:00","14:00"}}, 
                                    {{"14:00","15:00"},{"14:00","15:00"},{"14:00","15:00"}}};
    /*static String[][][] bloques = {{{"7:00","7:50"},{"7:10","8:00"},{"7:30","8:20"}},
                                   {{"7:50","8:40"},{"8:00","8:50"},{"8:20","9:10"}},
                                   {{"8:55","9:45"},{"9:05","9:55"},{"9:25","10:15"}},
                                   {{"9:45","10:35"},{"9:55","10:45"},{"10:15","11:05"}},
                                   {{"10:35","11:25"},{"10:45","11:35"},{"11:05","11:55"}},
                                   {{"11:55","12:45"},{"11:35","12:25"},{"12:25","13:15"}},
                                   {{"12:45","13:35"},{"12:55","13:45"},{"13:15","14:05"}},
                                   {{"13:35","14:25"},{"13:45","14:35"},{"14:05","14:55"}}};*/
    /*static String[][][] bloques = {{{"7:00","8:00"},{"7:30","8:30"},{"8:00","9:00"}},
                                   {{"8:00","9:00"},{"8:30","9:30"},{"9:00","10:00"}},
                                   {{"9:00","10:00"},{"9:30","10:30"},{"10:00","11:00"}},
                                   {{"10:00","11:00"},{"10:30","11:30"},{"11:00","12:00"}},
                                   {{"11:00","12:00"},{"11:30","12:30"},{"12:00","13:00"}},
                                   {{"12:00","13:00"},{"12:30","13:30"},{"13:00","14:00"}},
                                   {{"13:00","14:00"},{"13:30","14:30"},{"14:00","15:00"}},
                                   {{"14:00","15:00"},{"14:30","15:30"},{"14:35","15:25"}}};*/
    
    public static void generaCursos()
    {
        String file = "/Users/Humberto/Downloads/Courses.csv";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            String[] data;
            while(line!=null)
            {
                data = line.split(",");
                cursos.add(new Curso(data[0],data[1],data[2]));
                line = br.readLine();
            }
        }
        catch(IOException e){System.out.println("No generados");}
    }
    
    public static double convertToComparableTime(String time)
    {
        String[] sep = time.split(":");
        double fraction = Double.parseDouble(sep[1])/60;
        double hr = Double.parseDouble(sep[0]) + fraction;
        return hr/24;
    }
    
    public static double intersectTimeRanges(String[] range1, String[] range2)
    {
        double[] s = new double[2]; double[] e = new double[2];
        s[0] = convertToComparableTime(range1[0]);
        e[0] = convertToComparableTime(range1[1]);
        s[1] = convertToComparableTime(range2[0]);
        e[1] = convertToComparableTime(range2[1]);
        int i1 = 0;if(e[0]<e[1]) i1=1;
        int i2 = 0;if(s[0]>s[1]) i2=1;
        return (e[0]-s[0]+e[1]-s[1])-(e[i1]-s[i2]);
    }
    
    public static ArraySet block(ArrayList<Integer> bin, int bloque)
    {
        ArraySet block = new ArraySet<Integer>();
        int av;String[] times,bl;
        for(int i=0; i<bin.size(); i++)
        {
            av = bin.get(i);
            times = availabilities[av];
            for(int j=0;j<bloques.length;j++) 
            {
                bl = bloques[j][bloque];
                if(intersectTimeRanges(bl,times)>0.0035) {block.add(j);}
            }
        }
        return block;
    }
    
    public static void agregaProhibido(String file, ArrayList<Curso> cursos) 
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("/Users/Humberto/Downloads/"+file));
            String line = br.readLine();
            String[] data;
            while(line!=null)
            {
                data = line.split(",");
                ArrayList<Integer> bin = new ArrayList();
                for(int i=1; i<data.length; i++)
                    if(data[i].equals("0"))
                        bin.add(i-1);
                for(int i=0;i<cursos.size();i++)
                {
                    Curso c = cursos.get(i);
                    int semestre = Integer.parseInt(""+c.getGrupo().charAt(0));
                    int bloque = trap[semestre];
                    ArraySet toBlock = block(bin, bloque);
                    if(data[0].equals(c.getProfesor())) 
                    {
                        c.prohibido.addAll(toBlock);
                        cursos.set(i, c);
                    }
                }
                line = br.readLine();
            }
        }
        catch(Exception e){System.out.println("No generados.\n"+e.toString());}
    }
    
    public static ArrayList<Curso> selectCourses(String[] names, int param)
    {
        ArrayList<Curso> query = new ArrayList();
        String[] params;
        for(String name : names)
        {
            for(Curso c : cursos) 
            {
                params = c.getParams();
                if(params[param].equals(name))
                    query.add(c);
            }
        }
        return query;
    }
    
    public static ArrayList<Curso> selectCourses(String[] names1, String[] names2, int[] param)
    {
        ArrayList<Curso> query = new ArrayList();
        String[] params;
        boolean b;
        for(String n1 : names1)
            for(String n2 : names2)
                for(Curso c : cursos)
                {
                    params = c.getParams();
                    b = (params[param[0]].equals(n1) && params[param[1]].equals(n2));
                    if(b) query.add(c);
                }
        return query;
    }
    
    public static ArrayList[][][] intersectBlocks(String[][][] b, int[][] costs)
    {
        ArrayList[][][] matrix = new ArrayList[b.length][b[0].length][2];
        //costs = new int[b.length][b[0].length];
        String[] range1, range2;
        for(int i=0; i<b.length; i++)
            for(int j=0; j<b[0].length; j++)
            {
                matrix[i][j][0] = new ArrayList<Integer>();
                matrix[i][j][1] = new ArrayList<Integer>();
                matrix[i][j][0].add(i);matrix[i][j][1].add(j);
                costs[i][j] = 1;
            }
        for(int i=0; i<b.length; i++)
            for(int j=0; j<b[0].length-1; j++)
            {
                range1 = b[i][j];
                for(int k=0; k<b.length; k++)
                    for(int l=j+1; l<b[0].length; l++)
                    {
                        range2 = b[k][l];
                        if(intersectTimeRanges(range1,range2)>0.0035)
                        {
                            matrix[i][j][0].add(k);matrix[i][j][1].add(l);
                            matrix[k][l][0].add(i);matrix[k][l][1].add(j);
                            costs[i][j]++;costs[k][l]++;
                        }
                    }
            }
        return matrix;
    }
    
    public static void resetNodes()
    {
        for(int i=0;i<cursos.size();i++)
        {
            cursos.get(i).prohibido=new ArraySet();
            cursos.get(i).relaciones=new ArraySet();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int[][]costs=new int[bloques.length][bloques[0].length];
        ArrayList[][][] matrix = intersectBlocks(bloques, costs);
        String s="Matriz de costos para secciones y bloques:\n";
        for(int i=0;i<bloques.length;i++)
        {
            for(int j=0;j<bloques[0].length;j++)
                s+=costs[i][j]+" ";
            s+="\n";
        }
        System.out.println(s);
        generaCursos();
        
        // Lunes
        agregaProhibido("MondayAvailability.csv", cursos);
        String []clasesLunes = {"Quimica1", "Matematicas1", "Fisica1", "Matematicas3",
                                "CalculoDiferencial", "Informatica1", "DesarrolloHumano1",
                                "Geografia", "Biologia1", "TemasSelectosDeFisica1", 
                                "EstructuraSocioeconomicaDeMexico", "MarcoNormativoDeMexico", 
                                "DesarrolloHumano3", "HistoriaDeMexico1", "Literatura1", 
                                "MetodologiaDeLaInvestigacion", "Etica1", "TemasSelectosDeQuimica1",
                                "LenguajeFinanciero3", "DIN3", "DesarrolloHumano5.1", 
                                "CienciasDeLaSalud1", "DesarrolloDeInversores5"};
        // Selecciona por nombre de materia
        ArrayList<Curso> cursosLunes = selectCourses(clasesLunes, 0);
        System.out.println("Cursos a asignar: "+cursosLunes.size());
        HazHorarios lunes = new HazHorarios(cursosLunes, 3, bloques.length, true, matrix, costs, bloques);
        lunes.hazRelaciones();
        lunes.resuelve();
        String []horarioLunes = lunes.imprime();
        System.out.println("Bloque 1:\n"+horarioLunes[0]+"\nBloque2:\n"+horarioLunes[1]+"\nBloque3:\n"+horarioLunes[2]);
    }
    
}
