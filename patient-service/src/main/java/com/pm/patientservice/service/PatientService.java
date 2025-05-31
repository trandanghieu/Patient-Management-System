package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        List<PatientResponseDTO> patientResponseDTOs = patients.stream()
                .map(patient -> PatientMapper.toDTO(patient))
                .toList();

        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email " + patientRequestDTO.getEmail() + " already exists");
        }
        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID uuid, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(uuid).orElseThrow(()-> new PatientNotFoundException("Patient not found"));

        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email " + patientRequestDTO.getEmail() + " already exists");
        }

        return PatientMapper.toDTO(patientRepository.save(PatientMapper.toModel(patientRequestDTO)));
    }

    public void deletePatient(UUID uuid) {
        patientRepository.deleteById(uuid);
    }
}
