package med.voll.api.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> tratarErroDuplicidade(DataIntegrityViolationException ex) {
        String mensagem = "Já existe um cadastro com os dados informados.";
        return ResponseEntity.status(409).body(new ErroResponse(mensagem));
    }

    public record ErroResponse(String erro) {

    }
}
