package com.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstore.dto.bookdto.BookDto;
import com.example.bookstore.dto.bookdto.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.bookdto.CreateBookRequestDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.Category;
import com.example.bookstore.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    private static final long ID = 1;
    private Book book;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setId(ID);
        book.setTitle("Odyssey");
        book.setAuthor("Homer");
        book.setIsbn("98765432");
        book.setPrice(BigDecimal.valueOf(250.50));
    }

    @AfterEach
    public void cleanUp() {
        book = null;
    }

    @Test
    public void saveBook_ValidFields_ReturnValidBook() {
        CreateBookRequestDto bookRequestDto = createBook();
        BookDto expected = createBookDto();
        when(bookMapper.toModel(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.save(bookRequestDto);
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void findAll_ValidPageable_ReturnListOfBooks() {
        List<Book> books = new ArrayList<>();
        books.add(book);
        Page<Book> bookPage = new PageImpl<>(books);
        List<BookDto> expected = new ArrayList<>();
        BookDto bookDto = createBookDto();
        expected.add(bookDto);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        List<BookDto> actual = bookService.findAll(mock(Pageable.class));
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void getBookById_ValidId_ReturnBook() {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        BookDto expected = createBookDto();
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.getBookById(ID);
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findById(ID);

    }

    @Test
    public void getBookById_InvalidId_ThrowEntityNotFoundException() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(ID));
        verify(bookRepository, times(1)).findById(ID);
    }

    @Test
    public void deleteById_ValidId_DeleteBook() {
        bookService.deleteById(ID);
        verify(bookRepository, times(1)).deleteById(ID);
    }

    @Test
    @DisplayName("Update a book with valid id and dto")
    public void updateById_ValidId_UpdateBook() {
        Long idExisted = 1L;
        Book bookFromDB = getBook().setId(idExisted);
        Book newBook = getBook().setTitle("Agamemnon").setAuthor("Eschyle");
        CreateBookRequestDto requestDto = createBook().setTitle("Agamemnon").setAuthor("Eschyle");
        BookDto responseBookDto = createBookDto().setTitle(newBook.getTitle())
                .setAuthor(newBook.getAuthor());
        when((bookRepository.findById(idExisted))).thenReturn(Optional.of(bookFromDB));
        when(bookRepository.save(newBook)).thenReturn(newBook.setId(idExisted));
        when(bookMapper.toDto(newBook)).thenReturn(responseBookDto);
        BookDto actual = bookService.update(requestDto, ID);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(responseBookDto, actual);
    }

    @Test
    public void findAllByCategoryId_ValidCategoryId_ReturnListOfBooks() {
        List<Book> books = new ArrayList<>();
        books.add(book);
        when(bookRepository.findAllByCategoryId(anyLong(), any(Pageable.class)))
                .thenReturn(books);
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setTitle("Odyssey")
                .setAuthor("Homer")
                .setIsbn("98765432")
                .setPrice(BigDecimal.valueOf(250.50));
        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(bookDtoWithoutCategoryIds);
        when(bookMapper.toDtoWithoutCategories(any(Book.class)))
                .thenReturn(bookDtoWithoutCategoryIds);
        List<BookDtoWithoutCategoryIds> actual = bookService
                .findAllByCategoryId(anyLong(), any(Pageable.class));
        assertEquals(expected, actual);
        verify(bookRepository, times(1))
                .findAllByCategoryId(anyLong(), any(Pageable.class));
    }

    private CreateBookRequestDto createBook() {
        return new CreateBookRequestDto()
                .setTitle("Odyssey")
                .setAuthor("Homer")
                .setIsbn("98765432")
                .setPrice(BigDecimal.valueOf(250.50))
                .setCategoryIds(Set.of(1L));
    }

    private BookDto createBookDto() {
        return new BookDto()
                .setId(1L)
                .setTitle("Odyssey")
                .setAuthor("Homer")
                .setIsbn("98765432")
                .setPrice(BigDecimal.valueOf(250.50))
                .setCategoryIds(Set.of(1L));
    }

    private Book getBook() {
        return new Book()
                .setAuthor("Homer")
                .setIsbn("987654332")
                .setTitle("Odyssey")
                .setDescription("Description")
                .setPrice(BigDecimal.valueOf(250.50))
                .setCoverImage("image.jpg")
                .setCategories(Set.of(
                        new Category().setId(1L)));
    }
}
