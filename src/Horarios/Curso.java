/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Horarios;

import conjuntos.*;
/**
 *
 * @author humberto
 */
public class Curso 
{
    private String nombre;
    private String profesor;
    private String grupo;
    
    public ArraySet<Integer> prohibido;
    public ArraySet<Curso> relaciones;
    
    private int horario;
    
    public Curso(String nombre, String profesor, String grupo)
    {
        this.nombre=nombre;
        this.profesor=profesor;
        this.grupo=grupo;
        prohibido = new ArraySet();
        relaciones = new ArraySet();
    }
    
    public void setNombre(String nombre)
    {
        this.grupo=nombre;
    }
    
    public void setProfesor(String profesor)
    {
        this.profesor=profesor;
    }
    
    public void setGrupo(String grupo)
    {
        this.grupo=grupo;
    }
    
    public void setHorario(int h)
    {
        horario = h;
    }
    
    public int getHorario()
    {
        return horario;
    }
    
    public String getNombre()
    {
        return nombre;
    }
    
    public String getProfesor()
    {
        return profesor;
    }
    
    public String getGrupo()
    {
        return grupo;
    }
    
    public void agregaRelacion(Curso nuevo)
    {
        relaciones.add(nuevo);
    }
    
    public void prohibe(int horario)
    {
        prohibido.add(horario);
    }
    
    public int compareTo(Curso b)
    {
        return relaciones.size() - b.relaciones.size();
    }
    
    public boolean equals(Curso b)
    {
        return b.getNombre().equals(nombre) 
                && b.getProfesor().equals(profesor) 
                && b.getGrupo().equals(grupo);
    }
    
    public String[] getParams()
    {
        String[] params = {nombre, profesor, grupo};
        return params;
    }
    
    @Override
    public String toString()
    {
        return nombre+" : "+profesor+" : "+grupo;
    }
}
