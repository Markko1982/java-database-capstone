package com.project.back_end.services;

import com.project.back_end.dto.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        Optional<Doctor> opt = doctorRepository.findById(doctorId);
        if (opt.isEmpty()) return Collections.emptyList();
        Doctor d = opt.get();
        List<String> available = new ArrayList<>(Optional.ofNullable(d.getAvailableTimes()).orElse(Collections.emptyList()));

        var start = date.atStartOfDay();
        var end = date.plusDays(1).atStartOfDay().minusNanos(1);

        var booked = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end)
                .stream()
                .map(a -> a.getAppointmentTime().toLocalTime().withSecond(0).withNano(0).toString().substring(0,5))
                .collect(Collectors.toSet());

        available.removeIf(booked::contains);
        available.sort(Comparator.naturalOrder());
        return available;
    }

    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) return -1;
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int updateDoctor(Doctor doctor) {
        try {
            if (doctor.getId() == null || doctorRepository.findById(doctor.getId()).isEmpty()) return -1;
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    public int deleteDoctor(long id) {
        try {
            if (doctorRepository.findById(id).isEmpty()) return -1;
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Map<String, String> body = new HashMap<>();
        try {
            Doctor d = doctorRepository.findByEmail(login.getIdentifier());
            if (d == null || !Objects.equals(d.getPassword(), login.getPassword())) {
                body.put("message", "Credenciais inválidas.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
            }
            String token = tokenService.generateToken(d.getEmail());
            body.put("token", token);
            body.put("message", "Login realizado com sucesso.");
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            body.put("message", "Erro interno.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    public Map<String, Object> findDoctorByName(String name) {
        Map<String, Object> res = new HashMap<>();
        res.put("doctors", doctorRepository.findByNameLike("%" + name + "%"));
        return res;
    }

    public Map<String, Object> filterDoctorsByNameSpecilityandTime(String name, String specialty, String amOrPm) {
        List<Doctor> list = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        list = filterDoctorByTime(list, amOrPm);
        Map<String, Object> res = new HashMap<>();
        res.put("doctors", list);
        return res;
    }

    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
        List<Doctor> list = doctorRepository.findByNameLike("%" + name + "%");
        list = filterDoctorByTime(list, amOrPm);
        Map<String, Object> res = new HashMap<>();
        res.put("doctors", list);
        return res;
    }

    public Map<String, Object> filterDoctorByNameAndSpecility(String name, String specialty) {
        Map<String, Object> res = new HashMap<>();
        res.put("doctors", doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty));
        return res;
    }

    public Map<String, Object> filterDoctorByTimeAndSpecility(String specialty, String amOrPm) {
        List<Doctor> list = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        list = filterDoctorByTime(list, amOrPm);
        Map<String, Object> res = new HashMap<>();
        res.put("doctors", list);
        return res;
    }

    public Map<String, Object> filterDoctorBySpecility(String specialty) {
        Map<String, Object> res = new HashMap<>();
        res.put("doctors", doctorRepository.findBySpecialtyIgnoreCase(specialty));
        return res;
    }

    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        List<Doctor> list = doctorRepository.findAll();
        list = filterDoctorByTime(list, amOrPm);
        Map<String, Object> res = new HashMap<>();
        res.put("doctors", list);
        return res;
    }

    private List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        if (amOrPm == null || amOrPm.isBlank()) return doctors;
        boolean isAM = amOrPm.trim().equalsIgnoreCase("AM");
        return doctors.stream().filter(d -> {
            List<String> times = d.getAvailableTimes();
            if (times == null) return false;
            return times.stream().anyMatch(t -> {
                try {
                    LocalTime lt = LocalTime.parse(t);
                    return isAM ? lt.isBefore(LocalTime.NOON) : !lt.isBefore(LocalTime.NOON);
                } catch (Exception e) {
                    return false;
                }
            });
        }).collect(Collectors.toList());
    }
}
