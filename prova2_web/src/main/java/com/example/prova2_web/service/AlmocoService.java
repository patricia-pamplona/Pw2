package com.example.prova2_web.service;

import com.example.prova2_web.model.Almoco;
import com.example.prova2_web.repository.AlmocoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class AlmocoService {
    private AlmocoRepository almocoRepository;

    public AlmocoService(AlmocoRepository almocoRepository){
        this.almocoRepository = almocoRepository;
    }

    public List<Almoco> findALl(){
        return almocoRepository.findAll();

    }

    public void create(Almoco almoco){
        almocoRepository.save(almoco);
    }
    public Almoco findById(Long id){
        Optional<Almoco> almoco = almocoRepository.findById(id);
        if(almoco.isPresent()){
            return almoco.get();
        }else{
            return null;
        }
    }

    public void deleteById(Long id){
        almocoRepository.deleteById(id);
    }
    public void update(Almoco almoco){
        almocoRepository.saveAndFlush(almoco);
    }
    public List<Almoco> findAllNotDeleteds(){
        List<Almoco> lista = almocoRepository.findAll();
        List<Almoco> listaFinal = new ArrayList<>();
        for(Almoco a : lista) {
            if (a.getDeleted() == null) {
                listaFinal.add(a);
            }
        }
        return listaFinal;
    }


}
