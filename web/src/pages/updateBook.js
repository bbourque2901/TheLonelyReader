import BookTrackerClient from '../api/bookTrackerClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the update book page of the website.
 */
class UpdateBook extends BindingClass {

    urlParams = new URLSearchParams(window.location.search);
    id = this.urlParams.get('id');
    asin = this.urlParams.get('asin');

    constructor() {
        super();
        this.bindClassMethods(['mount', 'clientLoaded', 'submit', 'addBookToPage', 'redirectToBooklist'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addBookToPage);
        this.header = new Header(this.dataStore);
        console.log("updateBook constructor");
    }

    /**
     * Add the header to the page and load the BookTrackerClient.
     */
    mount() {
        document.getElementById('update-book').addEventListener('click', this.submit);

        this.header.addHeaderToPage();

        this.client = new BookTrackerClient();
        this.clientLoaded();
    }

    /**
     * Once the client is loaded, get the book metadata.
     */
    async clientLoaded() {
        document.getElementById('title').innerText = "Loading Book ...";

        const book = await this.client.getBookFromBooklist(this.id, this.asin);
        this.dataStore.set('book', book);
    }

    /**
     * When the book is updated in the datastore, update the book metadata on the page.
     */
    addBookToPage() {
        const book = this.dataStore.get('book');
        if (book == null) {
            return;
        }

        document.getElementById('thumbnail').innerHTML = `<img src="${book.thumbnail}" alt="Image not found">`;
        document.getElementById('title').innerText = book.title;
        document.getElementById('author').innerText = book.author;
        document.getElementById('genre').innerText = book.genre;
        document.getElementById('page-count').innerText = book.pageCount;
        document.getElementById('asin').innerText = book.asin;
    }

    /**
     * Method to run when the update book submit button is pressed. Call the BookTrackerClient to update the
     * book.
     */
    async submit(evt) {
        evt.preventDefault();

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const updateButton = document.getElementById('update-book');
        const origButtonText = updateButton.innerText;
        updateButton.innerText = 'Loading...';

        const currCheckBox = document.getElementById('currently-reading');
        const curr = currCheckBox.checked ? true : false;
        const rating = document.getElementById('rating').value;
        const perc = document.getElementById('percent-complete').value;

        const book = await this.client.updateBookInBooklist(this.id, this.asin, curr, perc, rating, (error) => {
            updateButton.innerText = origButtonText;
            console.log("update button clicked on updateBook page");
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });
        this.dataStore.set('book', book);
        this.redirectToBooklist(this.id);
    }

    /**
     * When the book is updated in the datastore, redirect back to the booklist page.
     */
    redirectToBooklist(id) {
         window.location.href = `/booklist.html?id=${id}`;

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
