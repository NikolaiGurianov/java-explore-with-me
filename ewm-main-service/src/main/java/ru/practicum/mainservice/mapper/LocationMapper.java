package ru.practicum.mainservice.mapper;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.dto.LocationDto;
import ru.practicum.mainservice.model.Location;

@Data
@Component
public class LocationMapper {
    public static Location toLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLon(locationDto.getLon());
        location.setLat(locationDto.getLat());
        return location;
    }

    public static LocationDto toLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());
        return locationDto;
    }
}