package com.momo.toys.be.controller;

import com.momo.toys.be.account.Problem;
import com.momo.toys.be.exception.FileStorageException;
import com.momo.toys.be.exception.ValidationException;
import com.momo.toys.be.factory.CommonUtility;
import com.momo.toys.be.factory.mapper.DocumentMapper;
import com.momo.toys.be.model.Document;
import com.momo.toys.be.service.DocumentService;
import com.momo.toys.be.validation.ValidationData;
import com.momo.toys.be.validation.ValidationProvider;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Function;

import static com.momo.toys.be.enumeration.SupportedType.DOCUMENT_UPLOADING;

@RestController
public class DocumentController {

    @Autowired
    private ValidationProvider validationProvider;

    @Autowired
    private CommonUtility commonUtility;

    @Autowired
    @Qualifier("mongoDocumentService")
    private DocumentService documentService;

    @PostMapping(value = "/document")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_CUSTOMER')")
    public ResponseEntity upload(@RequestParam("file") MultipartFile multipartFile) {

        Problem problem = validatorDocument.apply(multipartFile);
        if (Strings.isNotEmpty(problem.getTitle())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        Document document = DocumentMapper.mapToDocument.apply(multipartFile);
        document = documentService.upload(document);

        return ResponseEntity.status(HttpStatus.CREATED).body(document.getFileUri());
    }

    @GetMapping(value = "/no-auth/document/file-content")
    public ResponseEntity download(@RequestParam("filename") String filename) {

        Document document;
        try {
            document = documentService.readFile(filename);
        } catch (FileStorageException e) {
            Problem problem = commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").body(new ByteArrayResource(document.getFileContent()));
    }

    private Function<MultipartFile, Problem> validatorDocument = multipartFile -> {

        ValidationData validationData = new ValidationData().setMultipartFile(multipartFile);

        try {
            validationProvider.executeValidators(validationData, DOCUMENT_UPLOADING);
        } catch (ValidationException e) {
            return commonUtility.createProblem(HttpStatus.INTERNAL_SERVER_ERROR.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        return new Problem();
    };
}
