package de.pact.consumer.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/isbnconsumer")
public class BookController {

	@PostMapping("/validatebook")
	public ResponseEntity<String> validateBook(@RequestBody Book buch) {

		if (isValidIsbn(buch.getIsbn())) {
			return ResponseEntity.ok("Buchobjekt ist gültig.");
		} else {
			return ResponseEntity.badRequest().body("Ungültiges Buchobjekt.");
		}
	}

	private boolean isValidIsbn(String isbn) {
		if (isbn.length() != 10) {
			return false;
		}

		int sum = 0;
		for (int i = 0; i < 9; i++) {
			char digit = isbn.charAt(i);
			if (!Character.isDigit(digit)) {
				return false;
			}
			sum += (digit - '0') * (10 - i);
		}
		char lastDigit = isbn.charAt(9);
		if (lastDigit == 'X' || lastDigit == 'x') {
			sum += 10;
		} else if (Character.isDigit(lastDigit)) {
			sum += (lastDigit - '0');
		} else {
			return false;
		}

		return sum % 11 == 0;
	}
}
