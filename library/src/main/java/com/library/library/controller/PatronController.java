package com.library.library.controller;

import com.library.library.entity.Patron;
import com.library.library.service.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    @Autowired
    private PatronService patronService;

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrons(){
        List<Patron> patrons = patronService.getAllPatrons();
        return patrons.isEmpty()? ResponseEntity.noContent().build():ResponseEntity.ok(patrons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id){
        return patronService.getPatronById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Patron> addPatron(@RequestBody Patron patron){
        try {
            Patron newPatron = patronService.addPatron(patron);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPatron);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Patron> addPatron(@PathVariable Long id, @RequestBody Patron patron){
        try {
            return ResponseEntity.ok(patronService.updatePatron(id, patron));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> addPatron(@PathVariable Long id){
        try {
            return ResponseEntity.ok(patronService.deletePatron(id));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
