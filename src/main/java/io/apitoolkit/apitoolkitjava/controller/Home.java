package io.apitoolkit.apitoolkitjava.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

  @GetMapping("/{id}")
  public ResponseEntity<String> home() {
    System.out.println("home route was called");
    return ResponseEntity.ok("Hello World!");
  }
}
