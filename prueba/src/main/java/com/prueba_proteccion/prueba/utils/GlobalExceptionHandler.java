package com.prueba_proteccion.prueba.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.JsonMappingException;

@ControllerAdvice()
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * Manejo de la excepción GlobalException, que se utiliza para encapsular
	 * excepciones de tipo Exception con un status HTTP y un mensaje
	 * personalizado.
	 * 
	 * @param ex   La excepci n GlobalException que se va a manejar
	 * @param request    La solicitud HTTP que gener la excepci n
	 * @return          Una respuesta HTTP con el status y mensaje de la excepci n
	 */
	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<?> handleGlobalException(GlobalException ex, WebRequest request) {
		ApiResponse<Object> response = new ApiResponse<>(ex.getStatus().value(), ex.getMessage(), true, null);
		return new ResponseEntity<>(response, ex.getStatus());
	}


	/**
	 * Manejo de la excepción RuntimeException, que se lanza cuando se produce una excepci n
	 * no controlada por el programa. Se devuelve una respuesta HTTP con un status 409
	 * (Conflict) y un mensaje que describe la excepci n.
	 * 
	 * @param ex   La excepci n RuntimeException que se va a manejar
	 * @param request    La solicitud HTTP que gener la excepci n
	 * @return          Una respuesta HTTP con el status y mensaje de la excepci n
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleRuntimeException(RuntimeException ex, WebRequest request) {
		ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CONFLICT.value(), ex.getMessage(), true, null);
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}


	/**
	 * Manejo de la excepción MethodArgumentNotValidException, que se lanza
	 * cuando se produce una excepción al validar los argumentos de un método.
	 * Se devuelve una respuesta HTTP con un status 400 (Bad Request) y un mensaje
	 * que describe la excepción.
	 * 
	 * @param ex   La excepción MethodArgumentNotValidException que se va a manejar
	 * @param request    La solicitud HTTP que gener la excepción
	 * @return          Una respuesta HTTP con el status y mensaje de la excepción
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
		// Obtener el primer mensaje de error
		String errorMessage = ex.getBindingResult().getFieldErrors().stream().findFirst()
				.map(FieldError::getDefaultMessage).orElse("Error de validación.");

		// Crear la respuesta simplificada
		ApiResponse<Object> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), errorMessage, true, null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}


	/**
	 * Manejo de la excepción BindException, que se lanza cuando se produce
	 * un error al intentar enlazar un objeto con un formulario.
	 * Se devuelve una respuesta HTTP con un status 400 (Bad Request) y un mensaje
	 * que describe la excepción.
	 * 
	 * @param ex   La excepción BindException que se va a manejar
	 * @param request    La solicitud HTTP que gener la excepción
	 * @return          Una respuesta HTTP con el status y mensaje de la excepción
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<?> handleBindException(BindException ex, WebRequest request) {
		// Obtener el primer mensaje de error
		String errorMessage = ex.getBindingResult().getFieldErrors().stream().findFirst()
				.map(FieldError::getDefaultMessage).orElse("Error de validación.");

		// Crear la respuesta simplificada
		ApiResponse<Object> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), errorMessage, true, null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Manejo de la excepción Exception, que se lanza cuando se produce
	 * una excepción no controlada por el programa. Se devuelve una respuesta HTTP
	 * con un status 500 (Internal Server Error) y un mensaje que describe la
	 * excepci n.
	 * 
	 * @param ex   La excepci n Exception que se va a manejar
	 * @param request    La solicitud HTTP que gener la excepci n
	 * @return          Una respuesta HTTP con el status y mensaje de la excepci n
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException(Exception ex, WebRequest request) {
		ApiResponse<Object> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
				true, null);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	/**
	 * Manejo de la excepción NumberFormatException, que se lanza cuando se produce un error al intentar parsear un string a un número.
	 * Se devuelve una respuesta HTTP con un status 400 (Bad Request) y un mensaje que describe la excepción.
	 *
	 * @param ex   La excepción NumberFormatException que se va a manejar
	 * @param request    La solicitud HTTP que gener la excepción
	 * @return          Una respuesta HTTP con el status y mensaje de la excepción
	 */
	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<?> handleNumberFormatException(NumberFormatException ex, WebRequest request) {
		// Log (opcional)
		logger.error("NumberFormatException: ", ex);
		// Respuesta amigable para el cliente
		ApiResponse<Object> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Formato numérico incorrecto",
				true, null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}


	/**
	 * Manejo de la excepción JsonMappingException, que se lanza cuando se produce un error al intentar parsear un json a un objeto.
	 * Se devuelve una respuesta HTTP con un status 400 (Bad Request) y un mensaje que describe la excepción.
	 *
	 * @param ex   La excepción JsonMappingException que se va a manejar
	 * @param request    La solicitud HTTP que gener la excepción
	 * @return          Una respuesta HTTP con el status y mensaje de la excepción
	 */
	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<?> handleJsonMappingException(JsonMappingException ex, WebRequest request) {
		// Log (opcional)
		logger.error("JsonMappingException: ", ex);
		// Respuesta amigable para el cliente
		ApiResponse<Object> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Error al procesar los datos",
				true, null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
/**
 * Maneja la excepci n BusinessExceptionException, que se lanza cuando se produce
 * un error de negocio en el programa. Se devuelve una respuesta HTTP con un status
 * 200 (OK) y un objeto que contiene un mensaje de error. Se utiliza en el
 * controlador de autorizaci n y anulaci n para manejar las excepciones de negocio.
 * 
 * @param ex   La excepci n BusinessExceptionException que se va a manejar
 * @param request    La solicitud HTTP que gener la excepci n
 * @param model    El modelo de la respuesta HTTP
 * @return          Una cadena que identifica la vista que se debe renderizar
 */
	@ExceptionHandler(BusinessException.class)
	public String handleBusinessException(BusinessException ex, WebRequest request, org.springframework.ui.Model model) {
		model.addAttribute("success", false);
		model.addAttribute("message", "❌ " + ex.getMessage());
		return "autorizacion-anulacion-result";
	}

}
