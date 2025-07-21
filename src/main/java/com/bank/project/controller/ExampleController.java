package com.bank.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/examples")
@Tag(name = "Example Controller", description = "Example API endpoints")
public class ExampleController {

    @GetMapping
    @Operation(
        summary = "Get all examples",
        description = "Retrieves a list of all examples")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<String> getAllExamples() {
        return ResponseEntity.ok("List of examples");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get example by ID")
    @ApiResponse(responseCode = "200", description = "Example found")
    @ApiResponse(responseCode = "404", description = "Example not found")
    public ResponseEntity<String> getExampleById(@PathVariable Long id) {
        return ResponseEntity.ok("Example with ID: " + id);
    }

    @PostMapping
    @Operation(summary = "Create a new example")
    @ApiResponse(responseCode = "200", description = "Example created successfully")
    public ResponseEntity<String> createExample(@RequestBody String example) {
        return ResponseEntity.ok("Created: " + example);
    }
}
