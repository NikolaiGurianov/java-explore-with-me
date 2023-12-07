package ru.practicum.mainservice.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.dto.compilationDto.CompilationDto;
import ru.practicum.mainservice.dto.compilationDto.NewCompilationDto;
import ru.practicum.mainservice.model.Compilation;
import ru.practicum.mainservice.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setPinned(newCompilationDto.getPinned() != null && newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList()));
        compilationDto.setPinned(compilation.isPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }
}