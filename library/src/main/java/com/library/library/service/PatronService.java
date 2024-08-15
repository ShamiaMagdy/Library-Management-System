package com.library.library.service;

import com.library.library.entity.Patron;
import com.library.library.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PatronService {

    @Autowired
    private PatronRepository patronRepository;

    public List<Patron> getAllPatrons(){
        List<Patron> patrons=patronRepository.findAll();
        return patrons.isEmpty()? Collections.emptyList():patrons;
    }

    public Optional<Patron> getPatronById(Long Id){
        Optional<Patron> patron=patronRepository.findById(Id);
        return Optional.of(patron.orElse(new Patron()));
    }

    public void validateEmailFormat(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        if (email == null || !email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    public void validatePatronData(Patron patron) {
        if (patron.getName() == null || patron.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patron Name Cannot Be Null.");
        }
        if (patron.getPhoneNumber() == null || !patron.getPhoneNumber().matches("\\d{11}")) {
            throw new IllegalArgumentException("Phone Number Must Be 11 Digits.");
        }
        validateEmailFormat(patron.getContactInformation());
    }

    public Patron addPatron(Patron patron){
        validatePatronData(patron);
        if(patronRepository.findByName(patron.getName()).isPresent() ||
                patronRepository.findByContactInformation(patron.getContactInformation()).isPresent() ||
                patronRepository.findByPhoneNumber(patron.getPhoneNumber()).isPresent()){
            throw new IllegalArgumentException("Patron Attributes Must Be Unique.");
        }
        return patronRepository.save(patron);
    }

    public Patron updatePatron(Long Id, Patron updatedpatron){
        return patronRepository.findById(Id)
                .map(patron -> {
                    validatePatronData(updatedpatron);
                    if(!patron.getName().equals(updatedpatron.getName())&&
                            patronRepository.findByName(updatedpatron.getName()).isPresent()){
                        throw new IllegalArgumentException("This Name Already Exists");
                    }
                    if(!patron.getPhoneNumber().equals(updatedpatron.getPhoneNumber()) &&
                            patronRepository.findByPhoneNumber(updatedpatron.getPhoneNumber()).isPresent()){
                        throw new IllegalArgumentException("This Phone Number Already Exists");
                    }
                    if(!patron.getContactInformation().equals(updatedpatron.getContactInformation()) &&
                            patronRepository.findByContactInformation(updatedpatron.getContactInformation()).isPresent()) {
                        throw new IllegalArgumentException("This Contact Information Already Exists");
                    }
                    patron.setName(updatedpatron.getName());
                    patron.setContactInformation(updatedpatron.getContactInformation());
                    patron.setPhoneNumber(updatedpatron.getPhoneNumber());
                    return patronRepository.save(patron);
                }).orElseThrow(()-> new IllegalArgumentException("Patron Id "+Id+" Not Found."));
    }

    public Boolean deletePatron(Long Id){
        if(patronRepository.findById(Id).isEmpty()){
            //throw new IllegalArgumentException("Patron Id "+Id+" Not Found");
            return false;
        }
        patronRepository.deleteById(Id);
        System.out.println("Patron Id "+Id+" Was Deleted.");
        return true;
    }
}
