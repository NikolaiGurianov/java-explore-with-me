package ru.practicum.mainservice.service.compilations;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationRequest;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.mapper.CompilationMapper;
import ru.practicum.mainservice.model.Compilation;
import ru.practicum.mainservice.model.Event;
import ru.practicum.mainservice.repository.CompilationsRepository;
import ru.practicum.mainservice.repository.EventsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.Constants.SORT_BY_ID_ASC;

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationsRepository compilationsRepository;
    private final EventsRepository eventsRepository;

    @Override
    public CompilationDto addCompilationAdm(NewCompilationDto newCompilationDto) {
        log.info("Admin: Получен запрос на добавление подборки событий");
        List<Event> events;
        if (newCompilationDto.getEvents() == null) {
            events = new ArrayList<>();
        } else {
            events = eventsRepository.findByIdIn(newCompilationDto.getEvents());
        }
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationsRepository.save(compilation));
        log.info("Admin: Запрос выполнен. Добавлена подборка событий: {}", compilationDto);
        return compilationDto;
    }

    @Override
    public CompilationDto updateCompilationByIdAdm(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        log.info("Admin: Получен запрос на обновление подборки событий с ID={}", compId);
        Compilation compilation = getEntity(compId);

        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(eventsRepository.findByIdIn(updateCompilationRequest.getEvents()));
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationsRepository.save(compilation));
        log.info("Admin: Запрос выполнен. Подборка событий обновлена: {}", compilationDto);
        return compilationDto;
    }

    @Override
    public void deleteCompilationByIdAdm(Long compId) {
        log.info("Public: Запрос на удаление подборки событий ID= {}", compId);
        compilationsRepository.deleteById(compId);
    }

    @Override
    public List<CompilationDto> getCompilationsPub(Boolean pinned, int from, int size) {
        log.info("Public: Запрос на получение подборок событий");
        Pageable pageable = PageRequest.of(from / size, size, SORT_BY_ID_ASC);
        List<Compilation> compilationList = compilationsRepository.findAllByPinned(pageable, pinned);
        List<CompilationDto> compilationDtoList = compilationList
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());

        log.info("Public: Запрос выполнен. Получено {} подборок событий", compilationDtoList.size());
        return compilationDtoList;
    }

    @Override
    public CompilationDto getCompilationsByIdPub(Long compId) {
        log.info("Public: Запрос на получение подборки событий с ID={}", compId);
        CompilationDto compilationDto = get(compId);
        log.info("Public: Запрос выполнен. Получена подборка событий: {}", compilationDto);
        return compilationDto;
    }

    @Override
    public CompilationDto get(Long compId) {
        return CompilationMapper.toCompilationDto(getEntity(compId));
    }

    @Override
    public Compilation getEntity(Long compId) {
        return compilationsRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Подборка событий с ID={} не найдена", compId));
    }
}