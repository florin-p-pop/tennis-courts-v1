package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.reservations.ReservationRepository;
import com.tenniscourts.reservations.ReservationStatus;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;

    private final TennisCourtRepository tennisCourtRepository;

    private final ReservationRepository reservationRepository;

    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {
        validateStartDateTime(createScheduleRequestDTO.getStartDateTime());
        Schedule schedule = Schedule.builder()
                .tennisCourt(findTennisCourtById(createScheduleRequestDTO.getTennisCourtId()))
                .startDateTime(createScheduleRequestDTO.getStartDateTime())
                .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1))
                .build();

        return scheduleMapper.map(scheduleRepository.saveAndFlush(schedule));
    }

    public List<ScheduleDTO> findSchedulesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleRepository.findAllByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(startDate, endDate)
                .stream().map(scheduleMapper::map).collect(Collectors.toList());
    }

    public List<ScheduleDTO> findAvailableSchedulesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return findSchedulesBetweenDates(startDate, endDate)
                .stream()
                .filter(this::isAvailable)
                .collect(Collectors.toList());
    }

    public ScheduleDTO findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule not found.");
        });
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }

    private TennisCourt findTennisCourtById(Long id) {
        return tennisCourtRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Tennis Court not found.");
        });
    }

    public void validateStartDateTime(LocalDateTime startDateTime) {
        if (startDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("StartDateTime can not be in the past.");
        }
    }

    private boolean isAvailable(ScheduleDTO scheduleDTO) {
        return reservationRepository.findBySchedule_Id(scheduleDTO.getId())
                .stream()
                .anyMatch(reservation -> !reservation.getReservationStatus().equals(ReservationStatus.READY_TO_PLAY));
    }
}