import BookTrackerClient from '../api/bookTrackerClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view booklist page of the website.
 */
 class ViewBooklist extends BindingClass {
    constructor() {
            super();
            this.bindClassMethods(['clientLoaded', 'mount', 'addBooklistToPage', 'dummyChart', 'addBooksToPage', 'addBook', 'remove',
                        'redirectToUpdateBook', 'updateBooklistName'], this);
            this.dataStore = new DataStore();
            this.dataStore.addChangeListener(this.addBooklistToPage);
            this.dataStore.addChangeListener(this.addBooksToPage);
            this.dataStore.addChangeListener(this.dummyChart);
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
      * Add the header to the page and load the BookTrackerClient.
      */
     mount() {
         document.getElementById('add-book').addEventListener('click', this.addBook);
         document.getElementById('books').addEventListener('click', this.remove);
         document.getElementById('update-booklist').addEventListener('click', this.updateBooklistName);
         document.getElementById('books').addEventListener('click', this.redirectToUpdateBook);


         this.header.addHeaderToPage();

         this.client = new BookTrackerClient();
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

       //logic to create the chart

     async dummyChart() {
        const genreMap = new Map();
        const books = this.dataStore.get('books');

        if (books == null) {
                       return;
                   }
        let book;
        for (book of books) {
           if (genreMap.has(book.genre)) {
               let numberOfBooks = genreMap.get(book.genre)
               genreMap.set(book.genre, numberOfBooks+1)
           } else {
           genreMap.set(book.genre, 1)
        }
        }
        console.log(genreMap)
        console.log(genreMap.values())
        console.log(genreMap.keys())
        const genreLabel = Array.from( genreMap.keys() );
        const genreNumbers = Array.from( genreMap.values() );

        let ctx = document.getElementById('myChart');

      new Chart(ctx, {
        type: 'pie',
        data: {
          labels: genreLabel,
          datasets: [{
            label: 'Number of Books in Genre',
            data: genreNumbers,
            borderWidth: 1,
            backgroundColor: [
                                  'rgb(252,194,174)',
                                  'rgb(223,126,151)',
                                  'rgb(123,75,131)',
                                  'rgb(170,101,156)',
                                  'rgb(256,192,148)',
                                  'rgb(62,29,100)',
                                  'rgba(207,119,157,255)']
          }]
        },
        options: {
          scales: {
          }
        }
      });

    }
        /**
         * Method to run when the add book booklist submit button is pressed. Call the BookTrackerService to add a book to the
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
          * when button is clicked, user is prompted to update booklist name.
          */
          async updateBooklistName() {
          const errorMessageDisplay = document.getElementById('error-message');
          errorMessageDisplay.innerText = ``;
          errorMessageDisplay.classList.add('hidden');

          const newName = prompt("Enter new booklist name: ");
          if (!newName) return;

          const booklist = this.dataStore.get('booklist');
          if (booklist == null) {
            return;
          }

          document.getElementById('booklist-name').innerText = 'Updating...';

          const newBooklist = await this.client.updateBooklistName(booklist.id, newName, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
          });

          document.getElementById('booklist-name').innerText = newName;
          this.dataStore.set('booklist', newBooklist);
          console.log("button clicked!");
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
        viewbooklist.dummyChart();
  };

  window.addEventListener('DOMContentLoaded', main);
