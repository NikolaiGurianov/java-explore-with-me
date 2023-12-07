package ru.practicum.mainservice.service.compilations;

import ru.practicum.mainservice.dto.compilation.CompilationDto;
import ru.practicum.mainservice.dto.compilation.NewCompilationDto;
import ru.practicum.mainservice.dto.compilation.UpdateCompilationRequest;
import ru.practicum.mainservice.model.Compilation;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilationAdm(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilationByIdAdm(Long compId, UpdateCompilationRequest updateCompilationRequest);

    void deleteCompilationByIdAdm(Long compId);

    List<CompilationDto> getCompilationsPub(Boolean pinned, int from, int size);

    CompilationDto getCompilationsByIdPub(Long compId);

    Compilation getEntity(Long compId);

    CompilationDto get(Long compId);
}