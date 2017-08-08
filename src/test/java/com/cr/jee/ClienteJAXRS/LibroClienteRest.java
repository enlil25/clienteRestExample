package com.cr.jee.ClienteJAXRS;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LibroClienteRest {
	
	private final String PATH_GET_LISTADOPAISES = "https://restcountries.eu/rest/v2/all";
		
	private final String PATH_POST_NUEVOLIBRO = "http://localhost:8080/capitulo15-jaxrs-todojunto/rs/libro";
	
	private final String PATH_GET_LISTADOLIBROS = "http://localhost:8080/capitulo15-jaxrs-todojunto/rs/libro";
	
	private final String PATH_GET_OBTENERLIBRO_POR_ID = "http://localhost:8080/capitulo15-jaxrs-todojunto/rs/libro/{id}";

	private final String PATH_PUT_ACTUALIZAR_LIBRO = "http://localhost:8080/capitulo15-jaxrs-todojunto/rs/libro";
	
	private final String PATH_DELETE_REMOVER_LIBRO = "http://localhost:8080/capitulo15-jaxrs-todojunto/rs/libro/{id}";

	private final String PATH_GET_LISTADO_EXAMPLE= "https://jsonplaceholder.typicode.com/photos";
	
	public void listadoPaises() {

		Client client = ClientBuilder.newClient();
		Pais[] paises = client
				 .target("https://restcountries.eu/rest/v2/all")
				 .request()
				 .get(Pais[].class);
		System.out.println("Linea siguiente");
		for (Pais pais : paises) {
			System.out.println(pais.getName());
		}
	}

	public void listadoLibros() {
		Client client = ClientBuilder.newClient();
		Libro[] libros = client
				.target("http://localhost:8080/capitulo15-jaxrs-todojunto/rs/libro")
				.request()
				.get(Libro[].class);

		List<Libro> listado = Arrays.asList(libros);

		System.out.println(listado.size());

		for (Libro libro : libros) {
			System.out.println(libro.toString());
		}
	}
	
	public void listadoAsincrono() {
		
		Client client = null;
		Future<Pais[]> future;
		
		try{
			client = ClientBuilder.newClient();
			future=client.target("https://restcountries.eu/rest/v2/all")
			      .request()
			      .async()
			      .get(Pais[].class);
			
			Pais[] paises =future.get();
			for(Pais pais : paises){
				System.out.println(pais.getName());
			}	
		}catch(ExecutionException e){
			e.printStackTrace();
		}catch(InterruptedException e){
			e.printStackTrace();
			
		}
		
		
	}
	
	public void listadoAsincronoConCallback(){
		Client client = null;
		
		client = ClientBuilder.newClient();
		client.target(this.PATH_GET_LISTADOPAISES)
		      .request()
		      .async()
		      .get(new InvocationCallback<Pais[]>(){

				@Override
				public void completed(Pais[] paises) {
					for(Pais pais:paises){
						System.out.println(pais.getName());
					}
				}

				@Override
				public void failed(Throwable throwable) {
					// TODO Auto-generated method stub
					System.out.println("Peticion fallo");
				}
		    	  
		    	  
		      });
			
			System.out.println("Linea siguiente");
			
		
	}
	
	/**
	 * Crea libro mediante metodo post
	 */
	public void crearLibroPost(){
		
		Libro libro = new Libro();
		libro.setTitulo("libro post");
		libro.setDescripcion("descripcion libro post");
		libro.setPrecio(120.00f);
		
		Client client = ClientBuilder.newClient();
		Response response = client.target(this.PATH_POST_NUEVOLIBRO)
		                          .request()
		                          .post(Entity.entity(libro, MediaType.APPLICATION_JSON));
		     
		System.out.println("status:"+response.getStatus());
		System.out.println("location:"+response.getLocation());
		System.out.println("status info"+response.getStatusInfo());
	}
	
	public void crearLibroPostAsincrono(){
		
		Libro libro = new Libro();
		libro.setTitulo("libro post");
		libro.setDescripcion("descripcion libro post");
		libro.setPrecio(120.00f);
		
		Client client = null;
		Future<Response> future=null;
		
		
		
		try {
			client = ClientBuilder.newClient();
			future = client.target(this.PATH_POST_NUEVOLIBRO)
			      .request()
			      .async()
			      .post(Entity.entity(libro, MediaType.APPLICATION_JSON));
			

			Response response = future.get();
			System.out.println("status:"+response.getStatus());
			System.out.println("location:"+response.getLocation());
			
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void crearLibroPostAsincronoConCallback(){
		Libro libro = new Libro();
		libro.setTitulo("libro post");
		libro.setDescripcion("descripcion libro post");
		libro.setPrecio(120.00f);
		
		Client client = null;
		Future<Response> future = null;
		
		client = ClientBuilder.newClient();
		client.target(this.PATH_POST_NUEVOLIBRO)
		      .request()
		      .async()
		      .post(Entity.entity(libro, MediaType.APPLICATION_JSON), new InvocationCallback<Response>(){

				@Override
				public void completed(Response response) {
					// TODO Auto-generated method stub
					System.out.println("completado");
					System.out.println("status:"+response.getStatus());
					System.out.println("location:"+response.getLocation());
				}

				@Override
				public void failed(Throwable arg0) {
					// TODO Auto-generated method stub
					System.out.println("peticion fallo");
				}
		    	  
		      });
		
		System.out.println("siguiente linea");
		
	}
	
	public void actualizarLibro(){
		Long id=1L;
		Libro libro = new Libro();
		libro.setId(id);
		libro.setTitulo("libro post modificado");
		libro.setDescripcion("descripcion libro post modificado");
		libro.setPrecio(1200.00f);
		
		
		
		Client client = ClientBuilder.newClient();
		Response response = client.target(this.PATH_PUT_ACTUALIZAR_LIBRO)
		      .request()
		      .put(Entity.entity(libro, MediaType.APPLICATION_JSON));
	    System.out.println("status:"+response.getStatus());
	    System.out.println("statusinfo:"+response.getStatusInfo());
	}
	
	public void actualizarLibroAsincrono(){
		Long id=1L;
		Libro libro = new Libro();
		libro.setId(id);
		libro.setTitulo("libro post modificado 2 2");
		libro.setDescripcion("descripcion libro post modificado 22");
		libro.setPrecio(1200.00f);
		
		Client client=null;
		Future<Response> future=null;
		
		
		
		try {
			client = ClientBuilder.newClient();
			future = client.target(this.PATH_PUT_ACTUALIZAR_LIBRO)
			      .request()
			      .async()
			      .put(Entity.entity(libro, MediaType.APPLICATION_JSON));
			
			Response response = future.get();
			System.out.println("Status:"+response.getStatus());
			System.out.println("Status info:"+response.getStatusInfo());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void actualizarLibroAsincronoConCallback(){
		Long id=1L;
		Libro libro = new Libro();
		libro.setId(id);
		libro.setTitulo("libro post modificado");
		libro.setDescripcion("descripcion libro post modificado");
		libro.setPrecio(1200.00f);
		
		Client client = ClientBuilder.newClient();
		client.target(this.PATH_PUT_ACTUALIZAR_LIBRO)
		      .request()
		      .async()
		      .put(Entity.entity(libro, MediaType.APPLICATION_JSON), new InvocationCallback<Response>(){

				@Override
				public void completed(Response response) {
					// TODO Auto-generated method stub
					System.out.println("completado");
				    System.out.println("status:"+response.getStatus());
				    System.out.println("status info:"+response.getStatusInfo());
				}

				@Override
				public void failed(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}
		    	  
		      });
		
		System.out.println("Siguiente linea");
	
	}
	
	public void eliminarLibroAsincronoConCallback(){
		Long id = 3L;
		
		Client client = ClientBuilder.newClient();
		client.target(this.PATH_DELETE_REMOVER_LIBRO)
		      .resolveTemplate("id", id)
		      .request()
		      .async()
		      .delete(new InvocationCallback<Response>(){

				@Override
				public void completed(Response response) {
					// TODO Auto-generated method stub
					System.out.println("completado");
					System.out.println("status:"+response.getStatus());
					System.out.println("status info:"+response.getStatusInfo());
				}

				@Override
				public void failed(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}
		    	  
		      });
	  System.out.println("siguiente linea");
	}
	
	
	public void obtenerLibro(){
		Client client = ClientBuilder.newClient();
		Libro libro =client.target(this.PATH_GET_OBTENERLIBRO_POR_ID)
		      .resolveTemplate("id", 1)
		      .request()
		      .get(Libro.class);
		System.out.println(libro.toString());      
	}
	
	public void obtenerFotosAsincronicamente(){
		Client client = ClientBuilder.newClient();
	   	client.target(this.PATH_GET_LISTADO_EXAMPLE)
		      .request()
		      .async()
		      .get(new InvocationCallback<Foto[]>(){

				@Override
				public void completed(Foto[] fotos) {
					// TODO Auto-generated method stub
					for(Foto foto : fotos){
				   		System.out.println(foto.getTitle());
				   	}
				}

				@Override
				public void failed(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}
		    	  
		    	  
		      });
	   System.out.println("Siguiente linea");
	   
	   	
	}
	
	
	
	public void obtenerFotos(){
		Client client = ClientBuilder.newClient();
	   	Foto[] fotos = client.target(this.PATH_GET_LISTADO_EXAMPLE)
		      .request()
		      .get(Foto[].class);
	   	for(Foto foto : fotos){
	   		System.out.println(foto.getTitle());
	   	}
	   System.out.println("Siguiente linea");
	   
	   	
	}
	
	

	public static void main(String[] args) {
		
		LibroClienteRest clienterest = new LibroClienteRest();
		clienterest.obtenerFotosAsincronicamente();
	}
	
}
