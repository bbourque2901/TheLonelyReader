import MusicPlaylistClient from '../api/musicPlaylistClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view booklist page of the website.
 */
 class ViewBooklist extends BindingClass {
    constructor() {
            super();
            this.bindClassMethods(['clientLoaded', 'mount', 'addBooklistToPage', 'addBooksToPage', 'addBook', 'remove',
                        'redirectToUpdateBook'], this);
            this.dataStore = new DataStore();
            this.dataStore.addChangeListener(this.addBooklistToPage);
            this.dataStore.addChangeListener(this.addBooksToPage);
            this.header = new Header(this.dataStore);
            console.log("viewbooklist constructor");
    }

    /**
     * Once the client is loaded, get the booklist metadata and book list.
     */
    async clientLoaded() {
        const urlParams = new URLSearchParams(window.location.search);
        const booklistId = urlParams.get('id');
        document.getElementById('booklist-name').innerText = "Loading Booklist ...";
        const booklist = await this.client.getBooklist(booklistId);
        this.dataStore.set('booklist', booklist);
        document.getElementById('books').innerText = "(loading books...)";
        const books = await this.client.getBooklistBooks(booklistId);
        this.dataStore.set('books', books);
    }

     /**
      * Add the header to the page and load the MusicPlaylistClient.
      */
     mount() {
         document.getElementById('add-book').addEventListener('click', this.addBook);
         document.getElementById('books').addEventListener('click', this.remove);
         document.getElementById('books').addEventListener('click', this.redirectToUpdateBook);

         this.header.addHeaderToPage();

         this.client = new MusicPlaylistClient();
         this.clientLoaded();

     }

      /**
       * When the booklist is updated in the datastore, update the booklist metadata on the page.
       */
      addBooklistToPage() {
          const booklist = this.dataStore.get('booklist');
          if (booklist == null) {
              return;
          }

          document.getElementById('booklist-name').innerText = booklist.name;
          document.getElementById('booklist-owner').innerText = booklist.customerId;

          let tagHtml = '';
          let tag;
          for (tag of booklist.tags) {
              tagHtml += '<div class="tag">' + tag + '</div>';
          }
          document.getElementById('tags').innerHTML = tagHtml;
      }

       /**
        * When the books are updated in the datastore, update the list of books on the page.
        */
       addBooksToPage() {
           const books = this.dataStore.get('books')
           const booklist = this.dataStore.get('booklist');

           if (books == null) {
               return;
           }

           let bookHtml = '<table id="book-table"><tr><th></th><th>Title</th><th>Author</th><th>Genre</th><th>Rating</th><th>ISBN</th><th>Currently Reading</th><th>Update Book</th><th>Remove Book</th></tr>';
           let book;
           for (book of books) {
               bookHtml += `
               <tr id="${book.asin + booklist.id}">
                   <td>
                        <img src=${book.thumbnail} class="bookImage">
                        <figcaption><b>${book.percentComplete} of ${book.pageCount}</b></figcaption>
                   </td>
                   <td>${book.title}</td>
                   <td>${book.author}</td>
                   <td>${book.genre}</td>
                   <td>${book.rating}</td>
                   <td>${book.asin}</td>
                   <td><input type="checkbox" ${book.currentlyReading ? 'checked' : ''} disabled></td>
                   <td><button data-asin="${book.asin}" data-booklist-id="${booklist.id}" class="button update-book">Update</button></td>
                   <td><button data-asin="${book.asin}" data-booklist-id="${booklist.id}" class="button remove-book">Remove</button></td>
               </tr>`;
           }
           document.getElementById('books').innerHTML = bookHtml;
       }

        /**
         * Method to run when the add book booklist submit button is pressed. Call the MusicPlaylistService to add a book to the
         * booklist.
         */
         async addBook() {
                 const errorMessageDisplay = document.getElementById('error-message');
                 errorMessageDisplay.innerText = ``;
                 errorMessageDisplay.classList.add('hidden');

                 const booklist = this.dataStore.get('booklist');
                 if (booklist == null) {
                     return;
                 }

                 document.getElementById('add-book').innerText = 'Adding...';
                 const asin = document.getElementById('book-asin').value;
                 const booklistId = booklist.id;

                 const books = await this.client.addBookToBooklist(booklistId, asin, (error) => {
                     errorMessageDisplay.innerText = `Error: ${error.message}`;
                     errorMessageDisplay.classList.remove('hidden');
                 });

                 this.dataStore.set('books', books);

                 document.getElementById('add-book').innerText = 'Add Book';
                 document.getElementById("add-book-form").reset();

                 location.reload();
//                 document.getElementById(book-table).append()
         }

         /**
          * when remove button is clicked, removes book from booklist.
          */
          async remove(e) {
                const removeButton = e.target;
                if (!removeButton.classList.contains("remove-book")) {
                    return;
                }

                removeButton.innerText = "Removing...";

                const errorMessageDisplay = document.getElementById('error-message');
                errorMessageDisplay.innerText = ``;
                errorMessageDisplay.classList.add('hidden');

                await this.client.removeBookFromBooklist(removeButton.dataset.booklistId, removeButton.dataset.asin, (error) => {
                   errorMessageDisplay.innerText = `Error: ${error.message}`;
                   errorMessageDisplay.classList.remove('hidden');
               });

                document.getElementById(removeButton.dataset.asin + removeButton.dataset.booklistId).remove()
          }

          /**
            * when update button is clicked, redirects to update book page.
            */
            async redirectToUpdateBook(e) {
                  const updateButton = e.target;
                  if (!updateButton.classList.contains("update-book")) {
                      return;
                  }

                  updateButton.innerText = "Loading...";
                  console.log("update button clicked and now will redirect");

                  if (updateButton != null) {
                      window.location.href = `/updateBook.html?id=${updateButton.dataset.booklistId}&asin=${updateButton.dataset.asin}`;
                  }
            }
}

 /**
  * Main method to run when the page contents have loaded.
  */
  const main = async () => {
        const viewbooklist = new ViewBooklist();
        viewbooklist.mount();
  };

  window.addEventListener('DOMContentLoaded', main);
