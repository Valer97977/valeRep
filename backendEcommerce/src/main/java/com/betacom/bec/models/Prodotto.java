package com.betacom.bec.models;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity //tutti i db e tabelle sono entity
@Table (name="prodotti")
public class Prodotto {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length=100,
    		nullable=false)
	private String marca;
	
	@Column(length=100,
    		nullable=false)
	private String nome;
	
	@Column(length=100,
    		nullable=false)
	private String categoria;
	
	@Column(length=100,
    		nullable=false)
	private String descrizione;
	
	@Column(length=100,
    		nullable=false)
	private Double prezzo;
	
	@Column(name="quantita_disponibile",
			length=100,
    		nullable=false)
	private Integer quantitaDisponibile;
	
	@Column(name="url_immagine",
			length=100,
    		nullable=false)
	private String urlImg;
	
	@Column(length=100,
    		nullable=false)
	private String size;
	
	@Column(length=100,
    		nullable=false)
	private String colore;
	
	@Column(name="data_creazione",
			length=100,
    		nullable=false)
	private Timestamp dataCreazione;
	
	@OneToMany(mappedBy = "prodotto")
	private List<Carrello> carrelli;

	@OneToMany(mappedBy = "prodotto")
	private List<Ordine> ordini;

	@OneToMany(mappedBy = "prodotto")
	private List<Recensione> recensioni;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Double prezzo) {
		this.prezzo = prezzo;
	}

	public Integer getQuantitaDisponibile() {
		return quantitaDisponibile;
	}

	public void setQuantitaDisponibile(Integer quantitàDisponibile) {
		this.quantitaDisponibile = quantitàDisponibile;
	}

	public String getUrlImg() {
		return urlImg;
	}

	public void setUrlImg(String urlImg) {
		this.urlImg = urlImg;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public Timestamp getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public List<Carrello> getCarrelli() {
		return carrelli;
	}

	public void setCarrelli(List<Carrello> carrelli) {
		this.carrelli = carrelli;
	}

	public List<Ordine> getOrdini() {
		return ordini;
	}

	public void setOrdini(List<Ordine> ordini) {
		this.ordini = ordini;
	}

	public List<Recensione> getRecensioni() {
		return recensioni;
	}

	public void setRecensioni(List<Recensione> recensioni) {
		this.recensioni = recensioni;
	}
	
	

}
