package org.generation.italy.model;

public class Libro {
	public int id;
	public String titolo;
	public int annoPubblicazione;
	public int idAutore;
	public String autore;
	public int idGenere;
	public String genere;
	@Override
	public String toString() {
		return "Libro [id=" + id + ", titolo=" + titolo + ", annoPubblicazione=" + annoPubblicazione + ", idAutore="
				+ idAutore + ", autore=" + autore + ", idGenere=" + idGenere + ", genere=" + genere + "]";
	}
}
