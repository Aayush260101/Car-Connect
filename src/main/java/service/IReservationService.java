package service;

import entity.Reservation;

import exception.ReservationException;

import java.sql.SQLException;
import java.util.List;

public interface IReservationService {
    Reservation getReservationById(int reservationId);
    List<Reservation> getReservationsByCustomerId(int customerId);
    void createReservation(Reservation reservation) throws ReservationException;
    void updateReservation(Reservation reservation) throws ReservationException;
    void cancelReservation(int reservationId) throws ReservationException;
}
