package com.cibertec.assessment.controller;

import com.cibertec.assessment.beans.PolygonBean;
import com.cibertec.assessment.model.Polygon;
import com.cibertec.assessment.model.Square;
import com.cibertec.assessment.service.PolygonService;
import com.cibertec.assessment.service.SquareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/square")
public class SquareController {

    @Autowired
    SquareService squareService;

    @PostMapping
    public ResponseEntity<Square> buscarIntercepcion(@RequestBody Square square) {
        return new ResponseEntity<>(squareService.create(square), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Square>> listar(){
        return new ResponseEntity<>(squareService.list(), HttpStatus.OK)  ;
    }

}
