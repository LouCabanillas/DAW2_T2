package com.cibertec.assessment.controller;

import com.cibertec.assessment.beans.PolygonBean;
import com.cibertec.assessment.model.Polygon;
import com.cibertec.assessment.service.PolygonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polygon")
public class PolygonController {

    @Autowired
    PolygonService polygonService;

    @PostMapping
    public ResponseEntity<String> crear(@RequestBody List<Polygon> lista) {
        polygonService.create(lista);
        return new ResponseEntity<>("Polígonos Creados", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Polygon>> listar(){
        return new ResponseEntity<>(polygonService.list(), HttpStatus.OK)  ;
    }

}