package com.bank.service.appointment.impl;

import com.bank.dao.appointment.AppointmentDao;
import com.bank.domain.Appointment;
import com.bank.service.appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yubraj on 12/30/16.
 */

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentDao appointmentDao;

    @Override
    public Appointment createAppointment(Appointment appointment) {
        return  appointmentDao.save(appointment);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentDao.findAll();
    }

    @Override
    public Appointment findAppointment(Long id) {
        return appointmentDao.findOne(id);
    }

    @Override
    public void confirmAppointment(Long id) {
        Appointment appointment = findAppointment(id);
        appointment.setConfirmed(true);
        appointmentDao.save(appointment);
    }
}
