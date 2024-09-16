package tutorin.com.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tutorin.com.constant.StatusMessages;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.model.Image;
import tutorin.com.repository.ImageRepository;
import tutorin.com.service.ImageService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Value("${tutorin_api.image.path}")
    private String path;

    private Path imagePath;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initPath() throws IOException {
        imagePath = Paths.get(path);
        if (!Files.exists(imagePath)) {
            try {
                Files.createDirectories(imagePath);
            } catch (IOException e) {
                String errorMessage = "Failed to create image directory" + path;
                System.err.println(errorMessage);
                throw new RuntimeException(errorMessage, e);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Image save(MultipartFile image) throws BadRequestException, IOException {
        String fileName = validateAndSaveImage(image);
        Path filePath = imagePath.resolve(fileName);

        Image saved = Image.builder()
                .name(fileName)
                .path(filePath.toString())
                .size(image.getSize())
                .contentType(image.getContentType())
                .build();
        return imageRepository.saveAndFlush(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Resource getById(String id) throws NotFoundException, MalformedURLException {
        Image image = findById(id);
        Path filePath = Paths.get(image.getPath());

        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            throw new NotFoundException("File not found or not readable: " + image.getPath());
        }

        return new UrlResource(filePath.toUri());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Image updateById(String id, MultipartFile image) throws NotFoundException, IOException, BadRequestException {
        Image imageResult = findById(id);
        Path filePath = Paths.get(imageResult.getPath());

        Files.delete(filePath);

        String newFileName = validateAndSaveImage(image);
        Path newFilePath = imagePath.resolve(newFileName);

        imageResult.setName(newFileName);
        imageResult.setPath(newFilePath.toString());
        imageResult.setSize(image.getSize());
        imageResult.setContentType(image.getContentType());

        return imageResult;
    }

    private String validateAndSaveImage(MultipartFile image) throws IOException, BadRequestException {
        List<String> allowedContentTypes = List.of("image/jpg", "image/jpeg", "image/png", "image/svg+xml");
        if (!allowedContentTypes.contains(image.getContentType())) {
            throw new BadRequestException("invalid image type");
        }

        String fileName = System.currentTimeMillis() + image.getOriginalFilename();
        Path filePath = imagePath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);

        return fileName;
    }

    private Image findById(String id) throws NotFoundException {
        return imageRepository.findById(id).orElseThrow(() -> new NotFoundException(StatusMessages.IMAGE_NOT_FOUND));
    }
}
