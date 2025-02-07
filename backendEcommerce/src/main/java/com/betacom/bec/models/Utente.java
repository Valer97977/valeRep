package com.betacom.bec.models;

import java.sql.Timestamp;
import java.util.List;

import com.betacom.bec.utils.Roles;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity //tutti i db e tabelle sono entity
@Table (name="utenti")
public class Utente {
	
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length=100,
    		nullable=false)
	private String nome;
	
	@Column(length=100,
    		nullable=false)
	private String cognome;
	
	@Column(length=100,
    		nullable=false)
	private String email;
	
	@Column(name="hash_password",
			length=100,
    		nullable=false)
	private String psw;
	
	private Roles ruolo;
	
	@Column(name="numero_telefono",
			length=100,
    		nullable=false)
	private String numeroTelefono;
	
	@Column(name="indirizzo_spedizione",
			length=100,
    		nullable=false)
	private String indirizzoDiSpedizione;
	
	@Column(name="indirizzo_fatturazione",
			length=100,
    		nullable=false)
	private String indirizzoDiFatturazione;
	
	@Column(name="data_creazione",
			length=100,
    		nullable=false)
	private Timestamp dataC;
	
	@OneToOne
	@JoinColumn(name = "id_carrello")
	private Carrello carrello;
	
	@OneToMany(mappedBy="utente",   //colegamento con utente
 		   fetch = FetchType.EAGER,  //strategy di caricamento oggetto-> tutti oggetti sono caricati
 		   cascade= CascadeType.REMOVE) //remove all utenti in caso di delete
    private List<Ordine> ordini;
	
	@OneToMany(mappedBy="utente",   //colegamento con utente
 		   fetch = FetchType.EAGER,  //strategy di caricamento oggetto-> tutti oggetti sono caricati
 		   cascade= CascadeType.REMOVE) //remove all tenti in caso di delete
    private List<Recensione> recensioni;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public String getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public String getIndirizzoDiSpedizione() {
		return indirizzoDiSpedizione;
	}

	public void setIndirizzoDiSpedizione(String indirizzoDiSpedizione) {
		this.indirizzoDiSpedizione = indirizzoDiSpedizione;
	}

	public String getIndirizzoDiFatturazione() {
		return indirizzoDiFatturazione;
	}

	public void setIndirizzoDiFatturazione(String indirizzoDiFatturazione) {
		this.indirizzoDiFatturazione = indirizzoDiFatturazione;
	}

	public Timestamp getDataC() {
		return dataC;
	}

	public void setDataC(Timestamp dataC) {
		this.dataC = dataC;
	}

	public Carrello getCarrello() {
		return carrello;
	}

	public void setCarrello(Carrello carrello) {
		this.carrello = carrello;
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

	public Roles getRuolo() {
		return ruolo;
	}

	public void setRuolo(Roles ruolo) {
		this.ruolo = ruolo;
	}

	
	
}
