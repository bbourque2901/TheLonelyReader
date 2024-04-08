import MusicPlaylistClient from '../api/musicPlaylistClient';

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

        this.bindClassMethods(['mount', 'search', 'displaySearchResults', 'getHTMLForSearchResults', 'add'], this);

        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);

        this.dataStore.addChangeListener(this.displaySearchResults);
        console.log("searchBooks constructor");
    }

    /**
     * Add the header to the page and load the MusicPlaylistClient.
     */
    mount() {
        // Wire up the form's 'submit' event and the button's 'click' event to the search method.
        document.getElementById('search-books-form').addEventListener('submit', this.search);
        document.getElementById('search-books-btn').addEventListener('click', this.search);
        document.getElementById('search-books-results-container').addEventListener('click', this.add);

        this.client = new MusicPlaylistClient();
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
        if (searchResults.length === 0) {
            return '<h4>No results found</h4>';
        }

        let html = '<table><tr><th></th><th>ISBN</th><th>Title</th><th>Author</th><th>Genre</th><th>Add Book</th></tr>';
        for (const res of searchResults) {
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
                    <button data-title="${res.title}" data-booklist-id="sSKYZ" class="button add-book">Add to Booklist</button>
                </td>
            </tr>`;
        }
        html += '</table>';

        return html;
    }

//    /* When the user clicks on the button,
//    toggle between hiding and showing the dropdown content */
//    myFunction() {
//      document.getElementById("myDropdown").classList.toggle("show");
//    }
//
//    // Close the dropdown menu if the user clicks outside of it
//    window.onclick = function(event) {
//      if (!event.target.matches('.dropbtn')) {
//        var dropdowns = document.getElementsByClassName("dropdown-content");
//        var i;
//        for (i = 0; i < dropdowns.length; i++) {
//          var openDropdown = dropdowns[i];
//          if (openDropdown.classList.contains('show')) {
//            openDropdown.classList.remove('show');
//          }
//        }
//      }
//    }

    async add(e) {
       const addButton = e.target;
       if (!addButton.classList.contains("add-book")) {
           return;
       }

       addButton.innerText = "Adding...";

       const errorMessageDisplay = document.getElementById('error-message');
       errorMessageDisplay.innerText = ``;
       errorMessageDisplay.classList.add('hidden');

       await this.client.addBookToBooklist(addButton.dataset.booklistId, addButton.dataset.title, (error) => {
           errorMessageDisplay.innerText = `Error: ${error.message}`;
           errorMessageDisplay.classList.remove('hidden');
       });

       document.getElementById(addButton.dataset.title + addButton.dataset.booklistId).add();
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