# Design Document

## LonelyReads

## 1. Problem Statement

To give our clients the ability to categorize and track books being read without the social pressures on other reading apps.

## 2. Top Questions to Resolve in Review

1. How to acquire book data for the Books table. Will we need to manually input a bunch of books or is there a simpler solution?
2. How will we be able to let our clients input booklist specific data about each book? (ratings, comments, progress tracking)
3. How to make our front end user friendly while keeping it stylish and cute.

## 3. Use Cases

U1. As a user, I want to create a list of books with a name and list of tags.

U2. As a user, I want to retrieve my list(s) with a given ID.

U3. As a user, I want to be able to update list names and tags.

U4. As a user, I want to be able to add books to a list.

U5. As a user, I want to be able to remove books from a list.

U6. As a user, I want to be able to remove a booklist entirely from my account.

U7. As a user, I want to be able to add ratings to the books in my booklist(s).

U8. As a user, I want to be able to add comments to the books in my booklist(s).

U9. As a user, I want to be able to see the books I'm currently reading in my booklist(s).

U10. As a user, I want to see how far along I am in the books I'm currently reading in my booklist(s).

U11. As a user, I want to be able to search for a booklist by name.

U12. As a user, I want to be able to search for books by title/ASIN.

## 4. Project Scope

### 4.1. In Scope

- Creating, retrieving, and updating a booklist
- Adding to and retrieving a saved booklist's list of books
- Retrieving all booklists a customer has created

### 4.2. Out of Scope

- importing a full library of books so our database is complete
- using a third party tracking service like kindle so user doesn't have to manually update progress in a specific book whil reading

# 5. Proposed Architecture Overview

Our minimum lovable product will include creating, retrieving, and updating booklists, as well as adding to and retrieving a saved booklist's list of books. 

We will use API Gateway and Lambda to create eight endpoints (as outlined below) that will handle the creation, update, and retrieval of playlists to satisfy our requirements. 

We will utilize DynamoDB to store booklists and books. For simpler book list retrieval, we will store the list of books in a given booklist directly in the booklists table.

LonelyReads will also provide a web interface for users to manage their booklists. A main page providing a list view of all of their booklists will let them create new booklists and link off to pages per-booklists to update metadata and add books.

# 6. API

## 6.1. Public Models
// BooklistModel
- String id;
- String name;
- String customerId;
- Integer bookCount;
- List<String> tags;
- List<String> asin;

// BookModel
- String asin;
- String title;
- String author;
- String genre;
- Integer rating;
- String comments;
- Boolean currentlyReading;
- Integer percentageComplete;

## 6.2. Get BookList Endpoint

- accepts GET requests to /booklists/:id
- accepts a booklist id and returns corresponding BooklistModel
    - will throw BooklistNotFoundException if the given booklist id is not found

## 6.3 Create Booklist Endpoint

- accepts POST requests to /booklists
- accepts data to create a new booklist(name, customerId, tags), returns new booklist and unique id
- For security concerns, we will validate the provided booklist name does not contain any invalid characters: " ' \
    - If the booklist name contains any of the invalid characters, will throw an InvalidAttributeValueException.

## 6.4 Update Booklist Endpoint

- accepts PUT requests to /booklists/:id
- can update playlist id, name, and customerId. Returns updated booklist
- will throw BooklistNotFoundException if the given booklist id is not found
- For security concerns, we will validate the provided booklist name does not contain any invalid characters: " ' \
    - If the booklist name contains any of the invalid characters, will throw an InvalidAttributeValueException.
 
## 6.5 Add Book to Booklist Endpoint

- accepts POST requests to /booklists/:id/books
- accepts booklist ID and book to be added, book is specified by asin
- will throw BooklistNotFoundException if the given booklist id is not found
- will throw BookNotFoundException if the given book id is not found
- By default will insert book to end of the booklist
    - can add optional readNext parameter that will insert book to front of booklist
 
## 6.6 Remove Book from Booklist Endpoint

- accepts DELETE requests to /booklists/:id/books
- accepts a booklist id and book to be removed, book is specified by asin
- will throw BooklistNotFoundException if the given booklist id is not found
- will throw BookNotFoundException if the given book id is not found

 ## 6.7 Get Booklist Books Endpoint

- accepts GET request to /playlist/:id/songs
- retrieves all books of a booklist with the given booklist id
    - returns in default order, unless optional order parameter is provided (default, alpha, shuffle)
- if booklist is found, but contains no books, and empty list will be returned
- will throw BookNotFoundException if the given book id is not found

## 6.8 Get Booklists for User Endpoint

- accepts GET requets to /playlists/:customerId
- accepts customerId and returns BooklistModels created by user
- if none created by user, empty list returned

## 6.9 Update Book on User Booklist Enpoint

- accepts PUT requests to /booklists/:id
- user can update a book in their booklist's attributes (rating, comments, currentlyReading, percentageComplete). returns updated book on user 	booklist
- will throw BooklistNotFoundException if the given booklist id is not found
- will throw BookNotFoundException if the given book id is not found

# 7. Tables

//booklists
- id: partition key, string
- name: string
- customerId: string (customerId-bookList-index partitionKey)
- bookCount: number
- tags: stringSet
- bookList: list

//books
- asin: partition key, string
- title: string
- author: string
- genre: string
- rating: number
- comments: string
- currentlyReading: BOOL
- percentageComplete: number

# 8. Pages

![image](https://github.com/nss-se-cohort-04/u5-projecttemplate-carbon/assets/146966793/d8bf9a55-3554-468d-8363-d8e48459199d)

