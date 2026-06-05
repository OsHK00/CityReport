package com.ciudadreporta.api.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryAlmacenamientoService implements AlmacenamientoService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String subir(MultipartFile archivo, String carpeta) {
        try {
            String contentType = archivo.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Solo se permiten archivos de imagen");
            }

            String publicId = carpeta + "/" + System.currentTimeMillis();

            Map<?, ?> params = ObjectUtils.asMap(
                "public_id", publicId,
                "format", "webp",
                "resource_type", "image"
            );

            Map<?, ?> result = cloudinary.uploader().upload(archivo.getBytes(), params);
            return (String) result.get("secure_url");

        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen a Cloudinary: " + e.getMessage());
        }
    }
}
