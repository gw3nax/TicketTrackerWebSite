package ru.gw3nax.tickettrackerwebsite.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import ru.gw3nax.tickettrackerwebsite.configuration.MapperConfiguration;
import ru.gw3nax.tickettrackerwebsite.dto.request.FlightRequest;
import ru.gw3nax.tickettrackerwebsite.entity.FlightRequestEntity;


@Mapper(config = MapperConfiguration.class)
public interface FlightRequestEntityToFlightRequestMapper extends Converter<FlightRequestEntity, FlightRequest> {

    @Override
    FlightRequest convert(FlightRequestEntity source);
}