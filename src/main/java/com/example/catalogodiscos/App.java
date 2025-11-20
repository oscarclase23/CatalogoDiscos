package com.example.catalogodiscos;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.File;

public class App {
    
    private Document documento;
    private Element raizDiscos;
    private int contadorID = 0;

    public App() {
        try {
            DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
            DocumentBuilder constructor = fabrica.newDocumentBuilder();
            documento = constructor.newDocument();
            
            raizDiscos = documento.createElement("discos");
            documento.appendChild(raizDiscos);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarDisco(String titulo, String grupo, int anio, int mes, int dia, String genero, String[] canciones) {
        contadorID++;
        
        Element album = documento.createElement("album");
        album.setAttribute("id", String.valueOf(contadorID));
        
        Element tituloElem = documento.createElement("titulo");
        tituloElem.setTextContent(titulo);
        album.appendChild(tituloElem);
        
        Element grupoElem = documento.createElement("grupo");
        grupoElem.setTextContent(grupo);
        album.appendChild(grupoElem);
        
        Element fecha = documento.createElement("fecha");
        Element anhoElem = documento.createElement("anho");
        anhoElem.setTextContent(String.valueOf(anio));
        fecha.appendChild(anhoElem);
        Element mesElem = documento.createElement("mes");
        mesElem.setTextContent(String.valueOf(mes));
        fecha.appendChild(mesElem);
        Element diaElem = documento.createElement("dia");
        diaElem.setTextContent(String.valueOf(dia));
        fecha.appendChild(diaElem);
        album.appendChild(fecha);
        
        Element generoElem = documento.createElement("genero");
        generoElem.setTextContent(genero);
        album.appendChild(generoElem);
        
        Element cancionesElem = documento.createElement("canciones");
        for (int i = 0; i < canciones.length; i++) {
            Element cancion = documento.createElement("cancion");
            cancion.setAttribute("pista", String.valueOf(i + 1));
            cancion.setTextContent(canciones[i]);
            cancionesElem.appendChild(cancion);
        }
        album.appendChild(cancionesElem);
        
        raizDiscos.appendChild(album);
    }

    public void listarAlbumes() {
        NodeList albums = documento.getElementsByTagName("album");
        
        for (int i = 0; i < albums.getLength(); i++) {
            Element album = (Element) albums.item(i);
            String numero = album.getAttribute("id");
            String titulo = album.getElementsByTagName("titulo").item(0).getTextContent();
            NodeList canciones = album.getElementsByTagName("cancion");
            
            System.out.println("==Álbum (" + numero + ")== " + titulo);
            System.out.println("Número de canciones: " + canciones.getLength());
            
            for (int j = 0; j < canciones.getLength(); j++) {
                String tituloCancion = canciones.item(j).getTextContent();
                System.out.println("-" + tituloCancion);
            }
            System.out.println();
        }
    }

    public void guardarEnDisco(String nombreArchivo) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            
            DOMSource source = new DOMSource(documento);
            StreamResult result = new StreamResult(new File(nombreArchivo));
            transformer.transform(source, result);
            
            System.out.println("Archivo guardado: " + nombreArchivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarPorPantalla() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            
            DOMSource source = new DOMSource(documento);
            StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        App gestor = new App();
        
        for (int i = 1; i <= 25; i++) {
            String titulo = "Disco " + i;
            String grupo = "Grupo " + i;
            int anio = 2000 + i;
            int mes = (i % 12) + 1;
            int dia = (i % 28) + 1;
            String genero = "Género " + i;
            
            String[] canciones = new String[3];
            for (int j = 1; j <= 3; j++) {
                canciones[j - 1] = "Canción " + j + " del disco " + i;
            }
            
            gestor.agregarDisco(titulo, grupo, anio, mes, dia, genero, canciones);
        }
        
        System.out.println("=== LISTADO DE ÁLBUMES ===\n");
        gestor.listarAlbumes();
        
        System.out.println("\n=== XML COMPLETO ===\n");
        gestor.mostrarPorPantalla();
        
        gestor.guardarEnDisco("discos.xml");
    }
}