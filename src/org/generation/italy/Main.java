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
import java.util.Optional;
import java.util.Scanner;

import org.generation.italy.model.Libro;

public class Main {
	// dichiaro le variabili comuni a tutti i metodi
	static String url = "jdbc:mysql://localhost:3306/biblioteca"; // stringa di connessione
	static String username = "root";
	static String password = "jaita101";

	public static void main(String[] args) {
		ArrayList<Libro> elencoLibri = new ArrayList<Libro>();

		Libro l; // potrebbe essere diversa per altre tipologie di DBMS)
		String scelta;
		Scanner sc = new Scanner(System.in);
		int righeInteressate, idLibro;
		caricaLibri(elencoLibri);
		do {
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n**** GESTIONE BIBLIOTECA *****\n");
			System.out.println("1. Inserimento nuovo libro");
			System.out.println("2. Visualizzazione libri");
			System.out.println("3. Cancellazione libro");
			System.out.println("4. Modifica libro");

			System.out.println("5. Esci");

			System.out.print("\n\nInserisci la tua scelta: ");
			scelta = sc.nextLine();
			switch (scelta) {
			case "1": // inserimento
				l = new Libro();
				System.out.println("NUOVO LIBRO");
				System.out.print("Titolo: ");
				l.titolo = sc.nextLine();
				System.out.print("Anno pubblicazione: ");
				l.annoPubblicazione = sc.nextInt();
				sc.nextLine();
				do {
					System.out.print("Id autore: ");
					l.idAutore = sc.nextInt();
					sc.nextLine();
					l.autore = cercaPerId(l.idAutore, "autori", "CONCAT(nome,' ',cognome)", "nominativo"); // cerco il
																											// nome
																											// dell'autore
																											// con
																											// quell'id
					if (!l.autore.isEmpty())
						System.out.println("Autore: " + l.autore);
					else
						System.out.println("Autore non trovato. Ripetere l'inserimento");
				} while (l.autore.isEmpty());

				do {
					System.out.print("Id genere: ");
					l.idGenere = sc.nextInt();
					sc.nextLine();
					l.genere = cercaPerId(l.idGenere, "generi", "nome", "nome"); // cerco il nome del genere con
																					// quell'id
					if (!l.genere.isEmpty())
						System.out.println("Genere: " + l.genere);
					else
						System.out.println("Genere non trovato. Ripetere l'inserimento");
				} while (l.genere.isEmpty());

				righeInteressate = inserisciLibro(l); // inserisce nel DB
				if (righeInteressate == 1) {
					System.out.println("Libro correttamente inserito");
					// elencoLibri.add(l);
				} else
					System.out.println("Libro non correttamente inserito");

				break;
			case "2": // visualizzazione
				caricaLibri(elencoLibri);
				for (Libro lib : elencoLibri)
					System.out.println(lib.toString());
				break;
			case "3": // cancellazione
				System.out.println("CANCELLAZIONE LIBRO");
				System.out.print("Id: ");
				idLibro = sc.nextInt();
				sc.nextLine();
				righeInteressate = eliminaLibro(idLibro); // elimino dal database
				if (righeInteressate == 1) {
					System.out.println("Libro correttamente eliminato");
				} else
					System.out.println("Libro non correttamente eliminato");
				break;
			case "4": // modifica
				
				System.out.println("MODIFICA LIBRO");
				
				do {
					System.out.print("Id: ");
					int idLib = sc.nextInt();
					sc.nextLine();
					/*
					//cerco il libro con quell'id (versione "classica")
					l = null;
					for (Libro lib:elencoLibri)
						if (lib.id==idL)
							l=lib;
					if (l!=null)
						System.out.println(l.toString());
					else
						System.out.println("Libro non trovato. Reinserire");
					
					*/
					
					
					//cerco il libro con quell'id (versione con funzioni lambda)					
					Optional<Libro> ris=
						elencoLibri.stream().filter(lib -> lib.id==idLib ).findFirst();
					if (ris.isPresent()) {						
						l=ris.get();	
						System.out.println(l.toString());
					}						
					else {
						System.out.println("Libro non trovato. Reinserire");
						l=null;
					}
					
					
				} while (l==null);
				
				
				
				System.out.print("Titolo: ");
				l.titolo = sc.nextLine();
				System.out.print("Anno pubblicazione: ");
				l.annoPubblicazione = sc.nextInt();
				sc.nextLine();
				do {
					System.out.print("Id autore: ");
					l.idAutore = sc.nextInt();
					sc.nextLine();
					l.autore = cercaPerId(l.idAutore, "autori", "CONCAT(nome,' ',cognome)", "nominativo"); // cerco il
																											// nome
																											// dell'autore
																											// con
																											// quell'id
					if (!l.autore.isEmpty())
						System.out.println("Autore: " + l.autore);
					else
						System.out.println("Autore non trovato. Ripetere l'inserimento");
				} while (l.autore.isEmpty());

				do {
					System.out.print("Id genere: ");
					l.idGenere = sc.nextInt();
					sc.nextLine();
					l.genere = cercaPerId(l.idGenere, "generi", "nome", "nome"); // cerco il nome del genere con
																					// quell'id
					if (!l.genere.isEmpty())
						System.out.println("Genere: " + l.genere);
					else
						System.out.println("Genere non trovato. Ripetere l'inserimento");
				} while (l.genere.isEmpty());

				righeInteressate = modificaLibro(l); // modifica nel DB
				if (righeInteressate == 1) {
					System.out.println("Libro correttamente modificato");
					// elencoLibri.add(l);
				} else
					System.out.println("Libro non correttamente modificato");				
				
				break;
			case "5": // esci
				System.out.println("Arrivederci!");
				break;
			default: // tutti gli altri casi
				System.out.println("Scelta non valida!");
			}

			System.out.println("Premi invio per continuare...");
			sc.nextLine();

		} while (!scelta.equals("5"));
		sc.close();
	}

	static int inserisciLibro(Libro l) {
		String sql;
		int righeInserite = 0;
		// inserimento libro
		sql = "INSERT INTO libri(titolo, anno_pubblicazione, id_autore, id_genere) " + "VALUES (?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(url, username, password)) { // provo a connettermi
			try (PreparedStatement ps = conn.prepareStatement(sql)) { // provo a creare l'istruzione sql
				ps.setString(1, l.titolo);
				ps.setInt(2, l.annoPubblicazione);
				ps.setInt(3, l.idAutore);
				ps.setInt(4, l.idGenere);
				righeInserite = ps.executeUpdate(); // eseguo l'sql
			}
		} catch (Exception e) { // catch che gestisce tutti i tipi di eccezione (deve essere l'ultimo catch)
			// si è verificato un problema. L'oggetto e (di tipo Exception) contiene
			// informazioni sull'errore verificatosi

			System.err.println("Si è verificato un errore: " + e.getMessage());
			// System.err.println("Stacktrace:");
			// e.printStackTrace();

		}
		return righeInserite;
	}
	
	
	static int modificaLibro(Libro l) {
		String sql;
		int righeAggiornate = 0;
		// modifica libro
		sql = "UPDATE libri set titolo=?, anno_pubblicazione=?, id_autore=?, id_genere=? WHERE id=? ";
		try (Connection conn = DriverManager.getConnection(url, username, password)) { // provo a connettermi
			try (PreparedStatement ps = conn.prepareStatement(sql)) { // provo a creare l'istruzione sql
				ps.setString(1, l.titolo);
				ps.setInt(2, l.annoPubblicazione);
				ps.setInt(3, l.idAutore);
				ps.setInt(4, l.idGenere);
				ps.setInt(5, l.id);
				righeAggiornate = ps.executeUpdate(); // eseguo l'sql
			}
		} catch (Exception e) { // catch che gestisce tutti i tipi di eccezione (deve essere l'ultimo catch)
			// si è verificato un problema. L'oggetto e (di tipo Exception) contiene
			// informazioni sull'errore verificatosi

			System.err.println("Si è verificato un errore: " + e.getMessage());
			// System.err.println("Stacktrace:");
			// e.printStackTrace();

		}
		return righeAggiornate;
	}

	static int eliminaLibro(int id) {
		String sql;
		int righeEliminate = 0;
		// eliminazione libro
		sql = "DELETE FROM libri WHERE id=? ";

		try (Connection conn = DriverManager.getConnection(url, username, password)) { // provo a connettermi
			try (PreparedStatement ps = conn.prepareStatement(sql)) { // provo a creare l'istruzione sql
				ps.setInt(1, id);

				righeEliminate = ps.executeUpdate(); // eseguo l'sql
			}
		} catch (Exception e) { // catch che gestisce tutti i tipi di eccezione (deve essere l'ultimo catch)
			// si è verificato un problema. L'oggetto e (di tipo Exception) contiene
			// informazioni sull'errore verificatosi

			System.err.println("Si è verificato un errore: " + e.getMessage());
			// System.err.println("Stacktrace:");
			// e.printStackTrace();

		}
		return righeEliminate;
	}

	static String cercaPerId(int id, String nomeTabella, String campoDescrizione, String alias) {
		String sql = "SELECT " + campoDescrizione + " AS " + alias + " FROM " + nomeTabella + " WHERE id=" + id;
		String ris = "";
		// System.out.println(sql);
		try (Connection conn = DriverManager.getConnection(url, username, password)) { // provo a connettermi
			try (PreparedStatement ps = conn.prepareStatement(sql)) { // provo a creare l'istruzione sql
				try (ResultSet rs = ps.executeQuery()) { // il ResultSet mi consente di scorrere il risultato della
															// SELECT una riga alla volta
					if (rs.next())
						ris = rs.getString(alias);
				}
			}

		} catch (Exception e) { // catch che gestisce tutti i tipi di eccezione (deve essere l'ultimo catch)
			// si è verificato un problema. L'oggetto e (di tipo Exception) contiene
			// informazioni sull'errore verificatosi

			System.err.println("Si è verificato un errore: " + e.getMessage());
			// System.err.println("Stacktrace:");
			// e.printStackTrace();

		}
		return ris;
	}

	static void caricaLibri(ArrayList<Libro> elencoLibri) {
		// caricamento libri
		elencoLibri.clear();;
		Libro l;
		String sql = "SELECT * FROM libri INNER JOIN autori on libri.id_autore=autori.id INNER JOIN generi on libri.id_genere=generi.id";
		try (Connection conn = DriverManager.getConnection(url, username, password)) { // provo a connettermi
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
						l.autore = rs.getString("autori.nome") + " " + rs.getString("cognome");
						l.genere = rs.getString("generi.nome");
						elencoLibri.add(l);
					}
				}
			}
		} catch (Exception e) { // catch che gestisce tutti i tipi di eccezione (deve essere l'ultimo catch)
			// si è verificato un problema. L'oggetto e (di tipo Exception) contiene
			// informazioni sull'errore verificatosi

			System.err.println("Si è verificato un errore: " + e.getMessage());
			// System.err.println("Stacktrace:");
			// e.printStackTrace();

		}
	}
}
