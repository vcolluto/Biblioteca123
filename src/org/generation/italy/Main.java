/*Partendo dalla tabella libri che avete creato con Luca, scrivere un programma che ne consente la gestione.

All'avvio del programma vengono caricati in un'ArrayList tutti i libri eventualmente presenti nel database (creare una classe model per i libri)
Proporre all'utente un menu di scelta con le seguenti opzioni:
Inserimento nuovo libro (in caso di chiave primaria non autoincrementante gestire eventuali duplicati)
Visualizzazione libri esistenti (eventualmente prevedere dei filtri per titolo, autore, ...)
Cancellazione libro (chiedere all'utente di inserire il valore della chiave primaria, poi cancellare)
Modifica libro (chiedere all'utente di inserire il valore della chiave primaria, e se il libro esiste chiedere le informazioni aggiornate)
Esci*/

package org.generation.italy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.generation.italy.model.Libro;


public class Main {

	public static void main(String[] args) {
		ArrayList<Libro> elencoLibri = new ArrayList<Libro>();
		String url = "jdbc:mysql://localhost:3306/biblioteca"; // stringa di connessione (in questo caso per MySql, ma
		Libro l;											// potrebbe essere diversa per altre tipologie di DBMS)

		String sql;
		// caricamento libri
		sql = "SELECT * FROM libri INNER JOIN autori on libri.id_autore=autori.id INNER JOIN generi on libri.id_genere=generi.id";
		try (Connection conn = DriverManager.getConnection(url, "root", "jaita101")) { // provo a connettermi
			try (PreparedStatement ps = conn.prepareStatement(sql)) { // provo a creare l'istruzione sql
				try (ResultSet rs = ps.executeQuery()) { // il ResultSet mi consente di scorrere il risultato della
															// SELECT una riga alla volta

					// scorro tutte le righe
					while (rs.next()) { // rs.next() restituisce true se c'è ancora qualche riga da leggere, falso
										// altrimenti
						l = new Libro();
						l.id = rs.getInt("id"); // recupero il valore della colonna "id"
						l.titolo = rs.getString("titolo");
						l.annoPubblicazione = rs.getInt("anno_pubblicazione");
						l.idAutore = rs.getInt("id_autore");
						l.idGenere = rs.getInt("id_genere");
						l.autore = rs.getString("autori.nome")+ " "+rs.getString("cognome") ;
						l.genere = rs.getString("generi.nome");
						elencoLibri.add(l);
					}
				}
			}
			for (Libro lib:elencoLibri)
				System.out.println(lib.toString());
		}
		catch (Exception e) {	//catch che gestisce tutti i tipi di eccezione (deve essere l'ultimo catch)
			//si è verificato un problema. L'oggetto e (di tipo Exception) contiene informazioni sull'errore verificatosi
			
			System.err.println("Si è verificato un errore: "+e.getMessage());
			//System.err.println("Stacktrace:");
			//e.printStackTrace();
			
		}
	}

}
