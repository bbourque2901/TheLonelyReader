import MusicPlaylistClient from '../api/musicPlaylistClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the update book page of the website.
 */
class UpdateBook extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'clientLoaded', 'submit', 'populateBook', 'redirectToBooklist'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.redirectToBooklist);
        this.header = new Header(this.dataStore);
        console.log("updateBook constructor");
    }

    /**
     * Add the header to the page and load the MusicPlaylistClient.
     */
    mount() {
        document.getElementById('update').addEventListener('click', this.submit);

        this.header.addHeaderToPage();

        this.client = new MusicPlaylistClient();
    }

    /**
     * Once the client is loaded, get the book metadata.
     */
    clientLoaded() {
        const urlParams = new URLSearchParams(window.location.search);
        const title = urlParams.get('book-title');
        document.getElementById('book-title').innerText = "Loading Title ...";
//      PROBABLY NEED TO ADD A BACKEND METHOD TO GET THE BOOK?
//        const urlParams = new URLSearchParams(window.location.search);
//        const booklistId = urlParams.get('id');
//        document.getElementById('booklist-name').innerText = "Loading Booklist ...";
//        const booklist = await this.client.getBooklist(booklistId);
//        this.dataStore.set('booklist', booklist);
//        document.getElementById('books').innerText = "(loading books...)";
//        const books = await this.client.getBooklistBooks(booklistId);
//        this.dataStore.set('books', books);
    }

    /**
     * Add the book info to the page.
     */
    populateBook() {
        let book = this.dataStore.get('book');

        let titleHtml = '<table id="title"><tr><th>Title</th></tr>';
        titleHtml += `
        <tr id="book.title">
            <td>${book.title}</td>
        </tr>`;
        document.getElementById('book').innerHTML = titleHtml;

    }

    /**
     * Method to run when the update book submit button is pressed. Call the MusicPlaylistService to update the
     * book.
     */
    async submit(evt) {
        evt.preventDefault();

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const updateButton = document.getElementById('update');
        const origButtonText = updateButton.innerText;
        updateButton.innerText = 'Loading...';

        const commentsText = document.getElementById('comments').value;
        const id = document.getElementById('booklist-id').value;
        const asin = document.getElementById('book-asin').value;

        let comments;
        if (commentsText.length < 1) {
            comments = null;
        } else {
            comments = commentsText.split(/\s*,\s*/);
        }

        const book = await this.client.updateBookInBooklist(id, asin, (error) => {
            createButton.innerText = origButtonText;
            console.log("update button clicked on updateBook page");
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });
        this.dataStore.set('book', book);
    }

    /**
     * When the book is updated in the datastore, redirect back to the booklist page.
     */
    redirectToBooklist() {
        const book = this.dataStore.get('book');
        if (book != null) {
            window.location.href = `/booklist.html?id=${booklist.id}`;
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const updateBook = new UpdateBook();
    updateBook.mount();
};

window.addEventListener('DOMContentLoaded', main);
