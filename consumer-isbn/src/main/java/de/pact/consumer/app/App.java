package de.pact.consumer.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
public class App {

	private IsbnValidator isbnValidator = new IsbnValidator();

	@GetMapping("/validate-isbn")
	public String validateIsbnForBookId(@RequestParam int bookId) {
		if (isbnValidator.isValidIsbnForBookId(bookId)) {
			return "ISBN is valid for the specified book!";
		} else {
			return "ISBN is not valid for the specified book.";
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}