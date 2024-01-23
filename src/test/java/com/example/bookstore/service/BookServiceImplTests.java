package com.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
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
import com.example.bookstore.repository.category.CategoryRepository;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookServiceImplTests {
    private static final long ID = 1;
    private Book book;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CategoryRepository categoryRepository;
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
        book.setCategories(Set.of(new Category().setId(ID)));
    }

    @AfterEach
    public void cleanUp() {
        book = null;
    }

    @Test
    public void saveBook_ValidFields_ReturnValidBook() {
        CreateBookRequestDto bookRequestDto = createBook();
        BookDto expected = createBookDto();
        Category category = new Category().setId(1L);
        when(bookMapper.toModel(bookRequestDto)).thenReturn(book);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
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
    @DisplayName("Update a book with valid id and dto")
    public void updateById_ValidId_UpdateBook() {
        Long idExisted = 1L;
        Book bookFromDB = getBook().setId(idExisted);
        Book newBook = getBook().setTitle("Agamemnon")
                .setAuthor("Eschyle");
        CreateBookRequestDto requestDto = createBook().setTitle("Agamemnon").setAuthor("Eschyle");
        BookDto responseBookDto = createBookDto().setTitle(newBook.getTitle())
                .setAuthor(newBook.getAuthor()).setId(idExisted);
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
        Long categoryId = 1L;
        Book model = getBook();
        BookDtoWithoutCategoryIds responseDto = getBookWithoutCategoryIdsResponseDto();
        Pageable pageable = Pageable.unpaged();
        when(bookRepository.findAllByCategoryId(categoryId, pageable)).thenReturn(List.of(model));
        when(bookMapper.toDtoWithoutCategories(model))
                .thenReturn(responseDto);
        List<BookDtoWithoutCategoryIds> actual = bookService
                .findAllByCategoryId(categoryId, pageable);
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(responseDto, actual.get(0));
        verify(bookRepository, times(1))
                .findAllByCategoryId(categoryId, pageable);
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
                .setIsbn("98765432")
                .setTitle("Odyssey")
                .setPrice(BigDecimal.valueOf(250.50));

    }

    private BookDtoWithoutCategoryIds getBookWithoutCategoryIdsResponseDto() {
        return new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setAuthor("Homer")
                .setIsbn("98765432")
                .setTitle("Odyssey")
                .setDescription("Description")
                .setPrice(BigDecimal.valueOf(250.55))
                .setCoverImage("image.jpg");
    }
}

