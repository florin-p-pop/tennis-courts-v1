package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtMapper;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import com.tenniscourts.tenniscourts.TennisCourtService;
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

    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {
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
}