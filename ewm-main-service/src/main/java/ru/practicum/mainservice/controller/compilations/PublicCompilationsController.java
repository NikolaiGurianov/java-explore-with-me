package ru.practicum.mainservice.controller.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.compilationDto.CompilationDto;
import ru.practicum.mainservice.service.compilations.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class PublicCompilationsController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") boolean pinned,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return compilationService.getCompilationsPub(pinned, from, size);
    }

    @GetMapping("/{compilationId}")
    public CompilationDto getCompilationsById(@PathVariable Long compilationId) {
        return compilationService.getCompilationsByIdPub(compilationId);
    }


}
