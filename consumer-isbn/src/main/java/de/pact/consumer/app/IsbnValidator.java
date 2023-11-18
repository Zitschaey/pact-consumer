package de.pact.consumer.app;

import org.springframework.web.client.RestTemplate;

public class IsbnValidator {
	public String url = "http://localhost:8081";

	public Book isValidForBook(int bookId) {
		Book book = new RestTemplate().getForObject(url + "/books/" + bookId, Book.class);
		if (book != null) {
			return book;
		}
		return null;
	}

	public boolean isValidIsbnForBookId(int bookId) {
		Book book = new RestTemplate().getForObject(url + "/books/" + +bookId, Book.class);
		if (book != null) {
			String isbn = book.getIsbn();
			return isValidIsbn(isbn);
		}
		return false;
	}

	private boolean isValidIsbn(String isbn) {

		char[] ca;
		ca = isbn.toCharArray();

		if (isbn.matches("\\d{9}[\\dxX]")) {
			int sum = 0;
			for (int index = 0; index < ca.length - 1; index++) {
				sum += (index + 1) * Character.getNumericValue(ca[index]);
			}
			int checkSum = sum % 11;
			char checkDigit = ca[9];
			if (checkSum != 10) {
				return (checkSum == Character.getNumericValue(checkDigit));
			} else {
				return (checkDigit == 'x' || checkDigit == 'X');
			}

		}

		if (isbn.matches("\\d{13}")) {
			int sum = 0;
			for (int i = 0; i < 13; i++) {
				int digit = Character.getNumericValue(isbn.charAt(i));
				sum += (i % 2 == 0) ? digit : 3 * digit;
			}
			return (sum % 10 == 0);
		}
		return false;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
