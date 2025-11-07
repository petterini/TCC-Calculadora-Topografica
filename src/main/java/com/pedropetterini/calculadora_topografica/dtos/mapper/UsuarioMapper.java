package com.pedropetterini.calculadora_topografica.dtos.mapper;

import com.pedropetterini.calculadora_topografica.dtos.UsuarioDTO;
import com.pedropetterini.calculadora_topografica.models.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    private final ModelMapper modelMapper;

    public UsuarioMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, Usuario.class);
    }

    public UsuarioDTO toDTO(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioDTO.class);
    }
}
