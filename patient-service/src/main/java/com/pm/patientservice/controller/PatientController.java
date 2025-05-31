package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.validators.CreatePatientValidationGroup;
import com.pm.patientservice.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> patients() {
        List<PatientResponseDTO> patientResponseDTOs = patientService.getPatients();
        return ResponseEntity.ok().body(patientResponseDTOs);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({CreatePatientValidationGroup.class,Default.class})
                                                            @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);

        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @PutMapping("{patientID}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID patientID,
                                                            @Validated({Default.class})
                                                            @Valid @RequestBody PatientRequestDTO patientRequestDTO) {


        PatientResponseDTO patientResponseDTO = patientService.updatePatient(patientID, patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @DeleteMapping("{patientID}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID patientID) {
        patientService.deletePatient(patientID);
        return ResponseEntity.noContent().build();
    }
}
