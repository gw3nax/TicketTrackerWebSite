package ru.gw3nax.tickettrackerwebsite.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import ru.gw3nax.tickettrackerwebsite.configuration.MapperConfiguration;
import ru.gw3nax.tickettrackerwebsite.dto.request.FlightRequest;
import ru.gw3nax.tickettrackerwebsite.dto.request.UserRequest;
import ru.gw3nax.tickettrackerwebsite.entity.FlightRequestEntity;
import ru.gw3nax.tickettrackerwebsite.entity.UserEntity;


@Mapper(config = MapperConfiguration.class)
public interface UserRequestToUserEntityMapper extends Converter<UserRequest, UserEntity> {

    @Override
    UserEntity convert(UserRequest source);
}