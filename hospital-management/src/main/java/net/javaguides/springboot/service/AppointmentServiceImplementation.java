package net.javaguides.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import net.javaguides.springboot.entity.Appointment;
import net.javaguides.springboot.repository.AppointmentRepository;

@Service
public class AppointmentServiceImplementation {

	private AppointmentRepository appointmentRepository;

	// inject employee dao
	// @Autowired //Adding bean id @Qualifier
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
