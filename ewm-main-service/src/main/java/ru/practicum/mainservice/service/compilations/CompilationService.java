package ru.practicum.mainservice.service.compilations;

import org.springframework.http.ResponseEntity;
import ru.practicum.mainservice.dto.compilationDto.CompilationDto;
import ru.practicum.mainservice.dto.compilationDto.NewCompilationDto;
import ru.practicum.mainservice.dto.compilationDto.UpdateCompilationRequest;
import ru.practicum.mainservice.model.Compilation;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilationAdm(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilationByIdAdm(Long compId, UpdateCompilationRequest updateCompilationRequest);

    ResponseEntity<String> deleteCompilationByIdAdm(Long compId);

    List<CompilationDto> getCompilationsPub(Boolean pinned, int from, int size);

    CompilationDto getCompilationsByIdPub(Long compId);

    Compilation getEntity(Long compId);

    CompilationDto get(Long compId);
}