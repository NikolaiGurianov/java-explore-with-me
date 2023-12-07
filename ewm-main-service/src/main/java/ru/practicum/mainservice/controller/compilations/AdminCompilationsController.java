package ru.practicum.mainservice.controller.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.compilationDto.CompilationDto;
import ru.practicum.mainservice.dto.compilationDto.NewCompilationDto;
import ru.practicum.mainservice.dto.compilationDto.UpdateCompilationRequest;
import ru.practicum.mainservice.service.compilations.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return new ResponseEntity<>(compilationService.addCompilationAdm(newCompilationDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto updateCompilationById(@PathVariable Long compilationId,
                                                @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return compilationService.updateCompilationByIdAdm(compilationId, updateCompilationRequest);
    }

    @DeleteMapping("/{compilationId}")
    public ResponseEntity<String> deleteCompilationById(@PathVariable Long compilationId) {
        return compilationService.deleteCompilationByIdAdm(compilationId);
    }
}