package org.northernarc.librarymanagement.service;

import org.northernarc.librarymanagement.dto.BookRequestDTO;
import org.northernarc.librarymanagement.dto.BookResponseDTO;
import org.northernarc.librarymanagement.dto.BookUpdateDTO;

import java.util.List;

public interface BookService {
    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO);
    public BookResponseDTO updateBook(Long bookId,BookUpdateDTO bookUpdateDTO);
    public BookResponseDTO getBookById(Long bookId);
    public List<BookResponseDTO> getAllBook();
    public void deleteBook(Long bookId);

}
