/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Horarios;

import java.util.*;
import conjuntos.*;

/**
 *
 * @author humberto
 */
public class HazHorarios 
{
    public ArrayList<Curso> nodos;
    private ArraySet<String> profesores;
    private ArraySet<String> grupos;
    private ArraySet [] bloques;
    private int numSalones;
    public ArrayList[] horarios;
    private ArrayList<Curso> aux;
    private int limiteHorarios;
    private String[][][] hs;
    private ArrayList [][][] matrix;
    private int [][] blocked;
    private boolean xSemestre;
    private int[] trap={0,0,0,1,1,2,2};
    
    public HazHorarios(ArrayList<Curso> nodos, int numSalones, int limiteHorarios, boolean xSemestre, ArrayList[][][] matrix, int[][] blocked, String[][][] h)
    {
        this.nodos = nodos;
        this.numSalones = numSalones;
        horarios = new ArrayList[blocked[0].length];
        for(int i=0; i<blocked[0].length; i++) {
            horarios[i] = new ArrayList<Integer>();
            horarios[i].add(0);
        }
        aux = new ArrayList();
        profesores = new ArraySet();
        grupos = new ArraySet();
        this.limiteHorarios = limiteHorarios;
        this.xSemestre = xSemestre;
        hs = h;
        this.matrix = matrix;
        this.blocked = blocked;
    }
    
    public void initTensor()
    {
        matrix[0][1][0].add(1);matrix[0][1][1].add(0);
        matrix[0][2][0].add(1);matrix[0][2][1].add(0);matrix[0][2][0].add(1);matrix[0][2][1].add(1);
        matrix[1][0][0].add(0);matrix[1][0][1].add(1);matrix[1][0][0].add(0);matrix[1][0][1].add(2);
        matrix[1][1][0].add(0);matrix[1][1][1].add(2);
        matrix[1][2][0].add(2);matrix[1][2][1].add(0);
        matrix[2][0][0].add(1);matrix[2][0][1].add(2);
        matrix[2][1][0].add(3);matrix[2][1][1].add(0);
        matrix[2][2][0].add(3);matrix[2][2][1].add(0);matrix[2][2][0].add(3);matrix[2][2][1].add(1);
        matrix[3][0][0].add(2);matrix[3][0][1].add(1);matrix[3][0][0].add(2);matrix[3][0][1].add(2);
        matrix[3][1][0].add(2);matrix[3][1][1].add(2);matrix[3][1][0].add(4);matrix[3][1][1].add(0);
        matrix[3][2][0].add(4);matrix[3][2][1].add(0);matrix[2][2][0].add(4);matrix[2][2][1].add(1);
        matrix[4][0][0].add(3);matrix[4][0][1].add(1);matrix[4][0][0].add(3);matrix[4][0][1].add(2);
        matrix[4][1][0].add(3);matrix[4][1][1].add(2);
        matrix[4][2][0].add(5);matrix[4][2][1].add(1);
        matrix[5][1][0].add(4);matrix[5][1][1].add(2);matrix[5][1][0].add(6);matrix[5][1][1].add(0);matrix[5][1][0].remove(2);matrix[5][1][1].remove(2);
        matrix[5][2][0].add(1);matrix[5][2][1].add(0);matrix[5][2][0].add(1);matrix[5][2][1].add(1);matrix[5][2][0].remove(1);matrix[5][2][1].remove(1);
        matrix[6][0][0].add(5);matrix[6][0][1].add(2);
        matrix[6][1][0].add(5);matrix[6][1][1].add(2);matrix[6][1][0].add(7);matrix[6][1][1].add(0);
        matrix[6][2][0].add(7);matrix[6][2][1].add(0);matrix[6][2][0].add(7);matrix[6][2][1].add(1);
        matrix[7][0][0].add(6);matrix[7][0][1].add(1);matrix[7][0][0].add(6);matrix[7][0][1].add(2);
        matrix[7][1][0].add(6);matrix[7][1][1].add(2);
    }
    
    public void relaciona(ArrayList<Integer> comunes)
    {
        for(int i=0; i<comunes.size(); i++)
        {
            Curso nodo = nodos.get(comunes.get(i));
            for(int j=i+1; j<comunes.size(); j++)
            {
                Curso siguiente = nodos.get(comunes.get(j));
                nodo.agregaRelacion(siguiente);
                siguiente.agregaRelacion(nodo);
            }
        }
    }
    
    public void hazRelaciones()
    {
        for(Curso nodo : nodos)
        {
            profesores.add(nodo.getProfesor());
            grupos.add(nodo.getGrupo());
        }
        Iterator it = profesores.iterator();
        while(it.hasNext())
        {
            String prof = it.next().toString();
            ArrayList<Integer> comunes = new ArrayList();
            for(Curso nodo: nodos)
                if(nodo.getProfesor().equals(prof))
                    comunes.add(nodos.indexOf(nodo));
            relaciona(comunes);
        }
        it = grupos.iterator();
        while(it.hasNext())
        {
            String grup = it.next().toString();
            ArrayList<Integer> comunes = new ArrayList();
            for(Curso nodo: nodos)
                if(nodo.getGrupo().equals(grup))
                    comunes.add(nodos.indexOf(nodo));
            relaciona(comunes);
        }
    }
    
    public Curso obtenMax()
    {
        Curso max = null;
        int i = 0;
        while(i < nodos.size() && nodos.get(i).relaciones.size() != 0)
        {
            /*  La heuristica ahora se rige por los horarios prohibidos y por 
                el numero de relaciones. */
            if(max == null || max.relaciones.size() + max.prohibido.size() < nodos.get(i).relaciones.size() + nodos.get(i).prohibido.size()) 
                max = nodos.get(i);
            i++;
        }
        if(i < nodos.size()) max = nodos.get(i);
        return max;
    }
    
    public Curso obtenMax(int bloque)
    {
        Curso max = null;
        int i = 0;
        while(i < nodos.size() && nodos.get(i).relaciones.size() != 0)
        {
            /*  La heuristica ahora se rige por los horarios prohibidos y por 
                el numero de relaciones. */
            Curso nodo = nodos.get(i);
            if(obtenBloque(nodo)==bloque && (max==null 
                    || max.relaciones.size() + max.prohibido.size() < nodo.relaciones.size() + nodo.prohibido.size()))
                max = nodo;
            i++;
        }
        if(i < nodos.size() && nodos.get(i).relaciones.size() == 0 && 
                obtenBloque(nodos.get(i))==bloque) 
            max = nodos.get(i);
        return max;
    }
    
    public int obtenBloque(Curso actual) 
    {
        int i;
        if(xSemestre) i = Integer.parseInt(actual.getGrupo().charAt(0)+"");
        else {
            switch(actual.getGrupo().charAt(1)) {
                case 'A': i=1;break;
                case 'B': i=3; break;
                default: i=5;break;
            }
        }
        return trap[i];
    }
    
    public void asigna() throws Exception
    {
        int j;
        int i;
        // Tratar de resolver para todos los bloques al mismo tiempo.
        //Curso actual = obtenMax();
        //j = obtenBloque(actual);
        
        /* Tratar de asignar los cursos por bloques  
           (solo funciona si xSemestre=true). */
        j=0; Curso actual = obtenMax(j);
        while(actual==null) {
            j++;
            actual=obtenMax(j);
        }
        
        // Tratar de encontrar los horarios donde se puede poner el curso actual.
        ArrayList<Integer> posibles = new ArrayList();
        for(i=0;i<limiteHorarios;i++) {
            if(!actual.prohibido.contains(i) && (i>=horarios[j].size() 
                    || (int)horarios[j].get(i) < numSalones)) {
                posibles.add(i);
            }
        }
        if(posibles.isEmpty()) // Si no se encuentra ninguno, truena.
        {
            System.out.println("El curso " + actual.getNombre() + 
                    "\nimpartido por " + actual.getProfesor() + 
                    "\nal grupo " + actual.getGrupo() + 
                    "\nno puede ser asignado de manera óptima. " + 
                    "\nSe requieren más salones o más horarios.");
            throw new Exception();
        }
        /* Busca el horario menos conflictivo posible para asignarlo.
            Q: usar heuristica que busque el mas conflictivo?
            A: Parece ser util cuando se resuelve cada bloque individualmente
               pero cuando se resuelve todo al mismo tiempo resulta perjudicial
               porque la asignacion resulta ser menos uniforme */
        while(horarios[j].size()<i+1)
        {
            horarios[j].add(0);
        }
        int h = Integer.parseInt(horarios[j].get(posibles.get(0)).toString());
        i=posibles.get(0);
        /*for (int k=1;k<posibles.size();k++) 
        {
            int y = posibles.get(k);
            if(blocked[y][j]<h) {
                i=y;
                h = blocked[y][j];
            }
        }
        int k=0;
        while(k<posibles.size() && posibles.get(k)<horarios[j].size())
        {
            int y = posibles.get(k);
            if(Integer.parseInt(horarios[j].get(y).toString())<h)
            {
                i=y;
                h = Integer.parseInt(horarios[j].get(y).toString());
            }
            k++;
        }*/
        
        // Asignar el curso al horario encontrado.
        int n = Integer.parseInt(horarios[j].get(i).toString()) + 1;
        horarios[j].set(i, n);
        actual.setHorario(i);
        
        /* Actualizar los nodos relacionados prohibiendo el lugar del nodo
            actual y otros bloqueos y eliminando la realcion con el nodo recien
            asignado */
        for (Curso nodo : actual.relaciones) 
        {
            ArrayList<Integer> secs = matrix[i][j][0];
            ArrayList<Integer> bloqs = matrix[i][j][1];
            int bloque = obtenBloque(nodo);
            for(int g=0; g<secs.size(); g++)
                if(bloqs.get(g)==bloque)
                    nodo.prohibido.add(secs.get(g));
            nodo.relaciones.remove(actual);
            if(nodos.contains(nodo)) {
                nodos.remove(nodo);nodos.add(nodo);
            }
        }
        nodos.remove(actual);
        aux.add(actual);
    }
    
    public void resuelve()
    {
        while(!nodos.isEmpty())
        {
            try
            {
                asigna();
            }
            catch(Exception e)
            {
                System.out.println("No se pudo asignar un curso.\n"
                    + aux.size() + " cursos asignados antes del fallo.");
                //System.out.println(e.getClass().toString());
                int j=0; Curso actual = obtenMax(j);
                while(actual==null) {
                    j++;
                    actual=obtenMax(j);
                }
                nodos.remove(actual);
            }
        }
    }
    
    public String[] imprime()
    {
        String [] ss = new String[blocked[0].length];
        try
        {
            for(int k=0; k<blocked[0].length; k++)
            {
                String s = "";
                for(int i=0; i<blocked.length; i++)
                {
                    s+= hs[i][k][0] +":, ";
                    for(int j=0; j<aux.size(); j++)
                    {
                        Curso ac = aux.get(j);
                        if(obtenBloque(ac)==k && ac.getHorario() == i) 
                            s+= ac.toString()+", ";
                    }
                    s+="\n";
                }
                ss[k] = s;
            }
        }
        catch(Exception e){System.out.println("No se pudo imprimir\n"
                +e.getClass().toString());}
        return ss;
    }
}