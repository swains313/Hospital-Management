package net.javaguides.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.javaguides.springboot.entity.Appointment;
import net.javaguides.springboot.repository.AppointmentRepository;

@Service
public class AppointmentServiceImplementation {

	@Autowired
	private AppointmentRepository appointmentRepository;


	@Autowired
	public AppointmentServiceImplementation(AppointmentRepository obj) {
		appointmentRepository = obj;
	}

	public void save(Appointment app) {

		appointmentRepository.save(app);
	}

	public List<Appointment> findAll() {
		return appointmentRepository.findAll();
	}

}
