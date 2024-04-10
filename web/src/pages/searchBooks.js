import BookTrackerClient from '../api/bookTrackerClient';

import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const SEARCH_BOOKS_CRITERIA_KEY = 'search-books-criteria';
const SEARCH_BOOKS_RESULTS_KEY = 'search-books-results';
const EMPTY_DATASTORE_STATE = {
    [SEARCH_BOOKS_CRITERIA_KEY]: '',
    [SEARCH_BOOKS_RESULTS_KEY]: [],
};

/**
 * Logic needed for the view books page of the website.
 */
 class SearchBooks extends BindingClass {
    constructor() {
        super();

        this.bindClassMethods(['mount', 'search', 'displaySearchResults', 'getHTMLForSearchResults', 'add', 'showDropdown'], this);

        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);

        this.dataStore.addChangeListener(this.displaySearchResults);
        console.log("searchBooks constructor");
    }

     /**
      * Once the client is loaded, get the booklist metadata and book list.
      */
     async clientLoaded() {
         const urlParams = new URLSearchParams(window.location.search);
         const customerId = urlParams.get('email');
         const booklists = await this.client.getUserBooklists(customerId);
         this.dataStore.set('booklists', booklists);
     }

    /**
     * Add the header to the page and load the BookTrackerClient.
     */
    mount() {
        // Wire up the form's 'submit' event and the button's 'click' event to the search method.
        document.getElementById('search-books-form').addEventListener('submit', this.search);
        document.getElementById('search-books-btn').addEventListener('click', this.search);
        document.getElementById('search-books-results-container').addEventListener('click', this.showDropdown);
        document.getElementById('search-books-results-display').addEventListener('click', this.add);


        this.client = new BookTrackerClient();
        this.clientLoaded();

    }

    /**
     * Uses the client to perform the search,
     * then updates the datastore with the criteria and results.
     * @param evt The "event" object representing the user-initiated event that triggered this method.
     */
    async search(evt) {
        // Prevent submitting the from from reloading the page.
        evt.preventDefault();

        const searchCriteria = document.getElementById('search-books-criteria').value;
        const previousSearchCriteria = this.dataStore.get(SEARCH_BOOKS_CRITERIA_KEY);

        // If the user didn't change the search criteria, do nothing
        if (previousSearchCriteria === searchCriteria) {
            return;
        }

        if (searchCriteria) {
            const results = await this.client.searchBooks(searchCriteria);

            this.dataStore.setState({
                [SEARCH_BOOKS_CRITERIA_KEY]: searchCriteria,
                [SEARCH_BOOKS_RESULTS_KEY]: results,
            });
        } else {
            this.dataStore.setState(EMPTY_DATASTORE_STATE);
        }
    }

    /**
     * Pulls search results from the datastore and displays them on the html page.
     */
    displaySearchResults() {
        const searchCriteria = this.dataStore.get(SEARCH_BOOKS_CRITERIA_KEY);
        const searchResults = this.dataStore.get(SEARCH_BOOKS_RESULTS_KEY);

        const searchBooksResultsContainer = document.getElementById('search-books-results-container');
        const searchBooksCriteriaDisplay = document.getElementById('search-books-criteria-display');
        const searchBooksResultsDisplay = document.getElementById('search-books-results-display');

        if (searchCriteria === '') {
            searchBooksResultsContainer.classList.add('hidden');
            searchBooksCriteriaDisplay.innerHTML = '';
            searchBooksResultsDisplay.innerHTML = '';
        } else {
            searchBooksResultsContainer.classList.remove('hidden');
            searchBooksCriteriaDisplay.innerHTML = `"${searchCriteria}"`;
            searchBooksResultsDisplay.innerHTML = this.getHTMLForSearchResults(searchResults);
        }
    }

    /**
     * Create appropriate HTML for displaying searchResults on the page.
     * @param searchResults An array of books objects to be displayed on the page.
     * @returns A string of HTML suitable for being dropped on the page.
     */
    getHTMLForSearchResults(searchResults) {
        const booklists = this.dataStore.get('booklists');
        if (booklists == null) {
            return;
        }

        if (searchResults.length === 0) {
            return '<h4>No results found</h4>';
        }

        let html = '<table><tr><th></th><th>ISBN</th><th>Title</th><th>Author</th><th>Genre</th><th>Add Book</th></tr>';
        for (const res of searchResults) {
            let options = '';
            for (var i = 0; i < booklists.length; i++) {
                let option = '<option value="'+booklists[i].id+'" title="'+res.title+'">'+booklists[i].name+'</option>';
                options += option;
            }

            html += `
            <tr>
                <td>
                    <img src=${res.thumbnail} />
                </td>
                <td>${res.asin}</td>
                <td>${res.title}</td>
                <td>${res.author}</td>
                <td>${res.genre}</td>
                <td>
                    <div class="dropdown">
                        <button id="button" data-title="${res.title}" class="dropbtn">Add to Booklist</button>
                        <div id="myDropdown" class="dropdown-content">
                            ${options}
                        </div>
                    </div>
                </td>
            </tr>`;
        }

        html += '</table>';

        return html;
    }

    /**
     * Shows a dropdown menu of a user's booklist to add a book to
     * @param event the current click event
     */
    async showDropdown(event) {
        const parent = event.target.parentNode;
        parent.querySelector('.dropdown-content').classList.toggle('show');
        if (event.target.matches('.dropbtn')) {
            event.target.innerText = 'Adding to...';
        }
        window.onclick = function(event) {
            if (!event.target.matches('.dropbtn')) {
                document.querySelectorAll('.dropbtn')
                    .forEach(element => element.innerText = 'Add to Booklist');
                document.querySelectorAll('.dropdown-content.show')
                    .forEach(element => element.classList.remove('show'));
            }
        }
    }

    /**
     * Adds a book from searchResults to the chosen booklist in dropdown menu
     * @param event the current click event
     */
    async add(e) {
        const addButton = e.target;

        const errorMessageDisplay = document.getElementById('error-message');
        errorMessageDisplay.innerText = ``;
        errorMessageDisplay.classList.add('hidden');

        await this.client.addBookToBooklist(addButton.value, addButton.title, (error) => {
            errorMessageDisplay.innerText = `Error: ${error.message}`;
            // Network Error occurs when clicking 'Add to Booklist' ???
            // DynamoDBMappingException occurs when clicking out of dropdown and not selecting booklist
            // Hiding these two errors, only showing the DuplicateBookException
            if (errorMessageDisplay.innerText.includes("DuplicateBookException")) {
                errorMessageDisplay.classList.remove('hidden');
            }
        });

       document.getElementById(addButton.title + addButton.dataset.value).add();
    }
 }

 /**
  * Main method to run when the page contents have loaded.
  */
 const main = async () => {
     const searchBooks = new SearchBooks();
     searchBooks.mount();
 };

 window.addEventListener('DOMContentLoaded', main);