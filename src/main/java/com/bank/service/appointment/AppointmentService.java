package com.bank.service.appointment;

import com.bank.domain.Appointment;

import java.util.List;

/**
 * Created by yubraj on 12/30/16.
 */
public interface AppointmentService {
    Appointment createAppointment(Appointment appointment);
    List<Appointment> findAll();
    Appointment findAppointment(Long id);
    void confirmAppointment(Long id);
}
