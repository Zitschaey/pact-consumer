package de.pact.consumer.app;

public class Book {

	private String isbn;
	private String author;
	private String title;
	private int id;

	public Book() {
		super();
	}

	public Book(String isbn, String author, String title, int id) {
		super();
		this.isbn = isbn;
		this.author = author;
		this.title = title;
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
