package dk.lundogbendsen.springbootcourse.testing.example.bookstore.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(name = Book.FIND_ALL, query = "SELECT b FROM Book b")
})
public class Book implements Serializable {


    public static final String FIND_ALL = "Book.findAll";
    private static final float VAT_RATE = 5.5f;

    @Id
    @GeneratedValue
    private Long id;

    @Version
    @Column(name = "version")
    private int version;

    @Column(length = 17)
    private String isbn;

    @Column(length = 50)
    private String name;

    @Column(length = 300)
    private String description;

    @Column(length = 255)
    private String pictureURL;

    @Column(length = 13)
    private Float price;

    @Transient
    private Float vat;

    @Column
    private String author;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Book)) {
            return false;
        }
        Book other = (Book) obj;
        if (id != null) {
            if (!id.equals(other.id)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
        if (price != null) {
            vat = price * VAT_RATE / 100;
        }

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public Float getVat() {
        return vat;
    }


    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (id != null)
            result += "id: " + id;
        result += ", version: " + version;
        if (isbn != null && !isbn.trim().isEmpty())
            result += ", isbn: " + isbn;
        if (name != null && !name.trim().isEmpty())
            result += ", name: " + name;
        if (description != null && !description.trim().isEmpty())
            result += ", description: " + description;
        if (pictureURL != null && !pictureURL.trim().isEmpty())
            result += ", pictureURL: " + pictureURL;
        if (price != null)
            result += ", price: " + price;
        if (author != null && !author.trim().isEmpty())
            result += ", author: " + author;
        return result;
    }
}