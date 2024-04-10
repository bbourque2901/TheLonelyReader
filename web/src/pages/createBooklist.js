import BookTrackerClient from '../api/bookTrackerClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed for the create booklist page of the website.
 */
class CreateBooklist extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submit', 'redirectToViewBooklist'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.redirectToViewBooklist);
        this.header = new Header(this.dataStore);
    }

    /**
     * Add the header to the page and load the BookTrackerClient.
     */
    mount() {
        document.getElementById('create').addEventListener('click', this.submit);

        this.header.addHeaderToPage();

        this.client = new BookTrackerClient();
    }

    /**
     * Method to run when the create booklist submit button is pressed. Call the BookTrackerClient to create the
     * booklist.
     */
    async submit(evt) {
        evt.preventDefault();

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        const createButton = document.getElementById('create');
        const origButtonText = createButton.innerText;
        createButton.innerText = 'Loading...';

        const booklistName = document.getElementById('booklist-name').value;
        const tagsText = document.getElementById('tags').value;

        let tags;
        if (tagsText.length < 1) {
            tags = null;
        } else {
            tags = tagsText.split(/\s*,\s*/);
        }

        const booklist = await this.client.createBooklist(booklistName, tags, (error) => {
            createButton.innerText = origButtonText;
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            errorMessageDisplay.classList.remove('hidden');
        });
        this.dataStore.set('booklist', booklist);
    }

    /**
     * When the booklist is updated in the datastore, redirect to the view booklist page.
     */
    redirectToViewBooklist() {
        const booklist = this.dataStore.get('booklist');
        if (booklist != null) {
            window.location.href = `/booklist.html?id=${booklist.id}`;
        }
    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const createBooklist = new CreateBooklist();
    createBooklist.mount();
};

window.addEventListener('DOMContentLoaded', main);
