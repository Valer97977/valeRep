package com.betacom.bec.services.implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.betacom.bec.dto.UtenteDTO;
import com.betacom.bec.models.Carrello;
import com.betacom.bec.models.Utente;
import com.betacom.bec.repositories.CarrelloRepository;
import com.betacom.bec.repositories.UtenteRepository;
import com.betacom.bec.request.UtenteReq;
import com.betacom.bec.services.interfaces.MessaggioServices;
import com.betacom.bec.services.interfaces.UtenteServices;
import com.betacom.bec.utils.Roles;

@Service
public class UtenteImpl implements UtenteServices {

    @Autowired
    Logger log;

    @Autowired
    UtenteRepository utR;

    @Autowired
    CarrelloRepository caR;

    @Autowired
    private MessaggioServices msgS;

    @Override
    public List<UtenteDTO> list() {
        List<Utente> utenti = utR.findAll();

        return utenti.stream().map(u -> new UtenteDTO(
                u.getId(),
                u.getNome(),
                u.getCognome(),
                u.getEmail(),
                u.getPsw(),
                u.getRuolo() != null ? u.getRuolo().toString() : "N/A", // Controllo per evitare NullPointerException
                u.getNumeroTelefono(),
                u.getIndirizzoDiFatturazione(),
                u.getIndirizzoDiSpedizione()
        )).collect(Collectors.toList());
    }

    @Override
    public void create(UtenteReq req) throws Exception {
        System.out.println("Create : " + req);

        Optional<Utente> c = utR.findByNome(req.getNome().trim());

        if (c.isPresent())
            throw new Exception(msgS.getMessaggio("find-utente"));

        if (req.getNome() == null)
            throw new Exception(msgS.getMessaggio("no-nome"));
        if (req.getCognome() == null)
            throw new Exception(msgS.getMessaggio("no-cognome"));
        if (req.getEmail() == null)
            throw new Exception(msgS.getMessaggio("no-email"));
        if (req.getPassword() == null)
            throw new Exception(msgS.getMessaggio("no-psw"));
        if (req.getRuolo() == null)
            throw new Exception(msgS.getMessaggio("no-ruolo"));
        if (req.getNumeroTelefono() == null)
            throw new Exception(msgS.getMessaggio("no-num-tel"));
        if (req.getIndirizzoDiSpedizione() == null)
            throw new Exception(msgS.getMessaggio("no-spedizione"));
        if (req.getIndirizzoDiFatturazione() == null)
            throw new Exception(msgS.getMessaggio("no-fatturazione"));

        Utente utente = new Utente();
        utente.setNome(req.getNome());
        utente.setCognome(req.getCognome());
        utente.setEmail(req.getEmail());
        utente.setPsw(req.getPassword());

        // Aggiungi un controllo per verificare se il ruolo è valido
        if (req.getRuolo() == null || !isValidRole(req.getRuolo())) {
            throw new Exception("Ruolo non valido");
        }

        utente.setRuolo(Roles.valueOf(req.getRuolo()));
        utente.setNumeroTelefono(req.getNumeroTelefono());
        utente.setIndirizzoDiSpedizione(req.getIndirizzoDiSpedizione());
        utente.setIndirizzoDiFatturazione(req.getIndirizzoDiFatturazione());

        // Salva utente
        utR.save(utente);

        // Creazione e associazione del carrello
        Carrello carrello = new Carrello();
        carrello.setQuantita(0);
        carrello.setPrezzo(0.0);
        carrello.setUtente(utente);
        caR.save(carrello);
    }

    @Override
    public void update(UtenteReq req) throws Exception {
        Optional<Utente> u = utR.findById(req.getId());
        if (u.isEmpty())
            throw new Exception("Username inesistente");

        if (req.getId() != null)
            u.get().setId(req.getId());
        if (req.getCognome() != null)
            u.get().setCognome(req.getCognome());
        if (req.getEmail() != null)
            u.get().setEmail(req.getEmail());
        if (req.getPassword() != null)
            u.get().setPsw(req.getPassword());
        if (req.getRuolo() != null)
            u.get().setRuolo(Roles.valueOf(req.getRuolo()));
        if (req.getNumeroTelefono() != null)
            u.get().setNumeroTelefono(req.getNumeroTelefono());
        if (req.getIndirizzoDiSpedizione() != null)
            u.get().setIndirizzoDiSpedizione(req.getIndirizzoDiSpedizione());
        if (req.getIndirizzoDiFatturazione() != null)
            u.get().setIndirizzoDiFatturazione(req.getIndirizzoDiFatturazione());

        utR.save(u.get());
    }

    @Override
    public void remove(UtenteReq req) throws Exception {
        Optional<Utente> u = utR.findById(req.getId());

        if (u.isEmpty()) {
            throw new Exception("Username inesistente");
        }

        Utente utente = u.get();

        // Elimina il carrello associato all'utente, se esiste
        Optional<Carrello> carrelloOpt = caR.findByUtente(utente);
        carrelloOpt.ifPresent(caR::delete);

        // Elimina l'utente
        utR.delete(utente);

        System.out.println("Utente e carrello eliminati con successo");
    }

    // Metodo per validare se il ruolo è corretto
    private boolean isValidRole(String role) {
        try {
            Roles.valueOf(role);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
