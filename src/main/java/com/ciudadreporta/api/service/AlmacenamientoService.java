package com.ciudadreporta.api.service;

import org.springframework.web.multipart.MultipartFile;

public interface AlmacenamientoService {

    String subir(MultipartFile archivo, String carpeta);

}
