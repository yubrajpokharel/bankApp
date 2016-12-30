package com.bank.dao.appointment;

import com.bank.domain.Appointment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yubraj on 12/30/16.
 */

public interface AppointmentDao extends CrudRepository<Appointment, Long> {
    List<Appointment> findAll();
}
